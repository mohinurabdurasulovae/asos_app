/*
 * LearningRepository.kt
 * Module: :core:data   ·   Work package: WP-07
 *
 * O‘rganish hub and quiz attempts. Feature ViewModels depend on this interface only.
 */
package uz.asos.core.data.repository

import uz.asos.core.common.result.AsosResult
import uz.asos.core.model.learning.AnswerResult
import uz.asos.core.model.learning.AttemptResult
import uz.asos.core.model.learning.LearnHub
import uz.asos.core.model.learning.QuizAttempt

interface LearningRepository {

    /** GET /v1/learn. Works for guests too (no overview / points / streak). */
    suspend fun getLearnHub(): AsosResult<LearnHub>

    /** POST /v1/quizzes/{id}/attempts (token required). Returns the open attempt if there is one. */
    suspend fun startAttempt(quizKey: String): AsosResult<QuizAttempt>

    /** POST /v1/attempts/{id}/answers. [seconds] = time spent on the question. */
    suspend fun answer(attemptId: Int, questionId: Int, optionId: Int, seconds: Int): AsosResult<AnswerResult>

    /** POST /v1/attempts/{id}/finish. */
    suspend fun finish(attemptId: Int): AsosResult<AttemptResult>
}
