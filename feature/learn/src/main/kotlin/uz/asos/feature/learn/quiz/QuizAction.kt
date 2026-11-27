package uz.asos.feature.learn.quiz

sealed interface QuizAction {
    data class OnOptionClick(val optionId: Int) : QuizAction

    data object OnNextClick : QuizAction

    data object OnFinishClick : QuizAction

    data object OnRestartClick : QuizAction

    data class OnArticleClick(val number: Int) : QuizAction

    data object OnCloseClick : QuizAction

    data object OnSignInClick : QuizAction

    data object OnRetry : QuizAction
}
