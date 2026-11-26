/*
 * LearnVisuals.kt
 * Module: :feature:learn   ·   Work package: WP-07
 *
 * Icon and colour choices shared by the O‘rganish and Test savoli screens. The API's `icon`
 * names are not a fixed list yet, so icons are picked from the icon name, topic slug or title.
 */
package uz.asos.feature.learn.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.model.learning.QuizDifficulty

/** Icon colour + soft background, as in the design's 44 dp icon tiles. */
internal data class IconTone(val content: Color, val container: Color)

internal object LearnTones {
    val Brand = IconTone(AsosColors.Brand600, AsosColors.Brand50)
    val Teal = IconTone(AsosColors.TealIcon, AsosColors.TealBg)
    val Purple = IconTone(AsosColors.Purple, AsosColors.PurpleBg)
    val Orange = IconTone(AsosColors.Orange, AsosColors.OrangeBg)
    val Pink = IconTone(AsosColors.RoseIcon, AsosColors.RoseBg)
    val Navy = IconTone(AsosColors.Navy, AsosColors.SlateBg)
    val Streak = IconTone(AsosColors.StreakFlame, AsosColors.OrangeBg)

    private val cycle = listOf(Brand, Teal, Purple, Orange, Navy)

    /** The API tone; when it is missing, a stable colour from [fallbackKey]. */
    fun of(tone: String?, fallbackKey: String): IconTone = when (tone?.lowercase()) {
        "brand", "blue" -> Brand
        "teal", "green" -> Teal
        "purple", "violet" -> Purple
        "orange", "amber", "warning" -> Orange
        "pink", "rose" -> Pink
        "navy", "slate" -> Navy
        else -> cycle[(fallbackKey.hashCode() and Int.MAX_VALUE) % cycle.size]
    }
}

/** Icon for a quiz row, from its icon name / topic slug / title. */
internal fun quizIcon(hint: String): ImageVector {
    val key = hint.lowercase()
    return when {
        "shield" in key || "shaxs" in key || "erkin" in key -> AsosIcons.ShieldCheck
        "ballot" in key || "siyos" in key || "saylov" in key -> AsosIcons.Ballot
        "briefcase" in key || "ijtimoiy" in key || "mehnat" in key || "iqtisod" in key -> AsosIcons.Briefcase
        "clipboard" in key || "burch" in key -> AsosIcons.ClipboardCheck
        "government" in key || "building" in key || "davlat" in key || "hokimiyat" in key -> AsosIcons.Government
        "family" in key || "oila" in key || "bola" in key || "yosh" in key -> AsosIcons.Family
        "scales" in key || "sud" in key || "adolat" in key -> AsosIcons.Scales
        "graduation" in key || "talim" in key || "ta’lim" in key || "ta'lim" in key -> AsosIcons.GraduationCap
        "globe" in key || "xalqaro" in key -> AsosIcons.Globe
        "people" in key || "jamiyat" in key -> AsosIcons.People
        else -> AsosIcons.Book
    }
}

/** Icon for an achievement, from its icon name / metric / slug. */
internal fun achievementIcon(hint: String): ImageVector {
    val key = hint.lowercase()
    return when {
        "flame" in key || "fire" in key || "streak" in key || "seriya" in key -> FlameFilled
        "book" in key || "articles" in key || "qadam" in key -> AsosIcons.BookOpen
        "trophy" in key || "cup" in key || "quizzes" in key || "bilimdon" in key -> AsosIcons.Trophy
        "scales" in key || "case" in key || "kazus" in key -> AsosIcons.Scales
        "star" in key || "points" in key || "ball" in key -> AsosIcons.Star
        "dictionary" in key || "terms" in key || "lug" in key -> AsosIcons.Book
        "comment" in key || "posts" in key -> AsosIcons.Comment
        else -> AsosIcons.Trophy
    }
}

/** Default tone of an achievement when the API sends none. */
internal fun achievementTone(tone: String?, hint: String): IconTone {
    if (tone != null) return LearnTones.of(tone, hint)
    val key = hint.lowercase()
    return when {
        "streak" in key || "flame" in key -> LearnTones.Streak
        "quizzes" in key || "trophy" in key -> LearnTones.Teal
        "cases" in key || "scales" in key -> LearnTones.Purple
        else -> LearnTones.Brand
    }
}

/** "Boshlang‘ich" / "O‘rta" / "Murakkab" — same labels as the selector. */
internal fun QuizDifficulty.label(): String = when (this) {
    QuizDifficulty.EASY -> "Boshlang‘ich"
    QuizDifficulty.MEDIUM -> "O‘rta"
    QuizDifficulty.HARD -> "Murakkab"
}

/** Filled flame (the stroked AsosIcons.Flame is used elsewhere). */
internal val FlameFilled: ImageVector by lazy {
    ImageVector.Builder(
        name = "FlameFilled",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).addPath(
        pathData = addPathNodes("M12 3c1 3.5 5 5.5 5 10a5 5 0 0 1-10 0c0-2 1-3.5 2-4.5 0 2 1 3 2 3 0-3-1-5.5 1-8.5z"),
        fill = SolidColor(Color.Black),
    ).build()
}
