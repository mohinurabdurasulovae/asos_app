/*
 * FakeCommunityRepository.kt
 * Module: :core:testing   ·   Work package: WP-09
 *
 * In-memory CommunityRepository for ViewModel tests. Every result is a var, so a test can
 * swap in an error; calls are recorded for assertions. Sample content: CommunitySampleData.
 */
package uz.asos.core.testing.repository

import uz.asos.core.common.result.AsosResult
import uz.asos.core.data.repository.CommunityRepository
import uz.asos.core.data.repository.VoteResult
import uz.asos.core.model.community.Comment
import uz.asos.core.model.community.CommunityTopic
import uz.asos.core.model.community.PostDetail
import uz.asos.core.model.community.PostSort
import uz.asos.core.model.community.PostsPage
import uz.asos.core.model.community.VoteState
import uz.asos.core.testing.data.CommunitySampleData

class FakeCommunityRepository : CommunityRepository {

    /** null = filter CommunitySampleData the way the API would. */
    var postsResult: AsosResult<PostsPage>? = null
    var communitiesResult: AsosResult<List<CommunityTopic>> =
        AsosResult.Success(CommunitySampleData.communities)
    var postResult: AsosResult<PostDetail> = AsosResult.Success(CommunitySampleData.rentPostDetail)
    var createResult: AsosResult<PostDetail> = AsosResult.Success(CommunitySampleData.rentPostDetail)
    var voteResult: AsosResult<VoteResult>? = null
    var commentResult: AsosResult<Comment?>? = null
    var membershipResult: AsosResult<Boolean>? = null

    val requestedSorts = mutableListOf<PostSort>()
    val requestedCommunities = mutableListOf<String?>()
    val queries = mutableListOf<String?>()
    val postVotes = mutableListOf<Pair<Int, VoteState>>()
    val commentVotes = mutableListOf<Pair<Int, VoteState>>()
    val createdPosts = mutableListOf<String>()
    val addedComments = mutableListOf<Triple<Int, String, Int?>>()
    val memberships = mutableListOf<Pair<String, Boolean>>()
    /** Vote tallies the fake keeps, so a second vote behaves like the server's. */
    private val postTallies = mutableMapOf<Int, Pair<Int, VoteState>>()
    private val commentTallies = mutableMapOf<Int, Pair<Int, VoteState>>()

    var postsCalls = 0
        private set
    var postCalls = 0
        private set

    override suspend fun getCommunities(): AsosResult<List<CommunityTopic>> = communitiesResult

    override suspend fun getPosts(
        communitySlug: String?,
        sort: PostSort,
        query: String?,
        articleNumber: Int?,
        page: Int,
    ): AsosResult<PostsPage> {
        postsCalls++
        requestedSorts += sort
        requestedCommunities += communitySlug
        queries += query
        postsResult?.let { return it }

        val matching = CommunitySampleData.posts
            .filter { communitySlug == null || it.community?.slug == communitySlug }
            .filter { query.isNullOrBlank() || it.title.contains(query, ignoreCase = true) }
            .filter { articleNumber == null || it.article?.number == articleNumber }
            .let { posts ->
                when (sort) {
                    PostSort.POPULAR -> posts.sortedByDescending { it.upvotes }
                    PostSort.NEW -> posts.sortedByDescending { it.createdAt }
                    PostSort.UNANSWERED -> posts.filter { it.bestAnswer == null }
                    PostSort.SUBSCRIPTIONS -> posts.filter { it.community?.isMember == true }
                }
            }

        return AsosResult.Success(
            CommunitySampleData.feed.copy(
                posts = matching,
                page = page,
                total = matching.size,
                hasMore = false,
            ),
        )
    }

    override suspend fun getPost(id: Int): AsosResult<PostDetail> {
        postCalls++
        return postResult
    }

    override suspend fun createPost(
        title: String,
        body: String,
        communitySlug: String?,
        tags: List<String>,
        isAnonymous: Boolean,
        articleNumber: Int?,
    ): AsosResult<PostDetail> {
        createdPosts += title
        return createResult
    }

    override suspend fun votePost(id: Int, vote: VoteState): AsosResult<VoteResult> {
        postVotes += id to vote
        voteResult?.let { return it }

        val current = postTallies.getOrPut(id) {
            val post = CommunitySampleData.posts.first { it.id == id }
            post.upvotes to post.myVote
        }
        val tally = current.first + (vote.apiValue - current.second.apiValue) to vote
        postTallies[id] = tally
        return AsosResult.Success(VoteResult(upvotes = tally.first, myVote = tally.second))
    }

    override suspend fun voteComment(id: Int, vote: VoteState): AsosResult<VoteResult> {
        commentVotes += id to vote
        voteResult?.let { return it }

        val current = commentTallies.getOrPut(id) {
            (CommunitySampleData.rentPostDetail.comments.findById(id)?.upvotes ?: 0) to VoteState.NONE
        }
        val tally = current.first + (vote.apiValue - current.second.apiValue) to vote
        commentTallies[id] = tally
        return AsosResult.Success(VoteResult(upvotes = tally.first, myVote = tally.second))
    }

    override suspend fun addComment(
        postId: Int,
        body: String,
        parentId: Int?,
        isAnonymous: Boolean,
    ): AsosResult<Comment?> {
        addedComments += Triple(postId, body, parentId)
        return commentResult ?: AsosResult.Success(
            CommunitySampleData.lawyerAnswer.copy(
                id = NEW_COMMENT_ID,
                body = body,
                parentId = parentId,
                isBest = false,
                isLawyer = false,
                lawyer = null,
                upvotes = 0,
                isMine = true,
                author = if (isAnonymous) {
                    CommunitySampleData.anonymous
                } else {
                    CommunitySampleData.kamola
                },
            ),
        )
    }

    override suspend fun setMembership(slug: String, join: Boolean): AsosResult<Boolean> {
        memberships += slug to join
        return membershipResult ?: AsosResult.Success(join)
    }

    private companion object {
        const val NEW_COMMENT_ID = 99
    }
}

/** Finds a comment anywhere in the thread, including the nested replies. */
private fun List<Comment>.findById(id: Int): Comment? =
    firstNotNullOfOrNull { comment ->
        if (comment.id == id) comment else comment.replies.findById(id)
    }
