package uz.asos.feature.legalhelp.chat

import uz.asos.feature.legalhelp.ui.AvatarTone

data class ChatUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val needsSignIn: Boolean = false,
    val lawyer: ChatLawyerUi? = null,
    val messages: List<ChatMessageUi> = emptyList(),
    val input: String = "",
    val isSending: Boolean = false,
    val sendError: String? = null,
    val today: String = "",
) {
    val canSend: Boolean get() = input.isNotBlank() && !isSending
}

data class ChatLawyerUi(
    val name: String,
    val initials: String,
    val isOnline: Boolean,
    val isVerified: Boolean,
    val avatarTone: AvatarTone,
)

data class ChatMessageUi(
    val id: Int,
    val fromMe: Boolean,
    val text: String,
    val time: String,
    val day: String,
    val dayLabel: String,
    val isRead: Boolean,
    val files: List<ChatFileUi> = emptyList(),
    val articles: List<ChatArticleUi> = emptyList(),
)

data class ChatFileUi(
    val name: String,
    val meta: String,
    val url: String?,
)

data class ChatArticleUi(val number: Int, val title: String)
