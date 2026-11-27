package uz.asos.feature.auth.data.network

import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

internal interface SignInApi {

    @POST("v1/auth/request-code")
    suspend fun requestCode(@Body body: RequestCodeBodyDto): RequestCodeResponseDto

    @POST("v1/auth/verify")
    suspend fun verify(@Body body: VerifyCodeBodyDto): AuthResponseDto

    @POST("v1/auth/oauth")
    suspend fun oauth(@Body body: OAuthBodyDto): AuthResponseDto

    @PATCH("v1/me")
    suspend fun updateMe(@Body body: UpdateMeBodyDto): MeResponseDto
}
