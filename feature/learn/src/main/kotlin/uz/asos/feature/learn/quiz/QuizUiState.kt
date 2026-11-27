package uz.asos.feature.learn.quiz

import uz.asos.core.model.learning.QuestionMark

enum class QuizPhase {
    LOADING,

    NEEDS_SIGN_IN,

    ERROR,

    QUESTION,

    RESULT,
}

data class QuizUiState(
    val phase: QuizPhase = QuizPhase.LOADING,
    val errorMessage: String? = null,
    val quizTitle: String = "",
    val difficultyLabel: String = "",
    val marks: List<QuestionMark> = emptyList(),
    val currentNumber: Int = 0,
    val total: Int = 0,
    val question: QuestionUi? = null,
    val selectedOptionId: Int? = null,
    val isSubmitting: Boolean = false,
    val feedback: FeedbackUi? = null,
    val questionStartedAt: Long = 0L,
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
    val label: String,
    val text: String,
)

data class FeedbackUi(
    val isCorrect: Boolean,
    val correctOptionId: Int?,
    val pointsEarned: Int,
    val correctAnswerText: String?,
    val explanation: String?,
    val articleNumber: Int?,
    val isLastQuestion: Boolean,
)

data class QuizResultUi(
    val correct: Int,
    val total: Int,
    val percent: Int,
    val points: Int,
    val isPerfect: Boolean,
    val newAchievements: List<String>,
)

sealed interface QuizEvent {
    data class Message(val text: String) : QuizEvent
}
