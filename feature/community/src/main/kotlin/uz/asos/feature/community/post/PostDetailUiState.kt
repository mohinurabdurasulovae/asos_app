/*
 * PostDetailUiState.kt
 * Module: :feature:community   ·   Work package: WP-09
 * Screen: Muhokama (design artboard: Post.dc.html)
 *
 * Everything PostDetailScreen draws. No Android UI types.
 */
package uz.asos.feature.community.post

import uz.asos.core.model.community.Comment
import uz.asos.core.model.community.PostDetail

data class PostDetailUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isSignedIn: Boolean = false,
    val post: PostDetail? = null,
    /** The verified lawyer's answer, lifted out of [PostDetail.comments]. */
    val bestAnswer: Comment? = null,
    /** Everything except the best answer, in the order the API sent it. */
    val comments: List<Comment> = emptyList(),
    val reply: ReplyDraft = ReplyDraft(),
    val notice: String? = null,
) {
    val commentCount: Int get() = post?.post?.commentCount ?: 0
}

/** The composer pinned to the bottom. */
data class ReplyDraft(
    val body: String = "",
    /** Non-null while answering one comment instead of the post. */
    val parentId: Int? = null,
    /** Shown above the field: "<name> ga javob". */
    val parentAuthor: String? = null,
    val isAnonymous: Boolean = true,
    val isSending: Boolean = false,
) {
    val canSend: Boolean get() = !isSending && body.trim().length >= MIN_BODY

    companion object {
        const val MIN_BODY = 2
    }
}
