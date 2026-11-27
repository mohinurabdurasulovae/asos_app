package uz.asos.feature.community.post

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.asos.core.common.result.AsosResult
import uz.asos.core.data.repository.AuthRepository
import uz.asos.core.data.repository.CommunityRepository
import uz.asos.core.model.community.Comment
import uz.asos.core.model.community.PostDetail
import uz.asos.core.model.community.VoteState
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val communityRepository: CommunityRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val postId: Int = checkNotNull(savedStateHandle.get<Int>(KEY_ARG)) {
        "PostDetailViewModel needs the \"$KEY_ARG\" navigation argument"
    }

    private val _uiState = MutableStateFlow(PostDetailUiState())
    val uiState: StateFlow<PostDetailUiState> = _uiState.asStateFlow()

    init {
        load()
        observeSession()
    }

    fun onAction(action: PostDetailAction) {
        when (action) {
            is PostDetailAction.OnPostVote -> votePost(action.vote)
            is PostDetailAction.OnCommentVote -> voteComment(action.commentId, action.vote)
            PostDetailAction.OnMembershipToggle -> toggleMembership()
            is PostDetailAction.OnReplyTo -> _uiState.update {
                it.copy(
                    reply = it.reply.copy(
                        parentId = action.commentId,
                        parentAuthor = action.authorName,
                    ),
                )
            }
            is PostDetailAction.OnReplyChange ->
                _uiState.update { it.copy(reply = it.reply.copy(body = action.body)) }
            is PostDetailAction.OnReplyAnonymousChange ->
                _uiState.update { it.copy(reply = it.reply.copy(isAnonymous = action.isAnonymous)) }
            PostDetailAction.OnReplySend -> sendReply()
            PostDetailAction.OnNoticeDismiss -> _uiState.update { it.copy(notice = null) }
            PostDetailAction.OnRetry -> load()
            PostDetailAction.OnSaveToggle -> _uiState.update { it.copy(notice = NOT_READY) }
            PostDetailAction.OnShareClick -> Unit
            is PostDetailAction.OnArticleClick,
            is PostDetailAction.OnWriteToLawyer,
            PostDetailAction.OnSignInClick,
            PostDetailAction.OnBackClick,
            -> Unit
        }
    }

    private fun observeSession() {
        viewModelScope.launch {
            authRepository.isSignedIn.collect { signedIn ->
                _uiState.update { it.copy(isSignedIn = signedIn) }
            }
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = communityRepository.getPost(postId)) {
                is AsosResult.Success -> _uiState.update { it.withPost(result.data) }
                is AsosResult.Error -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = if (it.post == null) result.message else null,
                        notice = if (it.post != null) result.message else null,
                    )
                }
            }
        }
    }

    private fun votePost(vote: VoteState) {
        val post = _uiState.value.post ?: return
        if (!requireSignIn()) return

        val current = post.post
        val next = if (current.myVote == vote) VoteState.NONE else vote
        val delta = next.apiValue - current.myVote.apiValue
        patchPost { it.copy(myVote = next, upvotes = it.upvotes + delta) }

        viewModelScope.launch {
            when (val result = communityRepository.votePost(current.id, next)) {
                is AsosResult.Success ->
                    patchPost { it.copy(myVote = result.data.myVote, upvotes = result.data.upvotes) }
                is AsosResult.Error -> {
                    patchPost { it.copy(myVote = current.myVote, upvotes = current.upvotes) }
                    _uiState.update { it.copy(notice = result.message) }
                }
            }
        }
    }

    private fun voteComment(commentId: Int, vote: VoteState) {
        if (!requireSignIn()) return
        val before = _uiState.value.findComment(commentId) ?: return

        patchComment(commentId) { it.copy(upvotes = it.upvotes + vote.apiValue) }

        viewModelScope.launch {
            when (val result = communityRepository.voteComment(commentId, vote)) {
                is AsosResult.Success ->
                    patchComment(commentId) { it.copy(upvotes = result.data.upvotes) }
                is AsosResult.Error -> {
                    patchComment(commentId) { it.copy(upvotes = before.upvotes) }
                    _uiState.update { it.copy(notice = result.message) }
                }
            }
        }
    }

    private fun toggleMembership() {
        val community = _uiState.value.post?.post?.community ?: return
        if (!requireSignIn()) return

        viewModelScope.launch {
            val result = communityRepository.setMembership(community.slug, !community.isMember)
            when (result) {
                is AsosResult.Success -> _uiState.update { state ->
                    val post = state.post ?: return@update state
                    state.copy(
                        post = post.copy(
                            post = post.post.copy(
                                community = community.copy(isMember = result.data),
                            ),
                        ),
                    )
                }
                is AsosResult.Error -> _uiState.update { it.copy(notice = result.message) }
            }
        }
    }

    private fun sendReply() {
        val draft = _uiState.value.reply
        if (!draft.canSend) return
        if (!requireSignIn()) return

        viewModelScope.launch {
            _uiState.update { it.copy(reply = it.reply.copy(isSending = true)) }
            val result = communityRepository.addComment(
                postId = postId,
                body = draft.body,
                parentId = draft.parentId,
                isAnonymous = draft.isAnonymous,
            )
            when (result) {
                is AsosResult.Success -> {
                    _uiState.update { it.copy(reply = ReplyDraft(isAnonymous = draft.isAnonymous)) }
                    load()
                }
                is AsosResult.Error -> _uiState.update {
                    it.copy(reply = it.reply.copy(isSending = false), notice = result.message)
                }
            }
        }
    }

    private fun requireSignIn(): Boolean {
        if (_uiState.value.isSignedIn) return true
        _uiState.update { it.copy(notice = SIGN_IN_FIRST) }
        return false
    }

    private fun patchPost(transform: (uz.asos.core.model.community.Post) -> uz.asos.core.model.community.Post) {
        _uiState.update { state ->
            val post = state.post ?: return@update state
            state.copy(post = post.copy(post = transform(post.post)))
        }
    }

    private fun patchComment(commentId: Int, transform: (Comment) -> Comment) {
        _uiState.update { state ->
            state.copy(
                bestAnswer = state.bestAnswer?.patch(commentId, transform),
                comments = state.comments.map { it.patch(commentId, transform) },
            )
        }
    }

    private companion object {
        const val KEY_ARG = "postId"
        const val SIGN_IN_FIRST = "Buning uchun tizimga kiring."
        const val NOT_READY = "Bu amal hali ulanmagan."
    }
}

private fun PostDetailUiState.withPost(detail: PostDetail): PostDetailUiState {
    val best = detail.comments.firstOrNull { it.isBest }
    return copy(
        isLoading = false,
        errorMessage = null,
        post = detail,
        bestAnswer = best,
        comments = detail.comments.filterNot { it.id == best?.id },
    )
}

private fun PostDetailUiState.findComment(id: Int): Comment? =
    (listOfNotNull(bestAnswer) + comments).firstNotNullOfOrNull { it.find(id) }

private fun Comment.find(id: Int): Comment? =
    if (this.id == id) this else replies.firstNotNullOfOrNull { it.find(id) }

private fun Comment.patch(id: Int, transform: (Comment) -> Comment): Comment = when {
    this.id == id -> transform(this)
    replies.isEmpty() -> this
    else -> copy(replies = replies.map { it.patch(id, transform) })
}
