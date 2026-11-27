package uz.asos.core.data.repository

import uz.asos.core.common.result.AsosResult
import uz.asos.core.model.learning.AnswerResult
import uz.asos.core.model.learning.AttemptResult
import uz.asos.core.model.learning.LearnHub
import uz.asos.core.model.learning.QuizAttempt

interface LearningRepository {

    suspend fun getLearnHub(): AsosResult<LearnHub>

    suspend fun startAttempt(quizKey: String): AsosResult<QuizAttempt>

    suspend fun answer(attemptId: Int, questionId: Int, optionId: Int, seconds: Int): AsosResult<AnswerResult>

    suspend fun finish(attemptId: Int): AsosResult<AttemptResult>
}
