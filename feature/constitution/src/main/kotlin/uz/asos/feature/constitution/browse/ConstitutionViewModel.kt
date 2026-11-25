/*
 * ConstitutionViewModel.kt
 * Module: :feature:constitution   ·   Work package: WP-05
 * Screen: Konstitutsiya (design artboard: Konstitutsiya.dc.html)
 *
 * Holds StateFlow<ConstitutionUiState> and handles ConstitutionAction. No Android UI types.
 *
 * API used (through repositories, see docs/api/openapi.yaml):
 *  - GET /v1/constitution        section / chapter tree (+ progress with a token)
 *  - GET /v1/topics              topic chips and the Mavzular tab
 *  - GET /v1/chapters/{roman}    articles of an opened chapter
 *  - GET /v1/articles            search, topic filter, Saqlangan (status=saved), section VI
 *  - GET /v1/me/progress         "Oxirgi o‘qilgan" card (signed in only)
 */
package uz.asos.feature.constitution.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.asos.core.common.result.AsosResult
import uz.asos.core.common.result.getOrNull
import uz.asos.core.common.text.UzbekFormatters
import uz.asos.core.data.repository.AuthRepository
import uz.asos.core.data.repository.ConstitutionRepository
import uz.asos.core.model.constitution.ArticleBrief
import uz.asos.core.model.constitution.ArticleQuery
import uz.asos.core.model.constitution.Chapter
import uz.asos.core.model.constitution.ConstitutionTree
import uz.asos.core.model.constitution.ContinueReading
import uz.asos.core.model.constitution.ReadingStatus
import uz.asos.core.model.constitution.Section
import uz.asos.core.model.constitution.Topic
import javax.inject.Inject

@HiltViewModel
class ConstitutionViewModel @Inject constructor(
    private val constitutionRepository: ConstitutionRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConstitutionUiState())
    val uiState: StateFlow<ConstitutionUiState> = _uiState.asStateFlow()

    private var hasLoaded = false
    private var loadJob: Job? = null
    private var listJob: Job? = null
    private var searchJob: Job? = null
    private val groupJobs = mutableMapOf<String, Job>()

    init {
        load(showSkeleton = true)
        observeSession()
    }

    fun onAction(action: ConstitutionAction) {
        when (action) {
            is ConstitutionAction.OnQueryChange -> onQueryChange(action.query)
            is ConstitutionAction.OnTabSelect -> {
                _uiState.update { it.copy(selectedTab = action.tab) }
                refreshList()
            }
            is ConstitutionAction.OnTopicSelect -> {
                _uiState.update { it.copy(selectedTopicSlug = action.slug) }
                refreshList()
            }
            is ConstitutionAction.OnTopicOpen -> {
                _uiState.update {
                    it.copy(selectedTab = ConstitutionTab.STRUCTURE, selectedTopicSlug = action.slug)
                }
                refreshList()
            }
            is ConstitutionAction.OnSectionToggle -> onSectionToggle(action.roman)
            is ConstitutionAction.OnChapterToggle -> onChapterToggle(action.roman)
            is ConstitutionAction.OnChapterRetry -> loadChapter(action.roman)
            ConstitutionAction.OnSavedClick -> onAction(ConstitutionAction.OnTabSelect(ConstitutionTab.SAVED))
            ConstitutionAction.OnRetry -> load(showSkeleton = true)
            ConstitutionAction.OnListRetry -> refreshList()
            ConstitutionAction.OnResume -> if (hasLoaded) load(showSkeleton = false)
            // Navigation is handled by ConstitutionRoute.
            is ConstitutionAction.OnArticleClick, ConstitutionAction.OnSignInClick -> Unit
        }
    }

    // ------------------------------------------------------------------ loading

    private fun observeSession() {
        viewModelScope.launch {
            authRepository.isSignedIn.collect { signedIn ->
                val changed = _uiState.value.isSignedIn != signedIn
                _uiState.update { it.copy(isSignedIn = signedIn) }
                if (changed && hasLoaded) {
                    load(showSkeleton = false)
                    refreshList()
                }
            }
        }
    }

    private fun load(showSkeleton: Boolean) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            if (showSkeleton) _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val tree = async { constitutionRepository.getConstitution() }
            val topics = async { constitutionRepository.getTopics() }
            val overview = async { constitutionRepository.getReadingOverview() }

            when (val result = tree.await()) {
                is AsosResult.Success -> {
                    hasLoaded = true
                    val continueReading = overview.await().getOrNull()?.continueReading
                    val loadedTopics = topics.await().getOrNull()
                    applyTree(result.data, continueReading, loadedTopics)
                    // Opened chapters show reading progress, so refresh them too.
                    _uiState.value.expandedChapters.forEach { loadChapter(it, quiet = true) }
                }
                is AsosResult.Error -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = if (it.sections.isEmpty()) result.message else null,
                    )
                }
            }
        }
    }

    private fun applyTree(tree: ConstitutionTree, continueReading: ContinueReading?, topics: List<Topic>?) {
        val sections = tree.sections.map { it.toUi() }
        _uiState.update { state ->
            state.copy(
                isLoading = false,
                errorMessage = null,
                subtitle = with(tree.info) {
                    "$edition-yil tahriri · ${this.sections} bo‘lim · $chapters bob · $articles modda"
                },
                sections = sections,
                topics = topics?.map { it.toUi() } ?: state.topics,
                continueReading = continueReading?.toUi(),
                expandedSections = state.expandedSections.ifEmpty {
                    defaultExpandedSections(tree.sections, continueReading?.chapterRoman)
                },
            )
        }
    }

    /** Open the section the user is reading; otherwise section II (rights), as in the design. */
    private fun defaultExpandedSections(sections: List<Section>, chapterRoman: String?): Set<String> {
        val reading = chapterRoman?.let { roman -> sections.firstOrNull { s -> s.chapters.any { it.roman == roman } } }
        val section = reading ?: sections.firstOrNull { it.roman == "II" } ?: sections.firstOrNull()
        return setOfNotNull(section?.roman)
    }

    // ------------------------------------------------------------------ table of contents

    private fun onSectionToggle(roman: String) {
        val expanding = roman !in _uiState.value.expandedSections
        _uiState.update {
            it.copy(expandedSections = if (expanding) it.expandedSections + roman else it.expandedSections - roman)
        }
        val section = _uiState.value.sections.firstOrNull { it.roman == roman } ?: return
        // Section VI has no chapters: load its articles when it opens.
        if (expanding && section.directArticlesQuery != null && sectionKey(roman) !in _uiState.value.chapterArticles) {
            loadGroup(sectionKey(roman), ArticleQuery(section = section.directArticlesQuery))
        }
    }

    private fun onChapterToggle(roman: String) {
        val expanding = roman !in _uiState.value.expandedChapters
        _uiState.update {
            it.copy(expandedChapters = if (expanding) it.expandedChapters + roman else it.expandedChapters - roman)
        }
        val current = _uiState.value.chapterArticles[roman]
        if (expanding && (current == null || current is ArticleListState.Failed)) loadChapter(roman)
    }

    private fun loadChapter(roman: String, quiet: Boolean = false) {
        if (roman.startsWith(SECTION_PREFIX)) {
            val sectionRoman = roman.removePrefix(SECTION_PREFIX)
            loadGroup(roman, ArticleQuery(section = sectionRoman), quiet)
            return
        }
        groupJobs[roman]?.cancel()
        groupJobs[roman] = viewModelScope.launch {
            if (!quiet) setGroup(roman, ArticleListState.Loading)
            when (val result = constitutionRepository.getChapterArticles(roman)) {
                is AsosResult.Success -> setGroup(roman, ArticleListState.Loaded(result.data.map { it.toRow() }))
                is AsosResult.Error -> if (!quiet) setGroup(roman, ArticleListState.Failed(result.message))
            }
        }
    }

    private fun loadGroup(key: String, query: ArticleQuery, quiet: Boolean = false) {
        groupJobs[key]?.cancel()
        groupJobs[key] = viewModelScope.launch {
            if (!quiet) setGroup(key, ArticleListState.Loading)
            when (val result = constitutionRepository.getArticles(query)) {
                is AsosResult.Success -> setGroup(key, ArticleListState.Loaded(result.data.map { it.toRow() }))
                is AsosResult.Error -> if (!quiet) setGroup(key, ArticleListState.Failed(result.message))
            }
        }
    }

    private fun setGroup(key: String, state: ArticleListState) {
        _uiState.update { it.copy(chapterArticles = it.chapterArticles + (key to state)) }
    }

    // ------------------------------------------------------------------ search / topic / saved list

    private fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
        searchJob?.cancel()
        if (query.isBlank()) {
            refreshList()
        } else {
            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_MS)
                refreshList()
            }
        }
    }

    private fun refreshList() {
        listJob?.cancel()
        val state = _uiState.value
        val query: ArticleQuery? = when {
            state.isSearching -> ArticleQuery(
                text = state.query,
                savedOnly = state.selectedTab == ConstitutionTab.SAVED && state.isSignedIn,
            )
            state.selectedTab == ConstitutionTab.SAVED -> {
                if (!state.isSignedIn) {
                    _uiState.update { it.copy(articleList = ArticleListState.NeedsSignIn) }
                    return
                }
                ArticleQuery(savedOnly = true)
            }
            state.selectedTab == ConstitutionTab.STRUCTURE && state.selectedTopicSlug != null ->
                ArticleQuery(topic = state.selectedTopicSlug)
            else -> null
        }
        if (query == null) {
            _uiState.update { it.copy(articleList = ArticleListState.Hidden) }
            return
        }
        _uiState.update { it.copy(articleList = ArticleListState.Loading) }
        listJob = viewModelScope.launch {
            val list = when (val result = constitutionRepository.getArticles(query)) {
                is AsosResult.Success -> ArticleListState.Loaded(result.data.map { it.toRow() })
                is AsosResult.Error -> ArticleListState.Failed(result.message)
            }
            _uiState.update { it.copy(articleList = list) }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_MS = 300L
        internal const val SECTION_PREFIX = "section-"
        internal fun sectionKey(roman: String) = "$SECTION_PREFIX$roman"
    }
}

// ---------------------------------------------------------------------- mapping to UI models

private fun Section.toUi() = SectionUi(
    roman = roman,
    overline = UzbekFormatters.sectionLabel(roman),
    title = UzbekFormatters.display(title),
    meta = if (chapterCount > 0) {
        "$chapterCount bob · ${UzbekFormatters.articleRange(articleFrom, articleTo)}"
    } else {
        UzbekFormatters.articleRange(articleFrom, articleTo)
    },
    chapters = chapters.map { it.toUi() },
    directArticlesQuery = if (chapters.isEmpty()) roman else null,
)

private fun Chapter.toUi(): ChapterUi {
    val state = status.toReadState()
    val range = UzbekFormatters.articleRange(articleFrom, articleTo)
    return ChapterUi(
        roman = roman,
        title = UzbekFormatters.display(title),
        meta = if (state == ReadState.IN_PROGRESS) "$range · $readCount/$articleCount o‘qildi" else range,
        state = state,
        percent = percent,
    )
}

private fun ContinueReading.toUi() = ContinueReadingUi(
    articleKey = articleNumber.toString(),
    numberLabel = articleNumber.toString(),
    title = UzbekFormatters.display(articleTitle),
    meta = listOfNotNull(
        chapterRoman?.let { UzbekFormatters.chapterLabel(it) },
        if (minutesLeft > 0) UzbekFormatters.minutesLeft(minutesLeft) else UzbekFormatters.minutes(readingMinutes),
    ).joinToString(" · "),
)

private fun Topic.toUi() = TopicUi(
    slug = slug,
    title = UzbekFormatters.display(title),
    countLabel = UzbekFormatters.articleCount(articleCount),
    tone = tone,
)

internal fun ArticleBrief.toRow() = ArticleRowUi(
    key = if (isPreamble) PREAMBLE_KEY else number.toString(),
    numberLabel = if (isPreamble) "§" else number.toString(),
    title = UzbekFormatters.display(
        if (titleAuto) excerpt?.takeIf { it.isNotBlank() } ?: title else title,
    ),
    meta = listOfNotNull(
        chapterRoman?.let { UzbekFormatters.chapterLabel(it) },
        UzbekFormatters.minutes(readingMinutes),
    ).joinToString(" · "),
    state = progress?.status.toReadState(),
)

private fun ReadingStatus?.toReadState(): ReadState = when (this) {
    ReadingStatus.READ -> ReadState.READ
    ReadingStatus.IN_PROGRESS -> ReadState.IN_PROGRESS
    ReadingStatus.NOT_STARTED, null -> ReadState.NOT_STARTED
}

internal const val PREAMBLE_KEY = "muqaddima"
