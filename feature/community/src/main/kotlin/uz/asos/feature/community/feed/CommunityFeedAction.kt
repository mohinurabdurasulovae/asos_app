package uz.asos.feature.community.feed

import uz.asos.core.model.community.PostSort
import uz.asos.core.model.community.VoteState

sealed interface CommunityFeedAction {
    data class OnTopicSelect(val slug: String?) : CommunityFeedAction

    data class OnSortSelect(val sort: PostSort) : CommunityFeedAction

    data class OnVote(val postId: Int, val vote: VoteState) : CommunityFeedAction

    data class OnPostClick(val postId: Int) : CommunityFeedAction

    data object OnSearchClick : CommunityFeedAction

    data object OnSavedClick : CommunityFeedAction

    data object OnAskOpen : CommunityFeedAction

    data object OnAskDismiss : CommunityFeedAction

    data class OnAskTitleChange(val title: String) : CommunityFeedAction

    data class OnAskBodyChange(val body: String) : CommunityFeedAction

    data class OnAskTopicChange(val slug: String?) : CommunityFeedAction

    data class OnAskAnonymousChange(val isAnonymous: Boolean) : CommunityFeedAction

    data object OnAskSend : CommunityFeedAction

    data object OnSignInClick : CommunityFeedAction

    data object OnNoticeDismiss : CommunityFeedAction

    data object OnRetry : CommunityFeedAction

    data object OnResume : CommunityFeedAction
}
