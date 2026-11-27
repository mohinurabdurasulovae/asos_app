package uz.asos.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.asos.core.common.result.AsosResult
import uz.asos.core.common.text.UzbekFormatters
import uz.asos.core.data.repository.AuthRepository
import uz.asos.core.model.constitution.Topic
import uz.asos.feature.home.data.HomeRepository
import uz.asos.feature.home.data.model.ArticleOfDay
import uz.asos.feature.home.data.model.CaseOfWeek
import uz.asos.feature.home.data.model.CaseStatus
import uz.asos.feature.home.data.model.ContinueLearning
import uz.asos.feature.home.data.model.DailyAnswer
import uz.asos.feature.home.data.model.DailyQuestion
import uz.asos.feature.home.data.model.HomeFeed
import uz.asos.feature.home.data.model.PostHighlight
import uz.asos.feature.home.data.model.PostStatus
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _events = Channel<HomeEvent>(Channel.BUFFERED)
    val events: Flow<HomeEvent> = _events.receiveAsFlow()

    private var hasLoaded = false
    private var loadJob: Job? = null
    private var dailyQuestion: DailyQuestion? = null
    private var streakDays = 0

    init {
        observeSession()
        load(showSkeleton = true)
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OnRetry -> load(showSkeleton = true)
            HomeAction.OnResume -> if (hasLoaded) load(showSkeleton = false)
            HomeAction.OnSaveArticleClick -> saveArticleOfDay()
            is HomeAction.OnQuizOptionClick -> answerDailyQuestion(action.optionId)
            HomeAction.OnNotificationsClick -> send(HomeEvent.NotificationsComingSoon)
            HomeAction.OnProfileClick,
            HomeAction.OnSearchClick,
            is HomeAction.OnArticleClick,
            HomeAction.OnAllTopicsClick,
            is HomeAction.OnTopicClick,
            HomeAction.OnContinueAllClick,
            is HomeAction.OnCaseClick,
            HomeAction.OnLegalHelpClick,
            HomeAction.OnDictionaryClick,
            HomeAction.OnAssistantClick,
            HomeAction.OnAllPostsClick,
            is HomeAction.OnPostClick,
            -> Unit
        }
    }

    private fun observeSession() {
        viewModelScope.launch {
            authRepository.isSignedIn.collect { signedIn ->
                val changed = _uiState.value.isSignedIn != signedIn
                _uiState.update { it.copy(isSignedIn = signedIn) }
                if (changed && hasLoaded) load(showSkeleton = false)
            }
        }
    }

    private fun load(showSkeleton: Boolean) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            if (showSkeleton) _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = homeRepository.getHomeFeed()) {
                is AsosResult.Success -> {
                    hasLoaded = true
                    applyFeed(result.data)
                }
                is AsosResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = if (hasLoaded) null else result.message)
                }
            }
        }
    }

    private fun applyFeed(feed: HomeFeed) {
        dailyQuestion = feed.dailyQuestion
        streakDays = feed.user?.streakDays ?: 0
        val articleChanged = _uiState.value.articleOfDay?.articleKey != feed.articleOfDay?.number?.toString()
        _uiState.update { state ->
            state.copy(
                isLoading = false,
                errorMessage = null,
                greeting = feed.greeting,
                userName = feed.user?.name?.substringBefore(' ')?.takeIf { it.isNotBlank() },
                initials = feed.user?.initials?.takeIf { it.isNotBlank() },
                hasUnreadNotifications = feed.unreadNotifications > 0,
                articleOfDay = feed.articleOfDay?.toUi(),
                isArticleSaved = state.isArticleSaved && !articleChanged,
                topics = feed.topics.take(TOPIC_TILES).map { it.toUi() },
                continueReading = feed.continueLearning?.toUi(),
                caseOfWeek = feed.caseOfWeek?.toUi(),
                dailyQuiz = feed.dailyQuestion?.toUi(streakDays, submittingOptionId = null),
                help = HelpUi(
                    lawyersOnlineLabel = feed.lawyersOnline.takeIf { it > 0 }?.let { "$it advokat onlayn" },
                    termOfDayLabel = feed.termOfDay?.let { "Kun atamasi: ${UzbekFormatters.display(it.term)}" },
                ),
                posts = feed.posts.map { it.toUi(Instant.now()) },
            )
        }
    }

    private fun saveArticleOfDay() {
        val article = _uiState.value.articleOfDay ?: return
        if (_uiState.value.isArticleSaved) return
        if (!_uiState.value.isSignedIn) {
            send(HomeEvent.SignInRequired)
            return
        }
        _uiState.update { it.copy(isArticleSaved = true) }
        viewModelScope.launch {
            when (val result = homeRepository.saveArticleByNumber(article.articleKey.toInt())) {
                is AsosResult.Success -> _events.send(HomeEvent.ArticleSaved)
                is AsosResult.Error -> {
                    _uiState.update { it.copy(isArticleSaved = false) }
                    _events.send(HomeEvent.Message(result.message))
                }
            }
        }
    }

    private fun answerDailyQuestion(optionId: Int) {
        val question = dailyQuestion ?: return
        val quiz = _uiState.value.dailyQuiz ?: return
        if (quiz.isLocked) return
        if (!_uiState.value.isSignedIn) {
            send(HomeEvent.SignInRequired)
            return
        }
        _uiState.update { it.copy(dailyQuiz = question.toUi(streakDays, submittingOptionId = optionId)) }
        viewModelScope.launch {
            when (val result = homeRepository.answerDailyQuestion(optionId)) {
                is AsosResult.Success -> {
                    val answered = question.withAnswer(optionId, result.data)
                    dailyQuestion = answered
                    streakDays = result.data.streakDays
                    _uiState.update { it.copy(dailyQuiz = answered.toUi(streakDays, submittingOptionId = null)) }
                    if (result.data.pointsEarned > 0) _events.send(HomeEvent.PointsEarned(result.data.pointsEarned))
                }
                is AsosResult.Error -> {
                    _uiState.update { it.copy(dailyQuiz = question.toUi(streakDays, submittingOptionId = null)) }
                    _events.send(HomeEvent.Message(result.message))
                }
            }
        }
    }

    private fun send(event: HomeEvent) {
        viewModelScope.launch { _events.send(event) }
    }

    companion object {
        const val TOPIC_TILES = 6
        const val CASE_STEPS = 3
    }
}

private fun ArticleOfDay.toUi() = ArticleOfDayUi(
    articleKey = number.toString(),
    watermark = number.toString(),
    meta = listOfNotNull(
        chapterRoman?.let { UzbekFormatters.chapterLabel(it) },
        chapterTitle?.let { UzbekFormatters.display(it) },
    ).joinToString(" · "),
    quote = "“$quote”",
    reference = if (partNo > 0) "${UzbekFormatters.article(number)}, $partNo-qism" else UzbekFormatters.article(number),
)

private fun Topic.toUi() = TopicTileUi(
    slug = slug,
    title = UzbekFormatters.display(title),
    countLabel = UzbekFormatters.articleCount(articleCount),
    icon = icon,
    tone = tone,
)

private fun ContinueLearning.toUi(): ContinueReadingUi {
    val nextNumber = nextArticleNumber
    return ContinueReadingUi(
        articleKey = (nextNumber ?: lastArticleNumber).toString(),
        title = chapterTitle?.let { UzbekFormatters.display(it) } ?: UzbekFormatters.article(lastArticleNumber),
        meta = chapterRoman?.let { UzbekFormatters.chapterLabel(it) }.orEmpty(),
        progressLabel = "$readCount / $articleCount modda o‘qildi",
        percentLabel = "$percent%",
        progress = percent / 100f,
        nextLabel = if (nextNumber != null) {
            listOfNotNull("Keyingi: ${UzbekFormatters.article(nextNumber)}", nextArticleTitle?.let { UzbekFormatters.display(it) })
                .joinToString(" · ") + " →"
        } else {
            "Davom etish: ${UzbekFormatters.article(lastArticleNumber)} →"
        },
    )
}

private fun CaseOfWeek.toUi() = CaseOfWeekUi(
    caseId = number.toString(),
    title = UzbekFormatters.display(title),
    progressLabel = when {
        status == CaseStatus.SOLVED -> "Yechildi"
        step != null -> "$step / ${HomeViewModel.CASE_STEPS} bosqich"
        else -> "~${UzbekFormatters.minutes(estMinutes)}"
    },
    progress = when (status) {
        CaseStatus.SOLVED -> 1f
        else -> (percent ?: 0) / 100f
    },
)

private fun DailyQuestion.withAnswer(optionId: Int, answer: DailyAnswer): DailyQuestion = copy(
    answeredOptionId = optionId,
    isCorrect = answer.isCorrect,
    explanation = answer.explanation,
    options = options.map { option ->
        val isCorrect = when {
            answer.correctOptionId != null -> option.id == answer.correctOptionId
            option.id == optionId -> answer.isCorrect
            else -> null
        }
        option.copy(isCorrect = isCorrect)
    },
)

private fun DailyQuestion.toUi(streakDays: Int, submittingOptionId: Int?) = DailyQuizUi(
    meta = listOfNotNull(topic?.let { UzbekFormatters.display(it) }, difficultyLabel(difficulty)).joinToString(" · "),
    streakLabel = streakDays.takeIf { it > 0 }?.let { "$it kun ketma-ket" },
    question = UzbekFormatters.display(body),
    options = options.map { option ->
        val state = when {
            isAnswered && option.isCorrect == true -> QuizOptionState.CORRECT
            isAnswered && option.id == answeredOptionId -> QuizOptionState.WRONG
            option.id == submittingOptionId -> QuizOptionState.SELECTED
            else -> QuizOptionState.DEFAULT
        }
        QuizOptionUi(id = option.id, text = UzbekFormatters.display(option.body), state = state)
    },
    footer = when (isCorrect) {
        true -> "To‘g‘ri javob! +$points ball"
        false -> "Javob noto‘g‘ri. Ertaga yana urinib ko‘ring."
        null -> "To‘g‘ri javob uchun +$points ball"
    },
    explanation = explanation?.takeIf { isAnswered && it.isNotBlank() }?.let { UzbekFormatters.display(it) },
    isAnswered = isAnswered,
    isSubmitting = submittingOptionId != null,
)

private fun difficultyLabel(value: String): String? = when (value.lowercase()) {
    "boshlangich" -> "Boshlang‘ich"
    "orta" -> "O‘rta"
    "murakkab" -> "Murakkab"
    else -> null
}

private val ApiTimeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

private fun PostHighlight.toUi(now: Instant) = PostUi(
    id = id,
    communityTitle = communityTitle?.let { UzbekFormatters.display(it) },
    communityTone = communityTone,
    meta = listOfNotNull(authorName.takeIf { it.isNotBlank() }, relativeTime(createdAt, now)).joinToString(" · "),
    title = UzbekFormatters.display(title),
    upvotes = UzbekFormatters.groupThousands(upvotes),
    comments = UzbekFormatters.groupThousands(commentCount),
    badge = if (status == PostStatus.ANSWERED) PostBadge.LAWYER_ANSWERED else PostBadge.IN_DISCUSSION,
)

internal fun relativeTime(apiTime: String, now: Instant): String? {
    val created = runCatching { LocalDateTime.parse(apiTime, ApiTimeFormat).toInstant(ZoneOffset.UTC) }.getOrNull()
        ?: return null
    val minutes = Duration.between(created, now).toMinutes().coerceAtLeast(0)
    return when {
        minutes < 1 -> "hozirgina"
        minutes < 60 -> "$minutes daqiqa oldin"
        minutes < 60 * 24 -> "${minutes / 60} soat oldin"
        minutes < 60 * 24 * 30 -> "${minutes / (60 * 24)} kun oldin"
        else -> UzbekFormatters.date(apiTime)
    }
}
