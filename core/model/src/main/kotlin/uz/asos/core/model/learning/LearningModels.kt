package uz.asos.core.model.learning

import uz.asos.core.model.constitution.ArticleRef
import uz.asos.core.model.constitution.ReadingOverview

enum class QuizDifficulty(val apiValue: String) {
    EASY("boshlangich"),
    MEDIUM("orta"),
    HARD("murakkab"),
    ;

    companion object {
        fun fromApi(value: String?): QuizDifficulty =
            entries.firstOrNull { it.apiValue == value?.lowercase() } ?: MEDIUM
    }
}

data class LearnHub(
    val quizzes: List<Quiz>,
    val casesSolved: Int,
    val casesNew: Int,
    val overview: ReadingOverview?,
    val dailyGoal: DailyGoal?,
    val points: Int?,
    val streakDays: Int?,
    val achievements: List<Achievement>,
)

data class Quiz(
    val id: Int,
    val slug: String,
    val title: String,
    val difficulty: QuizDifficulty,
    val icon: String?,
    val tone: String?,
    val estMinutes: Int,
    val questionCount: Int,
    val topicSlug: String?,
    val topicTitle: String?,
    val best: QuizBest?,
) {
    val key: String get() = slug.ifBlank { id.toString() }
}

data class QuizBest(
    val correct: Int,
    val total: Int,
    val percent: Int,
    val attempts: Int,
    val isPerfect: Boolean,
    val openAttemptId: Int?,
)

data class DailyGoal(
    val goal: Int,
    val done: Int,
    val completed: Boolean,
)

data class Achievement(
    val id: Int,
    val slug: String,
    val title: String,
    val hint: String?,
    val icon: String?,
    val tone: String?,
    val metric: String?,
    val threshold: Int,
    val progress: Int,
    val earned: Boolean,
)

data class QuizAttempt(
    val id: Int,
    val quizId: Int,
    val quizSlug: String,
    val quizTitle: String,
    val difficulty: QuizDifficulty,
    val total: Int,
    val answered: Int,
    val correctCount: Int,
    val points: Int,
    val isFinished: Boolean,
    val nextQuestionId: Int?,
    val questions: List<QuizQuestion>,
)

enum class QuestionMark { CORRECT, WRONG, CURRENT, PENDING }

data class QuizQuestion(
    val id: Int,
    val index: Int,
    val body: String,
    val points: Int,
    val seconds: Int,
    val options: List<QuizOption>,
    val answered: Boolean,
    val myOptionId: Int?,
    val isCorrect: Boolean?,
    val explanation: String?,
    val article: ArticleRef?,
)

data class QuizOption(
    val id: Int,
    val label: String,
    val body: String,
    val isCorrect: Boolean?,
)

data class AnswerResult(
    val isCorrect: Boolean,
    val correctOptionId: Int?,
    val pointsEarned: Int,
    val totalPoints: Int,
    val explanation: String?,
    val article: ArticleRef?,
    val answered: Int,
    val total: Int,
    val result: AttemptResult?,
)

data class AttemptResult(
    val attemptId: Int,
    val total: Int,
    val correctCount: Int,
    val percent: Int,
    val points: Int,
    val isPerfect: Boolean,
    val achievements: List<Achievement>,
)
