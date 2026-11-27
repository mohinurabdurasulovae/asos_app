/*
 * LearnAction.kt
 * Module: :feature:learn   ·   Work package: WP-07
 *
 * Everything the user can do on the O‘rganish screen.
 */
package uz.asos.feature.learn.hub

import uz.asos.core.model.learning.QuizDifficulty

sealed interface LearnAction {
    data class OnDifficultySelect(val difficulty: QuizDifficulty) : LearnAction

    /** Opens Test savoli. [key] = quiz slug or id. */
    data class OnQuizClick(val key: String) : LearnAction

    data object OnCasesClick : LearnAction

    /** "Barchasi" next to Yutuqlar. */
    data object OnAchievementsClick : LearnAction

    data object OnSignInClick : LearnAction

    data object OnRetry : LearnAction

    /** Screen is visible again (e.g. back from a quiz): refresh results quietly. */
    data object OnResume : LearnAction
}
