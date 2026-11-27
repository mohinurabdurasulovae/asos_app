package uz.asos.core.network.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import uz.asos.core.network.dto.AnswerRequestDto
import uz.asos.core.network.dto.AnswerResponseDto
import uz.asos.core.network.dto.AttemptResponseDto
import uz.asos.core.network.dto.FinishResponseDto
import uz.asos.core.network.dto.LearnResponseDto

interface LearningApi {

    @GET("v1/learn")
    suspend fun getLearn(): LearnResponseDto

    @POST("v1/quizzes/{id}/attempts")
    suspend fun startAttempt(@Path("id") quizId: String): AttemptResponseDto

    @POST("v1/attempts/{id}/answers")
    suspend fun answer(
        @Path("id") attemptId: Int,
        @Body body: AnswerRequestDto,
    ): AnswerResponseDto

    @POST("v1/attempts/{id}/finish")
    suspend fun finish(@Path("id") attemptId: Int): FinishResponseDto
}
