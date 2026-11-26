/*
 * ChatViewModel.kt
 * Module: :feature:legalhelp   ·   Work package: WP-10
 * Screen: Advokat bilan chat (design artboard: Chat.dc.html)
 *
 * Holds StateFlow<ChatUiState> and handles ChatAction. No Android UI types.
 *
 * API used (through the repository, see docs/api/openapi.yaml):
 *  - GET  /chats/{chatId}
 *  - GET  /chats/{chatId}/messages
 *  - POST /chats/{chatId}/messages
 */
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
import kotlinx.coroutines.launch
import uz.asos.core.common.result.AsosResult
import uz.asos.feature.legalhelp.data.temp.ChatMessage
import uz.asos.feature.legalhelp.data.temp.Lawyer
import uz.asos.feature.legalhelp.data.temp.TempLegalHelpRepository
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    // TODO(WP-02): switch to uz.asos.core.data.repository.LegalHelpRepository.
    private val repository: TempLegalHelpRepository,
) : ViewModel() {

    /**
     * ChatRoute(chatId) stores its argument under "chatId". Read directly (instead of
     * toRoute()) so the ViewModel stays testable on the plain JVM.
     */
    private val chatId: Int = checkNotNull(savedStateHandle.get<Int>(CHAT_ID_ARG)) {
        "ChatViewModel needs the \"$CHAT_ID_ARG\" navigation argument"
    }

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null
    private var replyJob: Job? = null

    init {
        load()
    }

    fun onAction(action: ChatAction) {
        when (action) {
            is ChatAction.OnInputChange -> _uiState.update { it.copy(input = action.text) }
            ChatAction.OnSend -> send(_uiState.value.input, clearInput = true)
            is ChatAction.OnQuickReply -> send(action.text, clearInput = false)
            ChatAction.OnRetry -> load()
            // Navigation and system actions are handled by ChatRoute.
            ChatAction.OnBackClick,
            is ChatAction.OnArticleClick,
            is ChatAction.OnFileClick,
            ChatAction.OnCallClick,
            ChatAction.OnVideoCallClick,
            ChatAction.OnAttachClick,
            ChatAction.OnVoiceClick,
            -> Unit
        }
    }

    private fun load() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = repository.getChat(chatId)) {
                is AsosResult.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        lawyer = result.data.lawyer.toChatUi(),
                        messages = result.data.messages.map { message -> message.toUi() },
                    )
                }
                is AsosResult.Error -> _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
            }
        }
    }

    private fun send(text: String, clearInput: Boolean) {
        val trimmed = text.trim()
        if (trimmed.isEmpty() || _uiState.value.isSending || _uiState.value.lawyer == null) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSending = true, input = if (clearInput) "" else it.input) }
            when (val result = repository.sendMessage(chatId, trimmed)) {
                is AsosResult.Success -> {
                    _uiState.update { it.copy(isSending = false, messages = it.messages + result.data.toUi()) }
                    awaitReply(trimmed)
                }
                // Put the text back so the user doesn't lose it.
                is AsosResult.Error -> _uiState.update {
                    it.copy(isSending = false, input = if (clearInput) trimmed else it.input)
                }
            }
        }
    }

    /** Mock: shows "… yozmoqda" for a moment, then the lawyer's canned answer. */
    private fun awaitReply(text: String) {
        replyJob?.cancel()
        replyJob = viewModelScope.launch {
            _uiState.update { it.copy(isLawyerTyping = true) }
            delay(TYPING_MS)
            val reply = repository.awaitLawyerReply(chatId, text)
            _uiState.update { state ->
                state.copy(
                    isLawyerTyping = false,
                    messages = if (reply is AsosResult.Success) state.messages + reply.data.toUi() else state.messages,
                )
            }
        }
    }

    companion object {
        internal const val CHAT_ID_ARG = "chatId"
        internal const val TYPING_MS = 1_500L
    }
}

private fun Lawyer.toChatUi() = ChatLawyerUi(
    name = fullName,
    firstName = fullName.substringBefore(' '),
    initials = initials,
    isOnline = isOnline,
    isVerified = isVerified,
    avatarTone = avatarTone,
)

private fun ChatMessage.toUi() = ChatMessageUi(
    id = id,
    fromMe = fromMe,
    text = text,
    time = time,
    isRead = isRead,
    file = file?.let { ChatFileUi(name = it.fileName, meta = it.meta) },
    article = article?.let { ChatArticleUi(number = it.articleNumber, title = it.title) },
)
