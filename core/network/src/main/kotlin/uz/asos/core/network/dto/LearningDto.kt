/*
 * LearningDto.kt
 * Module: :core:network   ·   Work package: WP-07
 *
 * @Serializable DTOs for the O‘rganish endpoints. Field names mirror the OpenAPI schemas
 * (Quiz, DailyGoal, Achievement, Attempt, AttemptQuestion, QuizOption, AttemptResult).
 * Every field has a default so a missing or new field never crashes parsing.
 */
package uz.asos.core.network.dto

import kotlinx.serialization.Serializable

// ---------------------------------------------------------------- GET /v1/learn

@Serializable
data class LearnResponseDto(
    val ok: Boolean = true,
    val quizzes: List<QuizDto> = emptyList(),
    val casesSolved: Int = 0,
    val casesNew: Int = 0,
    /** Only with a token. */
    val overview: ReadingOverviewDto? = null,
    /** Only with a token. */
    val dailyGoal: DailyGoalDto? = null,
    /** Only with a token. */
    val points: Int? = null,
    /** Only with a token. */
    val streakDays: Int? = null,
    val achievements: List<AchievementDto> = emptyList(),
)

@Serializable
data class QuizDto(
    val id: Int = 0,
    val slug: String = "",
    val title: String = "",
    /** boshlangich | orta | murakkab */
    val difficulty: String = "orta",
    val icon: String? = null,
    val tone: String? = null,
    val estMinutes: Int = 0,
    val questionCount: Int = 0,
    val isGenerated: Boolean = false,
    val topic: QuizTopicDto? = null,
    val myBest: QuizBestDto? = null,
)

@Serializable
data class QuizTopicDto(
    val slug: String = "",
    val title: String = "",
)

@Serializable
data class QuizBestDto(
    val correct: Int = 0,
    val total: Int = 0,
    val percent: Int = 0,
    val attempts: Int = 0,
    val isPerfect: Boolean = false,
    val openAttemptId: Int? = null,
)

@Serializable
data class DailyGoalDto(
    val goal: Int = 0,
    val done: Int = 0,
    val rawDone: Int = 0,
    val completed: Boolean = false,
)

@Serializable
data class AchievementDto(
    val id: Int = 0,
    val slug: String = "",
    val title: String = "",
    val hint: String? = null,
    val icon: String? = null,
    val tone: String? = null,
    val metric: String? = null,
    val threshold: Int = 0,
    val progress: Int = 0,
    val earned: Boolean = false,
    val earnedAt: String? = null,
)

// ---------------------------------------------------------------- attempts

/** POST /v1/quizzes/{id}/attempts and GET /v1/attempts/{id}. */
@Serializable
data class AttemptResponseDto(
    val ok: Boolean = true,
    val attempt: AttemptDto = AttemptDto(),
)

@Serializable
data class AttemptDto(
    val id: Int = 0,
    val quiz: AttemptQuizDto = AttemptQuizDto(),
    val total: Int = 0,
    val answered: Int = 0,
    val correctCount: Int = 0,
    val points: Int = 0,
    val startedAt: String? = null,
    val finishedAt: String? = null,
    val isFinished: Boolean = false,
    /** correct | wrong | current | pending, one per question. */
    val marks: List<String> = emptyList(),
    val nextQuestionId: Int? = null,
    val questions: List<AttemptQuestionDto> = emptyList(),
)

@Serializable
data class AttemptQuizDto(
    val id: Int = 0,
    val slug: String = "",
    val title: String = "",
    val difficulty: String = "orta",
)

@Serializable
data class AttemptQuestionDto(
    val id: Int = 0,
    val index: Int = 0,
    val body: String = "",
    val points: Int = 0,
    val seconds: Int = 0,
    val options: List<QuizOptionDto> = emptyList(),
    val answered: Boolean = false,
    val myOptionId: Int? = null,
    val isCorrect: Boolean? = null,
    val explanation: String? = null,
    val article: ArticleRefDto? = null,
)

@Serializable
data class QuizOptionDto(
    val id: Int = 0,
    val label: String = "",
    val body: String = "",
    /** Only present after the question is answered. */
    val isCorrect: Boolean? = null,
)

/** Body of POST /v1/attempts/{id}/answers. */
@Serializable
data class AnswerRequestDto(
    val questionId: Int,
    val optionId: Int?,
    val seconds: Int,
)

@Serializable
data class AnswerResponseDto(
    val ok: Boolean = true,
    val isCorrect: Boolean = false,
    val correctOptionId: Int? = null,
    val pointsEarned: Int = 0,
    val points: Int = 0,
    val explanation: String? = null,
    val article: ArticleRefDto? = null,
    val answered: Int = 0,
    val total: Int = 0,
    val result: AttemptResultDto? = null,
)

/** POST /v1/attempts/{id}/finish. */
@Serializable
data class FinishResponseDto(
    val ok: Boolean = true,
    val result: AttemptResultDto = AttemptResultDto(),
)

@Serializable
data class AttemptResultDto(
    val attemptId: Int = 0,
    val total: Int = 0,
    val correctCount: Int = 0,
    val percent: Int = 0,
    val points: Int = 0,
    val isPerfect: Boolean = false,
    val finishedAt: String? = null,
    val achievements: List<AchievementDto> = emptyList(),
)
