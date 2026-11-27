package uz.asos.feature.legalhelp.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import uz.asos.core.common.result.AsosErrorCode
import uz.asos.core.common.result.AsosResult
import uz.asos.core.common.text.UzbekFormatters
import uz.asos.core.data.repository.LegalHelpRepository
import uz.asos.core.model.legalhelp.ChatMessage
import uz.asos.core.model.legalhelp.ConversationDetail
import uz.asos.core.model.legalhelp.Lawyer
import uz.asos.core.model.legalhelp.MessageAttachment
import uz.asos.feature.legalhelp.ui.AvatarTone
import uz.asos.feature.legalhelp.ui.LegalHelpFormat
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: LegalHelpRepository,
) : ViewModel() {

    private val conversationId: Int = checkNotNull(savedStateHandle.get<Int>(CHAT_ID_ARG)) {
        "ChatViewModel needs the \"$CHAT_ID_ARG\" navigation argument"
    }

    private val _uiState = MutableStateFlow(ChatUiState(today = LegalHelpFormat.today().toString()))
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null
    private var pollJob: Job? = null
    private var lastMarkedReadId: Int? = null

    init {
        load(showSkeleton = true)
    }

    fun onAction(action: ChatAction) {
        when (action) {
            is ChatAction.OnInputChange -> _uiState.update { it.copy(input = action.text) }
            ChatAction.OnSend -> send(_uiState.value.input, fromInput = true)
            is ChatAction.OnQuickReply -> send(action.text, fromInput = false)
            ChatAction.OnRetry -> load(showSkeleton = true)
            ChatAction.OnResume -> startPolling()
            ChatAction.OnPause -> {
                pollJob?.cancel()
                pollJob = null
            }
            ChatAction.OnSendErrorShown -> _uiState.update { it.copy(sendError = null) }
            ChatAction.OnBackClick,
            ChatAction.OnSignInClick,
            is ChatAction.OnArticleClick,
            is ChatAction.OnFileClick,
            ChatAction.OnCallClick,
            ChatAction.OnVideoCallClick,
            ChatAction.OnAttachClick,
            ChatAction.OnVoiceClick,
            -> Unit
        }
    }

    private fun load(showSkeleton: Boolean) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch { refresh(showSkeleton) }
    }

    private suspend fun refresh(showSkeleton: Boolean) {
        if (showSkeleton) _uiState.update { it.copy(isLoading = true, errorMessage = null, needsSignIn = false) }
        when (val result = repository.getConversation(conversationId)) {
            is AsosResult.Success -> {
                apply(result.data)
                markReadIfNeeded(result.data)
            }
            is AsosResult.Error -> _uiState.update {
                when {
                    result.code == AsosErrorCode.UNAUTHORIZED -> it.copy(isLoading = false, needsSignIn = true)
                    !showSkeleton && it.lawyer != null -> it
                    else -> it.copy(isLoading = false, errorMessage = result.message)
                }
            }
        }
    }

    private fun apply(detail: ConversationDetail) {
        val loaded = detail.messages.map { it.toUi() }
        _uiState.update { state ->
            val loadedIds = loaded.mapTo(HashSet()) { it.id }
            val pending = state.messages.filter { it.fromMe && it.id !in loadedIds && it.id > (loaded.lastOrNull()?.id ?: 0) }
            state.copy(
                isLoading = false,
                errorMessage = null,
                needsSignIn = false,
                lawyer = detail.conversation.lawyer?.toChatUi() ?: state.lawyer,
                messages = loaded + pending,
                today = LegalHelpFormat.today().toString(),
            )
        }
    }

    private suspend fun markReadIfNeeded(detail: ConversationDetail) {
        val lastTheirs = detail.messages.lastOrNull { !it.isMine } ?: return
        if (lastTheirs.readAt != null || lastMarkedReadId == lastTheirs.id) return
        if (repository.markRead(conversationId) is AsosResult.Success) lastMarkedReadId = lastTheirs.id
    }

    private fun startPolling() {
        if (pollJob?.isActive == true) return
        pollJob = viewModelScope.launch {
            while (isActive) {
                delay(POLL_MS)
                if (_uiState.value.lawyer != null && loadJob?.isActive != true) refresh(showSkeleton = false)
            }
        }
    }

    private fun send(text: String, fromInput: Boolean) {
        val trimmed = text.trim()
        val state = _uiState.value
        if (trimmed.isEmpty() || state.isSending || state.lawyer == null) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSending = true, sendError = null, input = if (fromInput) "" else it.input) }
            when (val result = repository.sendMessage(conversationId, trimmed)) {
                is AsosResult.Success -> _uiState.update {
                    val message = result.data.toUi()
                    it.copy(isSending = false, messages = it.messages.filter { m -> m.id != message.id } + message)
                }
                is AsosResult.Error -> _uiState.update {
                    it.copy(
                        isSending = false,
                        input = if (fromInput && it.input.isEmpty()) trimmed else it.input,
                        sendError = result.message,
                        needsSignIn = result.code == AsosErrorCode.UNAUTHORIZED,
                    )
                }
            }
        }
    }

    internal companion object {
        const val CHAT_ID_ARG = "chatId"
        const val POLL_MS = 8_000L
    }
}

private fun Lawyer.toChatUi() = ChatLawyerUi(
    name = UzbekFormatters.display(name),
    initials = initials,
    isOnline = isOnline,
    isVerified = isVerified,
    avatarTone = AvatarTone.forId(id),
)

internal fun ChatMessage.toUi(): ChatMessageUi {
    val date = LegalHelpFormat.date(createdAt)
    return ChatMessageUi(
        id = id,
        fromMe = isMine,
        text = UzbekFormatters.display(body),
        time = LegalHelpFormat.time(createdAt),
        day = date?.toString().orEmpty(),
        dayLabel = date?.let { LegalHelpFormat.fullDate(it) }.orEmpty(),
        isRead = readAt != null,
        files = attachments.filterIsInstance<MessageAttachment.File>().map {
            ChatFileUi(name = it.fileName, meta = LegalHelpFormat.fileMeta(it.bytes, it.fileName, it.mime), url = it.url)
        },
        articles = attachments.filterIsInstance<MessageAttachment.ArticleLink>().map {
            ChatArticleUi(number = it.number, title = UzbekFormatters.display(it.title))
        },
    )
}
