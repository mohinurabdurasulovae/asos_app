package uz.asos.feature.home

data class HomeUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isSignedIn: Boolean = false,
    val greeting: String = "",
    val userName: String? = null,
    val initials: String? = null,
    val hasUnreadNotifications: Boolean = false,
    val articleOfDay: ArticleOfDayUi? = null,
    val isArticleSaved: Boolean = false,
    val topics: List<TopicTileUi> = emptyList(),
    val continueReading: ContinueReadingUi? = null,
    val caseOfWeek: CaseOfWeekUi? = null,
    val dailyQuiz: DailyQuizUi? = null,
    val help: HelpUi = HelpUi(),
    val posts: List<PostUi> = emptyList(),
) {
    val hasContinueSection: Boolean get() = continueReading != null || caseOfWeek != null
}

data class ArticleOfDayUi(
    val articleKey: String,
    val watermark: String,
    val meta: String,
    val quote: String,
    val reference: String,
)

data class TopicTileUi(
    val slug: String,
    val title: String,
    val countLabel: String,
    val icon: String?,
    val tone: String?,
)

data class ContinueReadingUi(
    val articleKey: String,
    val title: String,
    val meta: String,
    val progressLabel: String,
    val percentLabel: String,
    val progress: Float,
    val nextLabel: String,
)

data class CaseOfWeekUi(
    val caseId: String,
    val title: String,
    val progressLabel: String,
    val progress: Float,
)

enum class QuizOptionState { DEFAULT, SELECTED, CORRECT, WRONG }

data class QuizOptionUi(
    val id: Int,
    val text: String,
    val state: QuizOptionState,
)

data class DailyQuizUi(
    val meta: String,
    val streakLabel: String?,
    val question: String,
    val options: List<QuizOptionUi>,
    val footer: String,
    val explanation: String?,
    val isAnswered: Boolean,
    val isSubmitting: Boolean,
) {
    val isLocked: Boolean get() = isAnswered || isSubmitting
}

data class HelpUi(
    val lawyersOnlineLabel: String? = null,
    val termOfDayLabel: String? = null,
)

enum class PostBadge { LAWYER_ANSWERED, IN_DISCUSSION }

data class PostUi(
    val id: Int,
    val communityTitle: String?,
    val communityTone: String?,
    val meta: String,
    val title: String,
    val upvotes: String,
    val comments: String,
    val badge: PostBadge,
)

sealed interface HomeEvent {
    data object SignInRequired : HomeEvent
    data object ArticleSaved : HomeEvent
    data object NotificationsComingSoon : HomeEvent
    data class PointsEarned(val points: Int) : HomeEvent
    data class Message(val text: String) : HomeEvent
}
