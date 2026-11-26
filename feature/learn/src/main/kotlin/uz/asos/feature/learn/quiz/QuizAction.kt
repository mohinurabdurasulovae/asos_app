/*
 * QuizAction.kt
 * Module: :feature:learn   ·   Work package: WP-07
 *
 * Everything the user can do on the Test savoli screen.
 */
package uz.asos.feature.learn.quiz

sealed interface QuizAction {
    /** Tapping an option answers the question (as in the design). */
    data class OnOptionClick(val optionId: Int) : QuizAction

    /** "Keyingi savol" / "Natijani ko‘rish". */
    data object OnNextClick : QuizAction

    /** "Tugatish": finish the attempt now and see the result. */
    data object OnFinishClick : QuizAction

    /** Result screen "Qaytadan": open a new attempt of the same test. */
    data object OnRestartClick : QuizAction

    data class OnArticleClick(val number: Int) : QuizAction

    /** X in the top bar, or "O‘rganishga qaytish". */
    data object OnCloseClick : QuizAction

    data object OnSignInClick : QuizAction

    data object OnRetry : QuizAction
}
