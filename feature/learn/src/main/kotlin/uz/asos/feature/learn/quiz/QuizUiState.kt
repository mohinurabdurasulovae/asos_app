/*
 * QuizUiState.kt
 * Module: :feature:learn   ·   Work package: WP-07
 * Screen: Test savoli (design artboard: Test.dc.html)
 *
 * Everything QuizScreen draws. No Android UI types, so the ViewModel stays testable.
 */
package uz.asos.feature.learn.quiz

import uz.asos.core.model.learning.QuestionMark

enum class QuizPhase {
    /** Opening the attempt. */
    LOADING,

    /** POST /v1/quizzes/{id}/attempts needs a token. */
    NEEDS_SIGN_IN,

    /** The attempt could not be opened ([QuizUiState.errorMessage]). */
    ERROR,

    /** Answering questions. */
    QUESTION,

    /** Attempt finished: score screen. */
    RESULT,
}

data class QuizUiState(
    val phase: QuizPhase = QuizPhase.LOADING,
    val errorMessage: String? = null,
    /** Chip 1: "Shaxsiy huquqlar". */
    val quizTitle: String = "",
    /** Chip 2: "O‘rta". */
    val difficultyLabel: String = "",
    /** Progress strip, one per question. */
    val marks: List<QuestionMark> = emptyList(),
    /** 1-based number of the question on screen. */
    val currentNumber: Int = 0,
    val total: Int = 0,
    val question: QuestionUi? = null,
    /** The option the user tapped (kept while the answer is being checked). */
    val selectedOptionId: Int? = null,
    val isSubmitting: Boolean = false,
    /** Filled once the server has checked the answer. */
    val feedback: FeedbackUi? = null,
    /** System time (ms) the question appeared; the screen shows a running mm:ss from it. */
    val questionStartedAt: Long = 0L,
    /** Time spent when the question was answered (freezes the timer). */
    val answeredAfterSeconds: Int? = null,
    val isFinishing: Boolean = false,
    val result: QuizResultUi? = null,
)

data class QuestionUi(
    val id: Int,
    val body: String,
    val options: List<OptionUi>,
)

data class OptionUi(
    val id: Int,
    /** "A", "B", … */
    val label: String,
    val text: String,
)

data class FeedbackUi(
    val isCorrect: Boolean,
    val correctOptionId: Int?,
    /** "+20 ball" when > 0. */
    val pointsEarned: Int,
    /** "To‘g‘ri javob: 48 soat" on a wrong answer. */
    val correctAnswerText: String?,
    val explanation: String?,
    /** "Moddani o‘qish" chip; null hides it. */
    val articleNumber: Int?,
    /** The next button says "Natijani ko‘rish". */
    val isLastQuestion: Boolean,
)

data class QuizResultUi(
    val correct: Int,
    val total: Int,
    val percent: Int,
    /** Points earned by this attempt. */
    val points: Int,
    val isPerfect: Boolean,
    /** Titles of achievements earned by this attempt. */
    val newAchievements: List<String>,
)

/** One-off things the screen shows in a snackbar. */
sealed interface QuizEvent {
    data class Message(val text: String) : QuizEvent
}
