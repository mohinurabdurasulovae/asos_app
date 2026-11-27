package uz.asos.core.testing.data

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

object ProfileSampleData {

    val aziza = User(
        id = 1,
        name = "Aziza Karimova",
        username = "aziza.k",
        initials = "AK",
        avatarUrl = null,
        city = "Toshkent",
        points = 1240,
        level = UserLevel(number = 3, title = "Bilimdon", minPoints = 1000, nextAt = 1500),
        streakDays = 7,
        locale = "uz-Latn",
        textSize = ReaderTextSize.MEDIUM,
        notifyDaily = true,
    )

    val stats = UserStats(
        points = 1240,
        quizzesDone = 18,
        casesSolved = 7,
        articlesRead = 59,
        percent = 38,
        streakDays = 7,
    )

    val week = WeekActivity(
        days = listOf(
            ActivityDay("Du", "2026-11-23", 8, 2, isToday = false, isFuture = false),
            ActivityDay("Se", "2026-11-24", 12, 3, isToday = false, isFuture = false),
            ActivityDay("Ch", "2026-11-25", 5, 1, isToday = false, isFuture = false),
            ActivityDay("Pa", "2026-11-26", 9, 2, isToday = true, isFuture = false),
            ActivityDay("Ju", "2026-11-27", 0, 0, isToday = false, isFuture = true),
            ActivityDay("Sh", "2026-11-28", 0, 0, isToday = false, isFuture = true),
            ActivityDay("Ya", "2026-11-29", 8, 2, isToday = false, isFuture = true),
        ),
        totalMinutes = 42,
        activeDays = 6,
    )

    val achievements = listOf(
        Achievement(
            id = 1,
            slug = "birinchi-qadam",
            title = "Birinchi qadam",
            hint = "Birinchi moddani o‘qing",
            icon = "star",
            tone = "brand",
            metric = AchievementMetric.ARTICLES_READ,
            threshold = 1,
            progress = 1,
            earned = true,
        ),
        Achievement(
            id = 2,
            slug = "7-kunlik-seriya",
            title = "7 kunlik seriya",
            hint = "Ketma-ket 7 kun o‘qing",
            icon = "flame",
            tone = "orange",
            metric = AchievementMetric.STREAK_DAYS,
            threshold = 7,
            progress = 7,
            earned = true,
        ),
        Achievement(
            id = 3,
            slug = "burchlar-bilimdoni",
            title = "Burchlar bilimdoni",
            hint = "Burchlar testini yeching",
            icon = "trophy",
            tone = "teal",
            metric = AchievementMetric.QUIZZES_DONE,
            threshold = 10,
            progress = 10,
            earned = true,
        ),
        Achievement(
            id = 4,
            slug = "kazus-ustasi",
            title = "Kazus ustasi",
            hint = "10 ta kazusni yeching",
            icon = "scales",
            tone = "purple",
            metric = AchievementMetric.CASES_SOLVED,
            threshold = 10,
            progress = 7,
            earned = false,
        ),
    )

    val profile = UserProfile(
        user = aziza,
        stats = stats,
        week = week,
        dailyGoal = DailyGoal(goal = 3, done = 2, completed = false),
        achievementsEarned = 3,
        achievementsTotal = 12,
        achievements = achievements,
        bookmarks = BookmarkCounts(articles = 8, terms = 12, posts = 5, cases = 2),
    )
}
