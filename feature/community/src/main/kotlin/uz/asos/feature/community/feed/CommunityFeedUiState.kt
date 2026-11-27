package uz.asos.feature.community.feed

import uz.asos.core.model.community.CommunityTopic
import uz.asos.core.model.community.Post
import uz.asos.core.model.community.PostSort

data class CommunityFeedUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isSignedIn: Boolean = false,
    val myInitials: String? = null,
    val articleNumber: Int? = null,
    val topics: List<CommunityTopic> = emptyList(),
    val selectedTopic: String? = null,
    val sort: PostSort = PostSort.POPULAR,
    val posts: List<Post> = emptyList(),
    val isAskSheetOpen: Boolean = false,
    val ask: AskDraft = AskDraft(),
    val notice: String? = null,
) {
    val isEmpty: Boolean get() = posts.isEmpty()
}

data class AskDraft(
    val title: String = "",
    val body: String = "",
    val topicSlug: String? = null,
    val isAnonymous: Boolean = true,
    val isSending: Boolean = false,
) {
    val canSend: Boolean
        get() = !isSending &&
            title.trim().length >= MIN_TITLE &&
            body.trim().length >= MIN_BODY

    companion object {
        const val MIN_TITLE = 10
        const val MIN_BODY = 20
    }
}
