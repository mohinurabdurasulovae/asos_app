package uz.asos.feature.learn.hub

import uz.asos.core.model.learning.QuizDifficulty

data class LearnUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isSignedIn: Boolean = false,
    val streakDays: Int? = null,
    val pointsLabel: String? = null,
    val progress: OverallProgressUi? = null,
    val cases: CasesPromoUi = CasesPromoUi(solved = 0, new = 0),
    val selectedDifficulty: QuizDifficulty = QuizDifficulty.MEDIUM,
    val quizzes: List<QuizRowUi> = emptyList(),
    val achievements: List<AchievementUi> = emptyList(),
) {
    val visibleQuizzes: List<QuizRowUi>
        get() = quizzes.filter { it.difficulty == selectedDifficulty }
}

data class OverallProgressUi(
    val percent: Int,
    val articlesRead: Int,
    val articlesTotal: Int,
    val goal: Int,
    val done: Int,
)

data class CasesPromoUi(
    val solved: Int,
    val new: Int,
)

enum class QuizRowState {
    NEW,

    IN_PROGRESS,

    DONE,
}

data class QuizRowUi(
    val key: String,
    val title: String,
    val difficulty: QuizDifficulty,
    val iconHint: String,
    val tone: String?,
    val state: QuizRowState,
    val meta: String,
    val percent: Int,
)

data class AchievementUi(
    val slug: String,
    val title: String,
    val iconHint: String,
    val tone: String?,
    val earned: Boolean,
    val progressLabel: String?,
)
