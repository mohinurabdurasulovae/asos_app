package uz.asos.core.data.repository

import uz.asos.core.common.result.AsosResult
import uz.asos.core.model.community.CommunityTopic
import uz.asos.core.model.community.Comment
import uz.asos.core.model.community.PostDetail
import uz.asos.core.model.community.PostSort
import uz.asos.core.model.community.PostsPage
import uz.asos.core.model.community.VoteState

interface CommunityRepository {

    suspend fun getCommunities(): AsosResult<List<CommunityTopic>>

    suspend fun getPosts(
        communitySlug: String? = null,
        sort: PostSort = PostSort.POPULAR,
        query: String? = null,
        articleNumber: Int? = null,
        page: Int = 1,
    ): AsosResult<PostsPage>

    suspend fun getPost(id: Int): AsosResult<PostDetail>

    suspend fun createPost(
        title: String,
        body: String,
        communitySlug: String?,
        tags: List<String> = emptyList(),
        isAnonymous: Boolean = false,
        articleNumber: Int? = null,
    ): AsosResult<PostDetail>

    suspend fun votePost(id: Int, vote: VoteState): AsosResult<VoteResult>

    suspend fun voteComment(id: Int, vote: VoteState): AsosResult<VoteResult>

    suspend fun addComment(
        postId: Int,
        body: String,
        parentId: Int? = null,
        isAnonymous: Boolean = false,
    ): AsosResult<Comment?>

    suspend fun setMembership(slug: String, join: Boolean): AsosResult<Boolean>
}

data class VoteResult(
    val upvotes: Int,
    val myVote: VoteState,
)
