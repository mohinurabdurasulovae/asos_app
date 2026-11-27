/*
 * ProfileUiState.kt
 * Module: :feature:profile   ·   Work package: WP-11
 * Screen: Profil (design artboard: Profil.dc.html)
 *
 * Everything ProfileScreen draws, already formatted. No Android UI types.
 */
package uz.asos.feature.profile

import uz.asos.core.model.user.Achievement
import uz.asos.core.model.user.BookmarkCounts
import uz.asos.core.model.user.ReaderTextSize
import uz.asos.core.model.user.User
import uz.asos.core.model.user.WeekActivity

data class ProfileUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    /** Guests see the sign-in card instead of the whole screen. */
    val isSignedIn: Boolean = false,
    val user: User? = null,
    /** "1 240" */
    val pointsLabel: String = "",
    val quizzesDone: Int = 0,
    val casesSolved: Int = 0,
    val week: WeekActivity? = null,
    val levelProgress: LevelProgressUi? = null,
    val achievementsEarned: Int = 0,
    val achievementsTotal: Int = 0,
    val achievements: List<Achievement> = emptyList(),
    val bookmarks: BookmarkCounts = BookmarkCounts(0, 0, 0, 0),
    /** Device setting, so it is right even before the profile loads. */
    val textSize: ReaderTextSize = ReaderTextSize.MEDIUM,
    val notifyDaily: Boolean = false,
    val isTextSizeSheetOpen: Boolean = false,
    val isLanguageSheetOpen: Boolean = false,
    val notice: String? = null,
)

/** The bar under "Bu hafta": how far the next level is. */
data class LevelProgressUi(
    val nextLevel: Int,
    /** "1 240" */
    val pointsLabel: String,
    /** "1 500" */
    val targetLabel: String,
    /** 0f..1f within the current level. */
    val fraction: Float,
)
