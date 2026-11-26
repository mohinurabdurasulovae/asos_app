/*
 * PostDetailUiState.kt
 * Module: :feature:community   ·   Work package: WP-09
 * Screen: Muhokama (design artboard: Post.dc.html)
 *
 * Immutable state the screen renders: Loading / Success(data) / Error. Everything
 * PostDetailScreen needs, already formatted.
 *
 * PLACEHOLDER: not implemented yet.
 */

package uz.asos.feature.community.post

data class PostDetailUi(
    val id: Int,
    val communityName: String,
    val membersCountLabel: String,
    val isJoined: Boolean,
    val authorLabel: String,
    val timeAgoLabel: String,
    val title: String,
    val body: String,
    val tags: List<String>,
    val votes: Int,
    val isUpvoted: Boolean,
    val commentCount: Int,
    val isBookmarked: Boolean,
)

data class ReplyItemUi(
    val id: String,
    val authorName: String,
    val initials: String,
    val metaLabel: String,
    val content: String,
    val likes: Int,
    val isLiked: Boolean,
    val isBestAnswer: Boolean = false,
    val isVerifiedLawyer: Boolean = false,
    val roleLabel: String? = null,
    val articleNumber: String? = null,
    val articleTitle: String? = null,
    val articleKey: String? = null,
    val lawyerChatId: Int? = null,
)

sealed interface PostDetailUiState {
    data object Loading : PostDetailUiState

    data class Error(
        val message: String,
    ) : PostDetailUiState

    data class Success(
        val post: PostDetailUi,
        val replies: List<ReplyItemUi>,
        val replyDraft: String = "",
        val isAnonymousReply: Boolean = false,
    ) : PostDetailUiState
}
