package uz.asos.core.network.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uz.asos.core.network.dto.TermResponseDto
import uz.asos.core.network.dto.TermsResponseDto

interface DictionaryApi {

    @GET("v1/terms")
    suspend fun getTerms(
        @Query("letter") letter: String? = null,
        @Query("q") query: String? = null,
        @Query("saved") saved: String? = null,
        @Query("page") page: Int = 1,
        @Query("perPage") perPage: Int = 30,
    ): TermsResponseDto

    @GET("v1/terms/of-the-day")
    suspend fun getTermOfTheDay(): TermResponseDto

    @GET("v1/terms/{slug}")
    suspend fun getTerm(@Path("slug") slug: String): TermResponseDto
}
