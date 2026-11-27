/*
 * UserModels.kt
 * Module: :core:model   ·   Work package: WP-02
 *
 * Domain models shared by every layer.
 *
 * Profil (WP-11): UserProfile and everything GET /v1/me/profile returns.
 */
package uz.asos.core.model.user

/** Kinds accepted by /v1/me/bookmarks. [apiValue] is the value the API expects. */
enum class BookmarkType(val apiValue: String) {
    ARTICLE("article"),
    TERM("term"),
    POST("post"),
    CASE("case"),
}

/** Reader text size. [apiValue] matches PATCH /v1/me `textSize`. */
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

/** The signed-in user, as GET /v1/me/profile describes them. */
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

/** "3-daraja · Bilimdon", and how many points the next level needs. */
data class UserLevel(
    val number: Int,
    val title: String,
    val minPoints: Int,
    /** Points at which the next level starts; null on the last one. */
    val nextAt: Int?,
)

/** The three numbers across the top of Profil. */
data class UserStats(
    val points: Int,
    val quizzesDone: Int,
    val casesSolved: Int,
    val articlesRead: Int,
    val percent: Int,
    val streakDays: Int,
)

/** The Du–Ya columns in the "Bu hafta" card. */
data class WeekActivity(
    val days: List<ActivityDay>,
    val totalMinutes: Int,
    val activeDays: Int,
)

data class ActivityDay(
    /** "Du", "Se"… */
    val label: String,
    val date: String,
    val minutes: Int,
    val lessons: Int,
    val isToday: Boolean,
    val isFuture: Boolean,
)

/** Daily goal in lessons. */
data class DailyGoal(
    val goal: Int,
    val done: Int,
    val completed: Boolean,
)

/** What an achievement is counted against. */
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
    /** Icon name from the API, used to pick an AsosIcons entry. */
    val icon: String?,
    val tone: String?,
    val metric: AchievementMetric,
    val threshold: Int,
    val progress: Int,
    val earned: Boolean,
)

/** How many of each kind the user saved, for the "Saqlanganlar" rows. */
data class BookmarkCounts(
    val articles: Int,
    val terms: Int,
    val posts: Int,
    val cases: Int,
)

/** Everything GET /v1/me/profile returns: the whole Profil screen in one call. */
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
