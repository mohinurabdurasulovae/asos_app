package uz.asos.feature.home.data.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

internal interface HomeFeedApi {

    @GET("v1/home")
    suspend fun getHome(): HomeResponseDto

    @POST("v1/daily-question/answer")
    suspend fun answerDailyQuestion(@Body body: DailyAnswerBodyDto): DailyAnswerResponseDto
}
