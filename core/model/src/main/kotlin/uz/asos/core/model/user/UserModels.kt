package uz.asos.core.model.user

enum class BookmarkType(val apiValue: String) {
    ARTICLE("article"),
    TERM("term"),
    POST("post"),
    CASE("case"),
}

enum class ReaderTextSize(val apiValue: String) {
    SMALL("small"),
    MEDIUM("medium"),
    LARGE("large"),
    XLARGE("xlarge");

    companion object {
        fun fromApi(value: String?): ReaderTextSize =
            entries.firstOrNull { it.apiValue == value } ?: MEDIUM
    }
}

data class User(
    val id: Int,
    val name: String,
    val username: String?,
    val initials: String,
    val avatarUrl: String?,
    val city: String?,
    val points: Int,
    val level: UserLevel,
    val streakDays: Int,
    val locale: String?,
    val textSize: ReaderTextSize,
    val notifyDaily: Boolean,
)

data class UserLevel(
    val number: Int,
    val title: String,
    val minPoints: Int,
    val nextAt: Int?,
)

data class UserStats(
    val points: Int,
    val quizzesDone: Int,
    val casesSolved: Int,
    val articlesRead: Int,
    val percent: Int,
    val streakDays: Int,
)

data class WeekActivity(
    val days: List<ActivityDay>,
    val totalMinutes: Int,
    val activeDays: Int,
)

data class ActivityDay(
    val label: String,
    val date: String,
    val minutes: Int,
    val lessons: Int,
    val isToday: Boolean,
    val isFuture: Boolean,
)

data class DailyGoal(
    val goal: Int,
    val done: Int,
    val completed: Boolean,
)

enum class AchievementMetric(val apiValue: String) {
    ARTICLES_READ("articles_read"),
    QUIZZES_DONE("quizzes_done"),
    CASES_SOLVED("cases_solved"),
    STREAK_DAYS("streak_days"),
    POINTS("points"),
    TERMS_SAVED("terms_saved"),
    POSTS_WRITTEN("posts_written"),
    UNKNOWN("");

    companion object {
        fun fromApi(value: String?): AchievementMetric =
            entries.firstOrNull { it.apiValue == value } ?: UNKNOWN
    }
}

data class Achievement(
    val id: Int,
    val slug: String,
    val title: String,
    val hint: String?,
    val icon: String?,
    val tone: String?,
    val metric: AchievementMetric,
    val threshold: Int,
    val progress: Int,
    val earned: Boolean,
)

data class BookmarkCounts(
    val articles: Int,
    val terms: Int,
    val posts: Int,
    val cases: Int,
)

data class UserProfile(
    val user: User,
    val stats: UserStats,
    val week: WeekActivity,
    val dailyGoal: DailyGoal?,
    val achievementsEarned: Int,
    val achievementsTotal: Int,
    val achievements: List<Achievement>,
    val bookmarks: BookmarkCounts,
)
