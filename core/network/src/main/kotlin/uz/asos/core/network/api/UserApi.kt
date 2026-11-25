/*
 * UserApi.kt
 * Module: :core:network   ·   Work package: WP-02
 *
 * Retrofit interface for the /v1/me endpoints (tag Profil in openapi.yaml). All calls need a token.
 *
 * Implemented: reading progress, bookmarks. Still to add: GET/PATCH /v1/me, profile,
 * achievements, points, notifications.
 */
package uz.asos.core.network.api

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import uz.asos.core.network.dto.BookmarkRequestDto
import uz.asos.core.network.dto.BookmarkResponseDto
import uz.asos.core.network.dto.ReadingOverviewDto

interface UserApi {

    /** Overall reading result and the "Davom ettiring" card. */
    @GET("v1/me/progress")
    suspend fun getReadingProgress(): ReadingOverviewDto

    @POST("v1/me/bookmarks")
    suspend fun addBookmark(@Body body: BookmarkRequestDto): BookmarkResponseDto

    /** [id] is the item's internal id (Article.id, not the article number). */
    @DELETE("v1/me/bookmarks/{kind}/{id}")
    suspend fun removeBookmark(
        @Path("kind") kind: String,
        @Path("id") id: Int,
    ): BookmarkResponseDto
}
