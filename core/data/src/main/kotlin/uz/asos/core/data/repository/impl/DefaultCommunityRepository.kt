package uz.asos.core.data.repository.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import uz.asos.core.common.dispatcher.AsosDispatchers
import uz.asos.core.common.dispatcher.Dispatcher
import uz.asos.core.common.result.AsosResult
import uz.asos.core.common.result.getOrNull
import uz.asos.core.common.result.mapSuccess
import uz.asos.core.data.mapper.asModel
import uz.asos.core.data.repository.CommunityRepository
import uz.asos.core.data.repository.VoteResult
import uz.asos.core.model.community.Comment
import uz.asos.core.model.community.CommunityTopic
import uz.asos.core.model.community.PostDetail
import uz.asos.core.model.community.PostSort
import uz.asos.core.model.community.PostsPage
import uz.asos.core.model.community.VoteState
import uz.asos.core.network.ApiCaller
import uz.asos.core.network.api.CommunityApi
import uz.asos.core.network.dto.CreateCommentRequestDto
import uz.asos.core.network.dto.CreatePostRequestDto
import uz.asos.core.network.dto.VoteRequestDto
import javax.inject.Inject

internal class DefaultCommunityRepository @Inject constructor(
    private val communityApi: CommunityApi,
    private val apiCaller: ApiCaller,
    @Dispatcher(AsosDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : CommunityRepository {

    override suspend fun getCommunities(): AsosResult<List<CommunityTopic>> = io {
        apiCaller.call { communityApi.getCommunities() }
            .mapSuccess { response -> response.items.map { it.asModel() } }
    }

    override suspend fun getPosts(
        communitySlug: String?,
        sort: PostSort,
        query: String?,
        articleNumber: Int?,
        page: Int,
    ): AsosResult<PostsPage> = io {
        val communities = apiCaller.call { communityApi.getCommunities() }
            .mapSuccess { response -> response.items.map { it.asModel() } }
            .getOrNull()
            .orEmpty()

        apiCaller.call {
            communityApi.getPosts(
                community = communitySlug?.takeIf { it.isNotBlank() },
                sort = sort.apiValue,
                query = query?.takeIf { it.isNotBlank() },
                articleNumber = articleNumber,
                page = page.coerceAtLeast(1),
            )
        }.mapSuccess { it.asModel(communities) }
    }

    override suspend fun getPost(id: Int): AsosResult<PostDetail> = io {
        apiCaller.call { communityApi.getPost(id) }.mapSuccess { it.post.asModel() }
    }

    override suspend fun createPost(
        title: String,
        body: String,
        communitySlug: String?,
        tags: List<String>,
        isAnonymous: Boolean,
        articleNumber: Int?,
    ): AsosResult<PostDetail> = io {
        apiCaller.call {
            communityApi.createPost(
                CreatePostRequestDto(
                    title = title.trim(),
                    body = body.trim(),
                    community = communitySlug?.takeIf { it.isNotBlank() },
                    tags = tags,
                    isAnonymous = isAnonymous,
                    articleNumber = articleNumber,
                ),
            )
        }.mapSuccess { it.post.asModel() }
    }

    override suspend fun votePost(id: Int, vote: VoteState): AsosResult<VoteResult> = io {
        apiCaller.call { communityApi.votePost(id, VoteRequestDto(vote.apiValue)) }
            .mapSuccess { VoteResult(upvotes = it.upvotes, myVote = VoteState.fromApi(it.myVote)) }
    }

    override suspend fun voteComment(id: Int, vote: VoteState): AsosResult<VoteResult> = io {
        apiCaller.call { communityApi.voteComment(id, VoteRequestDto(vote.apiValue)) }
            .mapSuccess { VoteResult(upvotes = it.upvotes, myVote = VoteState.fromApi(it.myVote)) }
    }

    override suspend fun addComment(
        postId: Int,
        body: String,
        parentId: Int?,
        isAnonymous: Boolean,
    ): AsosResult<Comment?> = io {
        apiCaller.call {
            communityApi.addComment(
                postId = postId,
                body = CreateCommentRequestDto(
                    body = body.trim(),
                    parentId = parentId,
                    isAnonymous = isAnonymous,
                ),
            )
        }.mapSuccess { response -> response.comment?.asModel() }
    }

    override suspend fun setMembership(slug: String, join: Boolean): AsosResult<Boolean> = io {
        if (join) {
            apiCaller.call { communityApi.joinCommunity(slug) }.mapSuccess { it.isMember }
        } else {
            apiCaller.call { communityApi.leaveCommunity(slug) }.mapSuccess { it.isMember }
        }
    }

    private suspend fun <T> io(block: suspend () -> T): T = withContext(ioDispatcher) { block() }
}
