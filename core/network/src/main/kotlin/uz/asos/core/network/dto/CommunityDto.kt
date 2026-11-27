/*
 * CommunityDto.kt
 * Module: :core:network   ·   Work package: WP-02
 *
 * Wire shapes of the Hamjamiyat tag in docs/api/openapi.yaml.
 */
package uz.asos.core.network.dto

import kotlinx.serialization.Serializable

/** GET /v1/posts */
@Serializable
data class PostsResponseDto(
    val ok: Boolean = true,
    val items: List<PostBriefDto> = emptyList(),
    val meta: ListMetaDto? = null,
    /** Echo of the sort actually applied. */
    val sort: String? = null,
)

/** GET /v1/communities */
@Serializable
data class CommunitiesResponseDto(
    val ok: Boolean = true,
    val items: List<CommunityDto> = emptyList(),
)

@Serializable
data class CommunityDto(
    val id: Int = 0,
    val slug: String = "",
    val title: String = "",
    val description: String = "",
    val tone: String? = null,
    val memberCount: Int? = null,
    val postCount: Int = 0,
    val isMember: Boolean = false,
)

@Serializable
data class AuthorDto(
    val id: Int? = null,
    val name: String = "",
    val username: String? = null,
    val initials: String = "",
    val avatarUrl: String? = null,
    val isAnonymous: Boolean = false,
    val isLawyer: Boolean = false,
    val lawyerId: Int? = null,
    val isVerified: Boolean = false,
)

@Serializable
data class BestAnswerDto(
    val id: Int = 0,
    val excerpt: String = "",
    val isLawyer: Boolean = false,
    val authorName: String = "",
)

@Serializable
data class PostBriefDto(
    val id: Int,
    val title: String = "",
    val excerpt: String = "",
    val tags: List<String> = emptyList(),
    val upvotes: Int = 0,
    val commentCount: Int = 0,
    /** open · awaiting_lawyer · answered */
    val status: String? = null,
    val createdAt: String = "",
    val author: AuthorDto = AuthorDto(),
    val community: CommunityDto? = null,
    val article: ArticleRefDto? = null,
    val bestAnswer: BestAnswerDto? = null,
    /** +1, 0 or -1. */
    val myVote: Int = 0,
    val isSaved: Boolean = false,
)

/** GET /v1/posts/{id} */
@Serializable
data class PostResponseDto(
    val ok: Boolean = true,
    val post: PostDto,
)

@Serializable
data class PostDto(
    val id: Int,
    val title: String = "",
    val excerpt: String = "",
    val body: String = "",
    val tags: List<String> = emptyList(),
    val upvotes: Int = 0,
    val commentCount: Int = 0,
    val status: String? = null,
    val createdAt: String = "",
    val author: AuthorDto = AuthorDto(),
    val community: CommunityDto? = null,
    val article: ArticleRefDto? = null,
    val bestAnswer: BestAnswerDto? = null,
    val myVote: Int = 0,
    val isSaved: Boolean = false,
    val isMine: Boolean = false,
    val disclaimer: String? = null,
    val comments: List<CommentDto> = emptyList(),
)

@Serializable
data class LawyerBadgeDto(
    val specialties: List<String> = emptyList(),
    val years: Int = 0,
)

@Serializable
data class CommentDto(
    val id: Int,
    val parentId: Int? = null,
    val body: String = "",
    val isDeleted: Boolean = false,
    val upvotes: Int = 0,
    val isBest: Boolean = false,
    val isLawyer: Boolean = false,
    val lawyer: LawyerBadgeDto? = null,
    val replyCount: Int = 0,
    val createdAt: String = "",
    val author: AuthorDto = AuthorDto(),
    val article: ArticleRefDto? = null,
    val isMine: Boolean = false,
    /** One nested level; the API lifts anything deeper to the root. */
    val replies: List<CommentDto> = emptyList(),
)

/** POST /v1/posts */
@Serializable
data class CreatePostRequestDto(
    val title: String,
    val body: String,
    val community: String? = null,
    val tags: List<String> = emptyList(),
    val isAnonymous: Boolean = false,
    val articleNumber: Int? = null,
)

/** POST /v1/posts/{id}/comments */
@Serializable
data class CreateCommentRequestDto(
    val body: String,
    val parentId: Int? = null,
    val isAnonymous: Boolean = false,
)

@Serializable
data class CommentResponseDto(
    val ok: Boolean = true,
    val comment: CommentDto? = null,
)

/** POST /v1/posts/{id}/vote and the comment vote. */
@Serializable
data class VoteRequestDto(
    /** +1, 0 or -1. */
    val value: Int,
)

@Serializable
data class VoteResponseDto(
    val ok: Boolean = true,
    val upvotes: Int = 0,
    val myVote: Int = 0,
)

/** POST /v1/communities/{slug}/join */
@Serializable
data class JoinResponseDto(
    val ok: Boolean = true,
    val isMember: Boolean = false,
    val memberCount: Int? = null,
)
