/*
 * LearnUiState.kt
 * Module: :feature:learn   ·   Work package: WP-07
 * Screen: O‘rganish (design artboard: Organish.dc.html)
 *
 * Everything LearnScreen draws. No Android UI types, so the ViewModel stays testable.
 */
package uz.asos.feature.learn.hub

import uz.asos.core.model.learning.QuizDifficulty

data class LearnUiState(
    val isLoading: Boolean = true,
    /** Shown full screen only when nothing has loaded yet. */
    val errorMessage: String? = null,
    val isSignedIn: Boolean = false,
    /** Header pills (signed in only). */
    val streakDays: Int? = null,
    /** "1 240" */
    val pointsLabel: String? = null,
    /** null for guests: the sign-in card is shown instead. */
    val progress: OverallProgressUi? = null,
    val cases: CasesPromoUi = CasesPromoUi(solved = 0, new = 0),
    val selectedDifficulty: QuizDifficulty = QuizDifficulty.MEDIUM,
    /** All quizzes; the list shows the ones of [selectedDifficulty]. */
    val quizzes: List<QuizRowUi> = emptyList(),
    val achievements: List<AchievementUi> = emptyList(),
) {
    val visibleQuizzes: List<QuizRowUi>
        get() = quizzes.filter { it.difficulty == selectedDifficulty }
}

/** "Umumiy natija" card. */
data class OverallProgressUi(
    val percent: Int,
    val articlesRead: Int,
    val articlesTotal: Int,
    /** Daily goal in lessons; 0 hides the goal row. */
    val goal: Int,
    val done: Int,
)

/** Navy "Kazuslar" card. */
data class CasesPromoUi(
    val solved: Int,
    val new: Int,
)

enum class QuizRowState {
    /** Never tried: "Boshlash" button. */
    NEW,

    /** Tried or open attempt: percent + progress bar. */
    IN_PROGRESS,

    /** All answers right: green check. */
    DONE,
}

data class QuizRowUi(
    /** Route key (slug or id). */
    val key: String,
    val title: String,
    val difficulty: QuizDifficulty,
    /** Icon name from the API, or the topic slug, used to pick an icon. */
    val iconHint: String,
    val tone: String?,
    val state: QuizRowState,
    /** "12 savol · 9/12 to‘g‘ri · Davom etish" */
    val meta: String,
    /** 0..100 (IN_PROGRESS). */
    val percent: Int,
)

data class AchievementUi(
    val slug: String,
    val title: String,
    /** Icon name or metric, used to pick an icon. */
    val iconHint: String,
    val tone: String?,
    val earned: Boolean,
    /** "7 / 10" on locked achievements. */
    val progressLabel: String?,
)
