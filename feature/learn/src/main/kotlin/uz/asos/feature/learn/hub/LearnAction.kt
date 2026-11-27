package uz.asos.feature.learn.hub

import uz.asos.core.model.learning.QuizDifficulty

sealed interface LearnAction {
    data class OnDifficultySelect(val difficulty: QuizDifficulty) : LearnAction

    data class OnQuizClick(val key: String) : LearnAction

    data object OnCasesClick : LearnAction

    data object OnAchievementsClick : LearnAction

    data object OnSignInClick : LearnAction

    data object OnRetry : LearnAction

    data object OnResume : LearnAction
}
