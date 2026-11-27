/*
 * DictionaryApi.kt
 * Module: :core:network   ·   Work package: WP-02
 *
 * Retrofit interface for the Lug‘at tag of docs/api/openapi.yaml.
 * Reading works without a token; `saved=1` and isSaved need one.
 */
package uz.asos.core.network.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uz.asos.core.network.dto.TermResponseDto
import uz.asos.core.network.dto.TermsResponseDto

interface DictionaryApi {

    /**
     * @param letter one Uzbek letter ("D", "O‘", "Sh"); null = every term.
     * @param query search text.
     * @param saved "1" returns only the user's saved terms (token required).
     */
    @GET("v1/terms")
    suspend fun getTerms(
        @Query("letter") letter: String? = null,
        @Query("q") query: String? = null,
        @Query("saved") saved: String? = null,
        @Query("page") page: Int = 1,
        @Query("perPage") perPage: Int = 30,
    ): TermsResponseDto

    /** "Kun atamasi" card at the top of Lug‘at. */
    @GET("v1/terms/of-the-day")
    suspend fun getTermOfTheDay(): TermResponseDto

    @GET("v1/terms/{slug}")
    suspend fun getTerm(@Path("slug") slug: String): TermResponseDto
}
