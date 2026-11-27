/*
 * CommunityFeedViewModel.kt
 * Module: :feature:community   ·   Work package: WP-09
 * Screen: Hamjamiyat (design artboard: Hamjamiyat.dc.html)
 *
 * Loads the feed with its topic chips, applies the topic filter and the sort tabs, votes on a
 * post without waiting for a reload, and sends a new question.
 */
package uz.asos.feature.community.feed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.asos.core.common.result.AsosResult
import uz.asos.core.data.repository.AuthRepository
import uz.asos.core.data.repository.CommunityRepository
import uz.asos.core.model.community.PostSort
import uz.asos.core.model.community.VoteState
import javax.inject.Inject

@HiltViewModel
class CommunityFeedViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val communityRepository: CommunityRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    /** CommunityRoute(articleNumber): 0 = the whole feed. */
    private val articleNumber: Int? =
        savedStateHandle.get<Int>(KEY_ARG)?.takeIf { it > 0 }

    private val _uiState = MutableStateFlow(CommunityFeedUiState(articleNumber = articleNumber))
    val uiState: StateFlow<CommunityFeedUiState> = _uiState.asStateFlow()

    private var hasLoaded = false
    private var loadJob: Job? = null

    init {
        load(showSkeleton = true)
        observeSession()
    }

    fun onAction(action: CommunityFeedAction) {
        when (action) {
            is CommunityFeedAction.OnTopicSelect -> {
                _uiState.update { it.copy(selectedTopic = action.slug) }
                load(showSkeleton = false)
            }
            is CommunityFeedAction.OnSortSelect -> {
                _uiState.update { it.copy(sort = action.sort) }
                load(showSkeleton = false)
            }
            is CommunityFeedAction.OnVote -> vote(action.postId, action.vote)
            CommunityFeedAction.OnAskOpen -> _uiState.update { it.copy(isAskSheetOpen = true) }
            CommunityFeedAction.OnAskDismiss -> _uiState.update { it.copy(isAskSheetOpen = false) }
            is CommunityFeedAction.OnAskTitleChange ->
                _uiState.update { it.copy(ask = it.ask.copy(title = action.title)) }
            is CommunityFeedAction.OnAskBodyChange ->
                _uiState.update { it.copy(ask = it.ask.copy(body = action.body)) }
            is CommunityFeedAction.OnAskTopicChange ->
                _uiState.update { it.copy(ask = it.ask.copy(topicSlug = action.slug)) }
            is CommunityFeedAction.OnAskAnonymousChange ->
                _uiState.update { it.copy(ask = it.ask.copy(isAnonymous = action.isAnonymous)) }
            CommunityFeedAction.OnAskSend -> send()
            CommunityFeedAction.OnNoticeDismiss -> _uiState.update { it.copy(notice = null) }
            CommunityFeedAction.OnRetry -> load(showSkeleton = true)
            CommunityFeedAction.OnResume -> if (hasLoaded) load(showSkeleton = false)
            // Navigation is handled by CommunityFeedRoute.
            is CommunityFeedAction.OnPostClick,
            CommunityFeedAction.OnSearchClick,
            CommunityFeedAction.OnSavedClick,
            CommunityFeedAction.OnSignInClick,
            -> Unit
        }
    }

    private fun observeSession() {
        viewModelScope.launch {
            authRepository.isSignedIn.collect { signedIn ->
                val changed = _uiState.value.isSignedIn != signedIn
                _uiState.update { it.copy(isSignedIn = signedIn) }
                if (changed && hasLoaded) load(showSkeleton = false)
            }
        }
    }

    private fun load(showSkeleton: Boolean) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            if (showSkeleton) _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val state = _uiState.value

            val result = communityRepository.getPosts(
                communitySlug = state.selectedTopic,
                sort = state.sort,
                articleNumber = state.articleNumber,
            )
            when (result) {
                is AsosResult.Success -> {
                    hasLoaded = true
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            posts = result.data.posts,
                            // The chips come with the feed; keep the old ones if empty.
                            topics = result.data.communities.ifEmpty { it.topics },
                        )
                    }
                }
                is AsosResult.Error -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = if (it.posts.isEmpty()) result.message else null,
                    )
                }
            }
        }
    }

    /**
     * Moves the arrow and the count straight away, then asks the server; a failure puts the
     * old numbers back, so the row never lies about what the server stored.
     */
    private fun vote(postId: Int, vote: VoteState) {
        val current = _uiState.value.posts.firstOrNull { it.id == postId } ?: return
        if (!_uiState.value.isSignedIn) {
            _uiState.update { it.copy(notice = SIGN_IN_TO_VOTE) }
            return
        }

        val next = if (current.myVote == vote) VoteState.NONE else vote
        val delta = next.apiValue - current.myVote.apiValue
        patchPost(postId) { it.copy(myVote = next, upvotes = it.upvotes + delta) }

        viewModelScope.launch {
            when (val result = communityRepository.votePost(postId, next)) {
                is AsosResult.Success -> patchPost(postId) {
                    it.copy(myVote = result.data.myVote, upvotes = result.data.upvotes)
                }
                is AsosResult.Error -> {
                    patchPost(postId) { it.copy(myVote = current.myVote, upvotes = current.upvotes) }
                    _uiState.update { it.copy(notice = result.message) }
                }
            }
        }
    }

    private fun send() {
        val draft = _uiState.value.ask
        if (!draft.canSend) return

        viewModelScope.launch {
            _uiState.update { it.copy(ask = it.ask.copy(isSending = true)) }
            val result = communityRepository.createPost(
                title = draft.title,
                body = draft.body,
                communitySlug = draft.topicSlug,
                isAnonymous = draft.isAnonymous,
                articleNumber = _uiState.value.articleNumber,
            )
            when (result) {
                is AsosResult.Success -> {
                    _uiState.update {
                        it.copy(isAskSheetOpen = false, ask = AskDraft(), notice = ASK_SENT)
                    }
                    load(showSkeleton = false)
                }
                is AsosResult.Error -> _uiState.update {
                    it.copy(ask = it.ask.copy(isSending = false), notice = result.message)
                }
            }
        }
    }

    private fun patchPost(postId: Int, transform: (uz.asos.core.model.community.Post) -> uz.asos.core.model.community.Post) {
        _uiState.update { state ->
            state.copy(
                posts = state.posts.map { post -> if (post.id == postId) transform(post) else post },
            )
        }
    }

    private companion object {
        const val KEY_ARG = "articleNumber"
        const val SIGN_IN_TO_VOTE = "Ovoz berish uchun tizimga kiring."
        const val ASK_SENT = "Savolingiz yuborildi. Yurist javobini kuting."
    }
}
