/*
 * DefaultLearningRepository.kt
 * Module: :core:data   ·   Work package: WP-07
 *
 * LearningRepository backed by LearningApi.
 */
package uz.asos.core.data.repository.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import uz.asos.core.common.dispatcher.AsosDispatchers
import uz.asos.core.common.dispatcher.Dispatcher
import uz.asos.core.common.result.AsosResult
import uz.asos.core.common.result.mapSuccess
import uz.asos.core.data.mapper.asModel
import uz.asos.core.data.repository.LearningRepository
import uz.asos.core.model.learning.AnswerResult
import uz.asos.core.model.learning.AttemptResult
import uz.asos.core.model.learning.LearnHub
import uz.asos.core.model.learning.QuizAttempt
import uz.asos.core.network.ApiCaller
import uz.asos.core.network.api.LearningApi
import uz.asos.core.network.dto.AnswerRequestDto
import javax.inject.Inject

internal class DefaultLearningRepository @Inject constructor(
    private val learningApi: LearningApi,
    private val apiCaller: ApiCaller,
    @Dispatcher(AsosDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : LearningRepository {

    override suspend fun getLearnHub(): AsosResult<LearnHub> = io {
        apiCaller.call { learningApi.getLearn() }.mapSuccess { it.asModel() }
    }

    override suspend fun startAttempt(quizKey: String): AsosResult<QuizAttempt> = io {
        apiCaller.call { learningApi.startAttempt(quizKey) }.mapSuccess { it.attempt.asModel() }
    }

    override suspend fun answer(
        attemptId: Int,
        questionId: Int,
        optionId: Int,
        seconds: Int,
    ): AsosResult<AnswerResult> = io {
        apiCaller.call {
            learningApi.answer(
                attemptId = attemptId,
                body = AnswerRequestDto(
                    questionId = questionId,
                    optionId = optionId,
                    seconds = seconds.coerceIn(0, MAX_SECONDS),
                ),
            )
        }.mapSuccess { it.asModel() }
    }

    override suspend fun finish(attemptId: Int): AsosResult<AttemptResult> = io {
        apiCaller.call { learningApi.finish(attemptId) }.mapSuccess { it.result.asModel() }
    }

    private suspend fun <T> io(block: suspend () -> T): T = withContext(ioDispatcher) { block() }

    private companion object {
        /** openapi.yaml: seconds is 0..3600. */
        const val MAX_SECONDS = 3600
    }
}
