package uz.asos.feature.home.data.model

import uz.asos.core.model.constitution.Topic

data class HomeFeed(
    val greeting: String,
    val user: HomeUser?,
    val unreadNotifications: Int,
    val articleOfDay: ArticleOfDay?,
    val termOfDay: TermOfDay?,
    val topics: List<Topic>,
    val continueLearning: ContinueLearning?,
    val caseOfWeek: CaseOfWeek?,
    val dailyQuestion: DailyQuestion?,
    val lawyersOnline: Int,
    val posts: List<PostHighlight>,
)

data class HomeUser(
    val name: String,
    val initials: String,
    val streakDays: Int,
)

data class ArticleOfDay(
    val number: Int,
    val title: String,
    val quote: String,
    val partNo: Int,
    val chapterRoman: String?,
    val chapterTitle: String?,
)

data class TermOfDay(
    val term: String,
    val slug: String,
)

data class ContinueLearning(
    val lastArticleNumber: Int,
    val chapterRoman: String?,
    val chapterTitle: String?,
    val readCount: Int,
    val articleCount: Int,
    val percent: Int,
    val nextArticleNumber: Int?,
    val nextArticleTitle: String?,
)

enum class CaseStatus { NEW, IN_PROGRESS, SOLVED }

data class CaseOfWeek(
    val number: Int,
    val title: String,
    val status: CaseStatus,
    val step: Int?,
    val percent: Int?,
    val estMinutes: Int,
)

data class DailyQuestion(
    val questionId: Int,
    val body: String,
    val points: Int,
    val difficulty: String,
    val topic: String?,
    val options: List<DailyOption>,
    val answeredOptionId: Int?,
    val isCorrect: Boolean?,
    val explanation: String?,
) {
    val isAnswered: Boolean get() = answeredOptionId != null
}

data class DailyOption(
    val id: Int,
    val label: String,
    val body: String,
    val isCorrect: Boolean?,
)

data class DailyAnswer(
    val isCorrect: Boolean,
    val correctOptionId: Int?,
    val pointsEarned: Int,
    val streakDays: Int,
    val explanation: String?,
)

enum class PostStatus { OPEN, AWAITING_LAWYER, ANSWERED }

data class PostHighlight(
    val id: Int,
    val title: String,
    val communityTitle: String?,
    val communityTone: String?,
    val authorName: String,
    val createdAt: String,
    val upvotes: Int,
    val commentCount: Int,
    val status: PostStatus,
)
