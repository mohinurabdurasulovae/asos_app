package uz.asos.feature.home.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.asos.core.network.dto.ArticleRefDto
import uz.asos.core.network.dto.ReadingOverviewDto
import uz.asos.core.network.dto.TopicDto
@Serializable
internal data class HomeResponseDto(
    val ok: Boolean = true,
    val greeting: String = "",
    val articleOfDay: ArticleOfDayDto? = null,
    val termOfDay: TermOfDayDto? = null,
    val topics: List<TopicDto> = emptyList(),
    val caseOfWeek: HomeCaseDto? = null,
    val dailyQuestion: DailyQuestionDto? = null,
    val help: HomeHelpDto = HomeHelpDto(),
    val community: HomeCommunityDto = HomeCommunityDto(),
    val user: HomeUserDto? = null,
    @SerialName("continue")
    val continueOverview: ReadingOverviewDto? = null,
    val unreadNotifications: Int = 0,
)

@Serializable
internal data class ArticleOfDayDto(
    val number: Int = 0,
    val title: String = "",
    val slug: String = "",
    val quote: String = "",
    val partNo: Int = 0,
    val chapter: ChapterNameDto? = null,
)

@Serializable
internal data class ChapterNameDto(
    val roman: String = "",
    val title: String = "",
)

@Serializable
internal data class TermOfDayDto(
    val term: String = "",
    val slug: String = "",
    val shortDef: String? = null,
)

@Serializable
internal data class HomeCaseDto(
    val id: Int = 0,
    val number: Int = 0,
    val slug: String = "",
    val title: String = "",
    val teaser: String = "",
    val difficulty: Int = 1,
    val estMinutes: Int = 0,
    val myStatus: String = "new",
    val myStep: Int? = null,
    val myPercent: Int? = null,
)

@Serializable
internal data class HomeHelpDto(
    val lawyersOnline: Int = 0,
)

@Serializable
internal data class HomeCommunityDto(
    val items: List<HomePostDto> = emptyList(),
)

@Serializable
internal data class HomePostDto(
    val id: Int = 0,
    val title: String = "",
    val upvotes: Int = 0,
    val commentCount: Int = 0,
    val status: String = "open",
    val createdAt: String = "",
    val author: HomeAuthorDto = HomeAuthorDto(),
    val community: HomeToneTagDto? = null,
)

@Serializable
internal data class HomeAuthorDto(
    val name: String = "",
    val isAnonymous: Boolean = false,
)

@Serializable
internal data class HomeToneTagDto(
    val slug: String = "",
    val title: String = "",
    val tone: String? = null,
)

@Serializable
internal data class DailyQuestionDto(
    val day: String = "",
    val questionId: Int = 0,
    val quizId: Int = 0,
    val body: String = "",
    val points: Int = 0,
    val difficulty: String = "",
    val topic: String? = null,
    val options: List<QuizOptionDto> = emptyList(),
    val answered: Boolean = false,
    val myOptionId: Int? = null,
    val isCorrect: Boolean? = null,
    val explanation: String? = null,
    val article: ArticleRefDto? = null,
)

@Serializable
internal data class QuizOptionDto(
    val id: Int = 0,
    val label: String = "",
    val body: String = "",
    val isCorrect: Boolean? = null,
)

@Serializable
internal data class DailyAnswerBodyDto(
    val optionId: Int,
    val seconds: Int? = null,
)

@Serializable
internal data class DailyAnswerResponseDto(
    val ok: Boolean = true,
    val isCorrect: Boolean = false,
    val correctOptionId: Int? = null,
    val pointsEarned: Int = 0,
    val points: Int = 0,
    val streakDays: Int = 0,
    val explanation: String? = null,
    val article: ArticleRefDto? = null,
)

@Serializable
internal data class HomeUserDto(
    val id: Int = 0,
    val name: String = "",
    val initials: String = "",
    val avatarUrl: String? = null,
    val points: Int = 0,
    val streakDays: Int = 0,
)
