/*
 * ArticleViewModel.kt
 * Module: :feature:constitution   ·   Work package: WP-05
 * Screen: Modda (50-modda) (design artboard: Modda.dc.html)
 *
 * Holds StateFlow<ArticleUiState> and handles ArticleAction. No Android UI types.
 *
 * API used (through repositories, see docs/api/openapi.yaml):
 *  - GET  /v1/articles/{key}            the whole reader
 *  - POST /v1/articles/{key}/progress   "reading" on open, "read" at the end (+10 points once)
 *  - POST /v1/me/bookmarks              save
 *  - DELETE /v1/me/bookmarks/article/{id}  unsave
 * Progress and bookmarks need a signed-in user; guests can read everything.
 */
package uz.asos.feature.constitution.article

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.asos.core.common.result.AsosResult
import uz.asos.core.common.text.UzbekFormatters
import uz.asos.core.data.repository.AuthRepository
import uz.asos.core.data.repository.BookmarkRepository
import uz.asos.core.data.repository.ConstitutionRepository
import uz.asos.core.data.repository.UserRepository
import uz.asos.core.model.constitution.Article
import uz.asos.core.model.constitution.ReadingStatus
import uz.asos.core.model.user.BookmarkType
import uz.asos.core.model.user.ReaderTextSize
import javax.inject.Inject
import kotlin.time.TimeSource

@HiltViewModel
class ArticleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val constitutionRepository: ConstitutionRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    /**
     * ArticleRoute(key) stores its argument under "key". Read directly (instead of toRoute())
     * so the ViewModel stays testable on the plain JVM.
     */
    private val articleKey: String = checkNotNull(savedStateHandle.get<String>(KEY_ARG)) {
        "ArticleViewModel needs the \"$KEY_ARG\" navigation argument"
    }

    private val _uiState = MutableStateFlow<ArticleUiState>(ArticleUiState.Loading)
    val uiState: StateFlow<ArticleUiState> = _uiState.asStateFlow()

    private val _events = Channel<ArticleEvent>(Channel.BUFFERED)
    val events: Flow<ArticleEvent> = _events.receiveAsFlow()

    private var loadJob: Job? = null
    private var openedAt = TimeSource.Monotonic.markNow()
    private var alreadyRead = false
    private var markReadSent = false
    private var textSize: ReaderTextSize = ReaderTextSize.MEDIUM

    init {
        observeTextSize()
        load()
    }

    fun onAction(action: ArticleAction) {
        when (action) {
            ArticleAction.OnBookmarkClick -> toggleBookmark()
            is ArticleAction.OnTextSizeSelect -> viewModelScope.launch { userRepository.setReaderTextSize(action.size) }
            ArticleAction.OnToggleFullText -> updateSuccess { it.copy(showFullText = !it.showFullText) }
            ArticleAction.OnReachedEnd -> markRead()
            ArticleAction.OnRetry -> load()
            // Navigation and sharing are handled by ArticleRoute.
            ArticleAction.OnBack,
            ArticleAction.OnShareClick,
            is ArticleAction.OnArticleClick,
            is ArticleAction.OnSiblingClick,
            is ArticleAction.OnTermClick,
            ArticleAction.OnDiscussionClick,
            ArticleAction.OnCasesClick,
            is ArticleAction.OnQuizClick,
            ArticleAction.OnSignInClick,
            -> Unit
        }
    }

    private fun observeTextSize() {
        viewModelScope.launch {
            userRepository.readerTextSize.collect { size ->
                textSize = size
                updateSuccess { it.copy(textSize = size) }
            }
        }
    }

    private fun load() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = ArticleUiState.Loading
            when (val result = constitutionRepository.getArticle(articleKey)) {
                is AsosResult.Success -> {
                    val article = result.data
                    alreadyRead = article.progress?.status == ReadingStatus.READ
                    openedAt = TimeSource.Monotonic.markNow()
                    _uiState.value = ArticleUiState.Success(
                        article = article.toUi(articleKey),
                        isSaved = article.isSaved,
                        textSize = textSize,
                    )
                    if (!alreadyRead) {
                        // Ignore the result: guests get Success(null), errors are not worth a message.
                        constitutionRepository.updateProgress(articleKey, ReadingStatus.IN_PROGRESS, 0)
                    }
                }
                is AsosResult.Error -> _uiState.value = ArticleUiState.Error(result.message)
            }
        }
    }

    private fun markRead() {
        if (alreadyRead || markReadSent || _uiState.value !is ArticleUiState.Success) return
        markReadSent = true
        val seconds = openedAt.elapsedNow().inWholeSeconds.toInt()
        viewModelScope.launch {
            when (val result = constitutionRepository.updateProgress(articleKey, ReadingStatus.READ, seconds)) {
                is AsosResult.Success -> {
                    alreadyRead = result.data != null
                    val points = result.data?.pointsEarned ?: 0
                    if (points > 0) _events.send(ArticleEvent.PointsEarned(points))
                    // Signed out (data == null): allow a retry after signing in.
                    if (result.data == null) markReadSent = false
                }
                is AsosResult.Error -> markReadSent = false
            }
        }
    }

    private fun toggleBookmark() {
        val state = _uiState.value as? ArticleUiState.Success ?: return
        if (state.isBookmarkBusy) return
        viewModelScope.launch {
            if (!authRepository.isSignedIn.first()) {
                _events.send(ArticleEvent.SignInRequired)
                return@launch
            }
            val wasSaved = state.isSaved
            updateSuccess { it.copy(isSaved = !wasSaved, isBookmarkBusy = true) }
            val result = if (wasSaved) {
                bookmarkRepository.remove(BookmarkType.ARTICLE, state.article.id)
            } else {
                bookmarkRepository.saveArticle(state.article.id)
            }
            when (result) {
                is AsosResult.Success -> updateSuccess { it.copy(isBookmarkBusy = false) }
                is AsosResult.Error -> {
                    updateSuccess { it.copy(isSaved = wasSaved, isBookmarkBusy = false) }
                    _events.send(ArticleEvent.Message(result.message))
                }
            }
        }
    }

    private inline fun updateSuccess(transform: (ArticleUiState.Success) -> ArticleUiState.Success) {
        _uiState.update { current -> if (current is ArticleUiState.Success) transform(current) else current }
    }

    companion object {
        internal const val KEY_ARG = "key"
    }
}

// ---------------------------------------------------------------------- mapping to UI model

internal fun Article.toUi(key: String): ArticleUi {
    val display = UzbekFormatters::display
    val heading = if (isPreamble) "Muqaddima" else UzbekFormatters.article(number)
    val readableTitle = title.takeIf { !titleAuto && it.isNotBlank() }?.let(display)
    return ArticleUi(
        key = key,
        id = id,
        number = number,
        isPreamble = isPreamble,
        sectionRoman = section?.roman,
        chapterRoman = chapter?.roman,
        chapterTitle = chapter?.title?.let(display),
        heading = heading,
        title = readableTitle,
        topics = topics.map { TagUi(text = display(it.title), tone = it.tone) },
        readingTime = UzbekFormatters.minutes(readingMinutes),
        effectiveFrom = effectiveFrom?.let { UzbekFormatters.date(it) },
        parts = parts.map { PartUi(number = it.partNo, body = it.body, highlights = it.highlights) },
        sourceLabel = source?.takeIf { it.isNotBlank() },
        summary = summary?.let {
            SummaryUi(
                bullets = it.bullets.map(display),
                reviewedBy = it.reviewedBy?.let(display),
                disclaimer = it.disclaimer?.let(display),
            )
        },
        examples = examples.map {
            ExampleUi(situation = display(it.situation), verdict = display(it.verdict), isLawful = it.isLawful)
        },
        related = related.map { RelatedUi(number = it.number, title = display(it.title)) },
        terms = terms.map { TermChipUi(slug = it.slug, term = display(it.term)) },
        discussion = discussion?.let { DiscussionUi(count = it.count, lastTitle = it.lastTitle?.let(display)) },
        quizId = quiz?.let { q -> q.slug.ifBlank { q.id.toString() } },
        previousNumber = previousNumber,
        nextNumber = nextNumber,
        shareText = buildString {
            append(heading)
            if (readableTitle != null) append(". ").append(readableTitle)
            append("\n\n")
            parts.forEach { append(it.partNo).append(". ").append(it.body).append("\n") }
            append("\nO‘zbekiston Respublikasi Konstitutsiyasi · Asos")
        },
    )
}
