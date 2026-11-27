/*
 * LearningApi.kt
 * Module: :core:network   ·   Work package: WP-07
 *
 * Retrofit interface for the O‘rganish tag of docs/api/openapi.yaml.
 */
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

    /** The whole O‘rganish screen (optional token: adds overview, dailyGoal, points, streak). */
    @GET("v1/learn")
    suspend fun getLearn(): LearnResponseDto

    /**
     * Opens an attempt (token required). An unfinished attempt is returned instead of a new
     * one, so "Davom etish" works without extra state. [quizId] = id or slug.
     */
    @POST("v1/quizzes/{id}/attempts")
    suspend fun startAttempt(@Path("id") quizId: String): AttemptResponseDto

    /** Answers one question. A second answer to the same question is rejected with 409. */
    @POST("v1/attempts/{id}/answers")
    suspend fun answer(
        @Path("id") attemptId: Int,
        @Body body: AnswerRequestDto,
    ): AnswerResponseDto

    @POST("v1/attempts/{id}/finish")
    suspend fun finish(@Path("id") attemptId: Int): FinishResponseDto
}
