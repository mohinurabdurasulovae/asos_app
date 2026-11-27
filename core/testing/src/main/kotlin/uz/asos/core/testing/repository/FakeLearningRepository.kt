/*
 * FakeLearningRepository.kt
 * Module: :core:testing   ·   Work package: WP-07
 *
 * In-memory fake of LearningRepository. Results can be swapped per test; calls are recorded.
 */
package uz.asos.core.testing.repository

import uz.asos.core.common.result.AsosResult
import uz.asos.core.data.repository.LearningRepository
import uz.asos.core.model.learning.AnswerResult
import uz.asos.core.model.learning.AttemptResult
import uz.asos.core.model.learning.LearnHub
import uz.asos.core.model.learning.QuizAttempt
import uz.asos.core.testing.data.LearnSampleData

class FakeLearningRepository : LearningRepository {

    var hubResult: AsosResult<LearnHub> = AsosResult.Success(LearnSampleData.hub)
    var attemptResult: AsosResult<QuizAttempt> = AsosResult.Success(LearnSampleData.attempt)
    var finishResult: AsosResult<AttemptResult> = AsosResult.Success(LearnSampleData.result)

    /** Answer per question id; default: [LearnSampleData.answerFor]. */
    var answerResults: MutableMap<Int, AsosResult<AnswerResult>> = mutableMapOf()

    data class AnswerCall(val attemptId: Int, val questionId: Int, val optionId: Int, val seconds: Int)

    val answerCalls = mutableListOf<AnswerCall>()
    val startedQuizzes = mutableListOf<String>()
    var hubCalls = 0
        private set
    var finishCalls = 0
        private set

    override suspend fun getLearnHub(): AsosResult<LearnHub> {
        hubCalls++
        return hubResult
    }

    override suspend fun startAttempt(quizKey: String): AsosResult<QuizAttempt> {
        startedQuizzes += quizKey
        return attemptResult
    }

    override suspend fun answer(
        attemptId: Int,
        questionId: Int,
        optionId: Int,
        seconds: Int,
    ): AsosResult<AnswerResult> {
        answerCalls += AnswerCall(attemptId, questionId, optionId, seconds)
        return answerResults[questionId] ?: AsosResult.Success(LearnSampleData.answerFor(questionId, optionId))
    }

    override suspend fun finish(attemptId: Int): AsosResult<AttemptResult> {
        finishCalls++
        return finishResult
    }
}
