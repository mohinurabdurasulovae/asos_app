package uz.asos.core.network.api

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import uz.asos.core.network.dto.BookmarkRequestDto
import uz.asos.core.network.dto.BookmarkResponseDto
import uz.asos.core.network.dto.MeResponseDto
import uz.asos.core.network.dto.ProfileResponseDto
import uz.asos.core.network.dto.ReadingOverviewDto
import uz.asos.core.network.dto.UpdateMeRequestDto

interface UserApi {

    @GET("v1/me/profile")
    suspend fun getProfile(): ProfileResponseDto

    @PATCH("v1/me")
    suspend fun updateMe(@Body body: UpdateMeRequestDto): MeResponseDto

    @GET("v1/me/progress")
    suspend fun getReadingProgress(): ReadingOverviewDto

    @POST("v1/me/bookmarks")
    suspend fun addBookmark(@Body body: BookmarkRequestDto): BookmarkResponseDto

    @DELETE("v1/me/bookmarks/{kind}/{id}")
    suspend fun removeBookmark(
        @Path("kind") kind: String,
        @Path("id") id: Int,
    ): BookmarkResponseDto
}
