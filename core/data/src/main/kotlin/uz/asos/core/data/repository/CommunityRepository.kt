/*
 * CommunityRepository.kt
 * Module: :core:data   ·   Work package: WP-02
 *
 * Interface. Feed, one discussion, votes, comments and joining a topic.
 * Feature ViewModels depend on this interface only.
 */
package uz.asos.core.data.repository

import uz.asos.core.common.result.AsosResult
import uz.asos.core.model.community.CommunityTopic
import uz.asos.core.model.community.Comment
import uz.asos.core.model.community.PostDetail
import uz.asos.core.model.community.PostSort
import uz.asos.core.model.community.PostsPage
import uz.asos.core.model.community.VoteState

interface CommunityRepository {

    /** GET /v1/communities — the topic chips above the feed. */
    suspend fun getCommunities(): AsosResult<List<CommunityTopic>>

    /**
     * GET /v1/posts
     *
     * @param communitySlug topic slug, or null for every topic.
     * @param articleNumber only discussions about this article.
     */
    suspend fun getPosts(
        communitySlug: String? = null,
        sort: PostSort = PostSort.POPULAR,
        query: String? = null,
        articleNumber: Int? = null,
        page: Int = 1,
    ): AsosResult<PostsPage>

    /** GET /v1/posts/{id} */
    suspend fun getPost(id: Int): AsosResult<PostDetail>

    /** POST /v1/posts — a new question starts as `awaiting_lawyer`. */
    suspend fun createPost(
        title: String,
        body: String,
        communitySlug: String?,
        tags: List<String> = emptyList(),
        isAnonymous: Boolean = false,
        articleNumber: Int? = null,
    ): AsosResult<PostDetail>

    /** POST /v1/posts/{id}/vote — returns the new count and the user's vote. */
    suspend fun votePost(id: Int, vote: VoteState): AsosResult<VoteResult>

    /** POST /v1/comments/{id}/vote */
    suspend fun voteComment(id: Int, vote: VoteState): AsosResult<VoteResult>

    /** POST /v1/posts/{id}/comments */
    suspend fun addComment(
        postId: Int,
        body: String,
        parentId: Int? = null,
        isAnonymous: Boolean = false,
    ): AsosResult<Comment?>

    /** POST/DELETE /v1/communities/{slug}/join — returns the new membership state. */
    suspend fun setMembership(slug: String, join: Boolean): AsosResult<Boolean>
}

/** What a vote call gives back. */
data class VoteResult(
    val upvotes: Int,
    val myVote: VoteState,
)
