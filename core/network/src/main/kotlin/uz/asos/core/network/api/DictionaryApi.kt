/*
 * DictionaryApi.kt
 * Module: :core:network   ·   Work package: WP-02 (filled in for WP-06)
 *
 * Retrofit interface for the Lug‘at tag of docs/api/openapi.yaml. Every endpoint works for
 * guests; with a token the answers also carry `isSaved`. `saved=1` needs a token.
 */
package uz.asos.core.network.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uz.asos.core.network.dto.TermListResponseDto
import uz.asos.core.network.dto.TermResponseDto

interface DictionaryApi {

    /** Terms (sorted A–Z) plus the alphabet rail. [saved] = "1" for the user's saved terms only. */
    @GET("v1/terms")
    suspend fun getTerms(
        @Query("q") q: String? = null,
        @Query("letter") letter: String? = null,
        @Query("saved") saved: String? = null,
        @Query("page") page: Int = 1,
        @Query("perPage") perPage: Int = 100,
    ): TermListResponseDto

    @GET("v1/terms/of-the-day")
    suspend fun getTermOfTheDay(): TermResponseDto

    @GET("v1/terms/{slug}")
    suspend fun getTerm(@Path("slug") slug: String): TermResponseDto
}
