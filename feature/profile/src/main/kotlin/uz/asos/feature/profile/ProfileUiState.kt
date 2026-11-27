package uz.asos.feature.profile

import uz.asos.core.model.user.Achievement
import uz.asos.core.model.user.BookmarkCounts
import uz.asos.core.model.user.ReaderTextSize
import uz.asos.core.model.user.User
import uz.asos.core.model.user.WeekActivity

data class ProfileUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isSignedIn: Boolean = false,
    val user: User? = null,
    val pointsLabel: String = "",
    val quizzesDone: Int = 0,
    val casesSolved: Int = 0,
    val week: WeekActivity? = null,
    val levelProgress: LevelProgressUi? = null,
    val achievementsEarned: Int = 0,
    val achievementsTotal: Int = 0,
    val achievements: List<Achievement> = emptyList(),
    val bookmarks: BookmarkCounts = BookmarkCounts(0, 0, 0, 0),
    val textSize: ReaderTextSize = ReaderTextSize.MEDIUM,
    val notifyDaily: Boolean = false,
    val isTextSizeSheetOpen: Boolean = false,
    val isLanguageSheetOpen: Boolean = false,
    val notice: String? = null,
)

data class LevelProgressUi(
    val nextLevel: Int,
    val pointsLabel: String,
    val targetLabel: String,
    val fraction: Float,
)
