package uz.asos.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class BookmarkRequestDto(
    val kind: String,
    val targetId: Int? = null,
    val articleNumber: Int? = null,
    val termSlug: String? = null,
    val caseNumber: Int? = null,
)

@Serializable
data class BookmarkResponseDto(
    val ok: Boolean = true,
    val saved: Boolean = false,
    val kind: String? = null,
    val targetId: Int? = null,
)

@Serializable
data class ProfileResponseDto(
    val ok: Boolean = true,
    val user: MeDto,
    val stats: ProfileStatsDto = ProfileStatsDto(),
    val week: WeekActivityDto = WeekActivityDto(),
    val dailyGoal: DailyGoalDto? = null,
    val achievements: AchievementsDto = AchievementsDto(),
    val bookmarks: BookmarkCountsDto = BookmarkCountsDto(),
)

@Serializable
data class MeDto(
    val id: Int = 0,
    val name: String = "",
    val username: String? = null,
    val initials: String = "",
    val avatarUrl: String? = null,
    val city: String? = null,
    val role: String? = null,
    val points: Int = 0,
    val level: LevelDto = LevelDto(),
    val streakDays: Int = 0,
    val phone: String? = null,
    val email: String? = null,
    val dailyGoal: Int = 0,
    val locale: String? = null,
    val textSize: String? = null,
    val notifyDaily: Boolean = false,
    val createdAt: String? = null,
)

@Serializable
data class LevelDto(
    val number: Int = 1,
    val title: String = "",
    val minPoints: Int = 0,
    val nextAt: Int? = null,
)

@Serializable
data class ProfileStatsDto(
    val points: Int = 0,
    val quizzesDone: Int = 0,
    val casesSolved: Int = 0,
    val articlesRead: Int = 0,
    val percent: Int = 0,
    val streakDays: Int = 0,
)

@Serializable
data class WeekActivityDto(
    val days: List<ActivityDayDto> = emptyList(),
    val totalMinutes: Int = 0,
    val activeDays: Int = 0,
)

@Serializable
data class ActivityDayDto(
    val label: String = "",
    val date: String = "",
    val minutes: Int = 0,
    val lessons: Int = 0,
    val isToday: Boolean = false,
    val isFuture: Boolean = false,
)

@Serializable
data class AchievementsDto(
    val earned: Int = 0,
    val total: Int = 0,
    val items: List<AchievementDto> = emptyList(),
)

@Serializable
data class BookmarkCountsDto(
    val article: Int = 0,
    val term: Int = 0,
    val post: Int = 0,
    val case: Int = 0,
)

@Serializable
data class UpdateMeRequestDto(
    val name: String? = null,
    val city: String? = null,
    val locale: String? = null,
    val textSize: String? = null,
    val notifyDaily: Boolean? = null,
    val dailyGoal: Int? = null,
)

@Serializable
data class MeResponseDto(
    val ok: Boolean = true,
    val user: MeDto,
)
