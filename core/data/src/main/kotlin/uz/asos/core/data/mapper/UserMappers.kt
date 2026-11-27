package uz.asos.core.data.mapper

import uz.asos.core.model.user.Achievement
import uz.asos.core.model.user.AchievementMetric
import uz.asos.core.model.user.ActivityDay
import uz.asos.core.model.user.BookmarkCounts
import uz.asos.core.model.user.DailyGoal
import uz.asos.core.model.user.ReaderTextSize
import uz.asos.core.model.user.User
import uz.asos.core.model.user.UserLevel
import uz.asos.core.model.user.UserProfile
import uz.asos.core.model.user.UserStats
import uz.asos.core.model.user.WeekActivity
import uz.asos.core.network.dto.AchievementDto
import uz.asos.core.network.dto.ActivityDayDto
import uz.asos.core.network.dto.BookmarkCountsDto
import uz.asos.core.network.dto.DailyGoalDto
import uz.asos.core.network.dto.LevelDto
import uz.asos.core.network.dto.MeDto
import uz.asos.core.network.dto.ProfileResponseDto
import uz.asos.core.network.dto.ProfileStatsDto
import uz.asos.core.network.dto.WeekActivityDto

internal fun ProfileResponseDto.asModel() = UserProfile(
    user = user.asModel(),
    stats = stats.asModel(),
    week = week.asModel(),
    dailyGoal = dailyGoal?.asUserModel(),
    achievementsEarned = achievements.earned,
    achievementsTotal = achievements.total,
    achievements = achievements.items.map { it.asUserModel() },
    bookmarks = bookmarks.asModel(),
)

internal fun MeDto.asModel() = User(
    id = id,
    name = name,
    username = username?.takeIf { it.isNotBlank() },
    initials = initials.ifBlank { name.take(1).uppercase() },
    avatarUrl = avatarUrl?.takeIf { it.isNotBlank() },
    city = city?.takeIf { it.isNotBlank() },
    points = points,
    level = level.asModel(),
    streakDays = streakDays,
    locale = locale?.takeIf { it.isNotBlank() },
    textSize = ReaderTextSize.fromApi(textSize),
    notifyDaily = notifyDaily,
)

internal fun LevelDto.asModel() = UserLevel(
    number = number,
    title = title,
    minPoints = minPoints,
    nextAt = nextAt,
)

internal fun ProfileStatsDto.asModel() = UserStats(
    points = points,
    quizzesDone = quizzesDone,
    casesSolved = casesSolved,
    articlesRead = articlesRead,
    percent = percent.coerceIn(0, 100),
    streakDays = streakDays,
)

internal fun WeekActivityDto.asModel() = WeekActivity(
    days = days.map { it.asModel() },
    totalMinutes = totalMinutes,
    activeDays = activeDays,
)

internal fun ActivityDayDto.asModel() = ActivityDay(
    label = label,
    date = date,
    minutes = minutes,
    lessons = lessons,
    isToday = isToday,
    isFuture = isFuture,
)

internal fun DailyGoalDto.asUserModel() = DailyGoal(
    goal = goal,
    done = done,
    completed = completed,
)

internal fun AchievementDto.asUserModel() = Achievement(
    id = id,
    slug = slug,
    title = title,
    hint = hint?.takeIf { it.isNotBlank() },
    icon = icon?.takeIf { it.isNotBlank() },
    tone = tone?.takeIf { it.isNotBlank() },
    metric = AchievementMetric.fromApi(metric),
    threshold = threshold,
    progress = progress,
    earned = earned,
)

internal fun BookmarkCountsDto.asModel() = BookmarkCounts(
    articles = article,
    terms = term,
    posts = post,
    cases = case,
)
