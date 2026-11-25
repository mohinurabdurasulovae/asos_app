/*
 * ConstitutionApi.kt
 * Module: :core:network   ·   Work package: WP-02
 *
 * Retrofit interface for the Konstitutsiya tag of docs/api/openapi.yaml.
 * Endpoints marked "optional token" work for guests; with a token they also return the
 * user's reading progress and saved state.
 */
package uz.asos.core.network.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import uz.asos.core.network.dto.ArticleListResponseDto
import uz.asos.core.network.dto.ArticleProgressRequestDto
import uz.asos.core.network.dto.ArticleProgressResponseDto
import uz.asos.core.network.dto.ArticleResponseDto
import uz.asos.core.network.dto.ChapterResponseDto
import uz.asos.core.network.dto.ConstitutionResponseDto
import uz.asos.core.network.dto.TopicsResponseDto

interface ConstitutionApi {

    /** Section and chapter tree (optional token: adds readCount / percent per chapter). */
    @GET("v1/constitution")
    suspend fun getConstitution(): ConstitutionResponseDto

    @GET("v1/topics")
    suspend fun getTopics(): TopicsResponseDto

    /** One chapter and its articles (optional token). [roman] e.g. "IX". */
    @GET("v1/chapters/{roman}")
    suspend fun getChapter(@Path("roman") roman: String): ChapterResponseDto

    /**
     * Article list (optional token). [status] = read | unread | saved works only with a token.
     * [q] is an article number or a word.
     */
    @GET("v1/articles")
    suspend fun getArticles(
        @Query("topic") topic: String? = null,
        @Query("chapter") chapter: String? = null,
        @Query("section") section: String? = null,
        @Query("q") q: String? = null,
        @Query("status") status: String? = null,
        @Query("page") page: Int = 1,
        @Query("perPage") perPage: Int = 100,
    ): ArticleListResponseDto

    /** Full article for the reader. [key] = number ("50"), slug, or "muqaddima". */
    @GET("v1/articles/{key}")
    suspend fun getArticle(@Path("key") key: String): ArticleResponseDto

    /** Token required. The first `read` gives 10 points. */
    @POST("v1/articles/{key}/progress")
    suspend fun updateProgress(
        @Path("key") key: String,
        @Body body: ArticleProgressRequestDto,
    ): ArticleProgressResponseDto
}
