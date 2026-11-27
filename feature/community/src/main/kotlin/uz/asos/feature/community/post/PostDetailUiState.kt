package uz.asos.feature.community.post

import uz.asos.core.model.community.Comment
import uz.asos.core.model.community.PostDetail

data class PostDetailUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isSignedIn: Boolean = false,
    val post: PostDetail? = null,
    val bestAnswer: Comment? = null,
    val comments: List<Comment> = emptyList(),
    val reply: ReplyDraft = ReplyDraft(),
    val notice: String? = null,
) {
    val commentCount: Int get() = post?.post?.commentCount ?: 0
}

data class ReplyDraft(
    val body: String = "",
    val parentId: Int? = null,
    val parentAuthor: String? = null,
    val isAnonymous: Boolean = true,
    val isSending: Boolean = false,
) {
    val canSend: Boolean get() = !isSending && body.trim().length >= MIN_BODY

    companion object {
        const val MIN_BODY = 2
    }
}
