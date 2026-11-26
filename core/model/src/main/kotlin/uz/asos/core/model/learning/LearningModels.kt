/*
 * LearningModels.kt
 * Module: :core:model   ·   Work package: WP-07
 *
 * Domain models for the O‘rganish hub and the Test savoli flow (docs/api/openapi.yaml, tag
 * O‘rganish): GET /v1/learn, POST /v1/quizzes/{id}/attempts, POST /v1/attempts/{id}/answers,
 * POST /v1/attempts/{id}/finish.
 */
package uz.asos.core.model.learning

import uz.asos.core.model.constitution.ArticleRef
import uz.asos.core.model.constitution.ReadingOverview

/** Quiz difficulty. [apiValue] is what the API sends: boshlangich | orta | murakkab. */
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

/** Everything the O‘rganish screen shows, from one GET /v1/learn call. */
data class LearnHub(
    val quizzes: List<Quiz>,
    val casesSolved: Int,
    val casesNew: Int,
    /** null for guests. */
    val overview: ReadingOverview?,
    /** null for guests. */
    val dailyGoal: DailyGoal?,
    /** null for guests. */
    val points: Int?,
    /** null for guests. */
    val streakDays: Int?,
    val achievements: List<Achievement>,
)

data class Quiz(
    val id: Int,
    val slug: String,
    val title: String,
    val difficulty: QuizDifficulty,
    /** Icon name from the API (not a fixed list yet). */
    val icon: String?,
    /** Design tone: brand, teal, orange, purple, pink, navy. */
    val tone: String?,
    val estMinutes: Int,
    val questionCount: Int,
    val topicSlug: String?,
    val topicTitle: String?,
    /** The user's best result. null when never tried or signed out. */
    val best: QuizBest?,
) {
    /** What QuizRoute / POST /v1/quizzes/{id}/attempts accepts: slug, or the id. */
    val key: String get() = slug.ifBlank { id.toString() }
}

data class QuizBest(
    val correct: Int,
    val total: Int,
    val percent: Int,
    val attempts: Int,
    val isPerfect: Boolean,
    /** An unfinished attempt ("Davom etish"). */
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
    /** articles_read | quizzes_done | cases_solved | streak_days | points | terms_saved | posts_written */
    val metric: String?,
    val threshold: Int,
    val progress: Int,
    val earned: Boolean,
)

// ---------------------------------------------------------------------- quiz attempt

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

/** One mark in the progress strip at the top of the quiz. */
enum class QuestionMark { CORRECT, WRONG, CURRENT, PENDING }

data class QuizQuestion(
    val id: Int,
    val index: Int,
    val body: String,
    val points: Int,
    /** Suggested time in seconds. */
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
    /** "A", "B", … */
    val label: String,
    val body: String,
    /** Only known after the question is answered. */
    val isCorrect: Boolean?,
)

/** Response of POST /v1/attempts/{id}/answers. */
data class AnswerResult(
    val isCorrect: Boolean,
    val correctOptionId: Int?,
    val pointsEarned: Int,
    val totalPoints: Int,
    val explanation: String?,
    val article: ArticleRef?,
    val answered: Int,
    val total: Int,
    /** Filled when the last question was answered. */
    val result: AttemptResult?,
)

data class AttemptResult(
    val attemptId: Int,
    val total: Int,
    val correctCount: Int,
    val percent: Int,
    val points: Int,
    val isPerfect: Boolean,
    /** Achievements earned by this attempt. */
    val achievements: List<Achievement>,
)
