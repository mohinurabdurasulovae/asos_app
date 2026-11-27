package uz.asos.core.network.api

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import uz.asos.core.network.dto.CommentResponseDto
import uz.asos.core.network.dto.CommunitiesResponseDto
import uz.asos.core.network.dto.CreateCommentRequestDto
import uz.asos.core.network.dto.CreatePostRequestDto
import uz.asos.core.network.dto.JoinResponseDto
import uz.asos.core.network.dto.PostResponseDto
import uz.asos.core.network.dto.PostsResponseDto
import uz.asos.core.network.dto.VoteRequestDto
import uz.asos.core.network.dto.VoteResponseDto

interface CommunityApi {

    @GET("v1/communities")
    suspend fun getCommunities(): CommunitiesResponseDto

    @GET("v1/posts")
    suspend fun getPosts(
        @Query("community") community: String? = null,
        @Query("sort") sort: String? = null,
        @Query("q") query: String? = null,
        @Query("article") articleNumber: Int? = null,
        @Query("page") page: Int = 1,
        @Query("perPage") perPage: Int = 15,
    ): PostsResponseDto

    @GET("v1/posts/{id}")
    suspend fun getPost(@Path("id") id: Int): PostResponseDto

    @POST("v1/posts")
    suspend fun createPost(@Body body: CreatePostRequestDto): PostResponseDto

    @POST("v1/posts/{id}/vote")
    suspend fun votePost(
        @Path("id") id: Int,
        @Body body: VoteRequestDto,
    ): VoteResponseDto

    @POST("v1/posts/{id}/comments")
    suspend fun addComment(
        @Path("id") postId: Int,
        @Body body: CreateCommentRequestDto,
    ): CommentResponseDto

    @POST("v1/comments/{id}/vote")
    suspend fun voteComment(
        @Path("id") id: Int,
        @Body body: VoteRequestDto,
    ): VoteResponseDto

    @POST("v1/communities/{slug}/join")
    suspend fun joinCommunity(@Path("slug") slug: String): JoinResponseDto

    @DELETE("v1/communities/{slug}/join")
    suspend fun leaveCommunity(@Path("slug") slug: String): JoinResponseDto
}
