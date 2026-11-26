/*
 * ChatUiState.kt
 * Module: :feature:legalhelp   ·   Work package: WP-10
 * Screen: Advokat bilan chat (design artboard: Chat.dc.html)
 *
 * Immutable state the chat screen renders.
 */
package uz.asos.feature.legalhelp.chat

import uz.asos.feature.legalhelp.data.temp.AvatarTone

data class ChatUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val lawyer: ChatLawyerUi? = null,
    val messages: List<ChatMessageUi> = emptyList(),
    val input: String = "",
    /** "Sardor yozmoqda…" */
    val isLawyerTyping: Boolean = false,
    val isSending: Boolean = false,
) {
    val canSend: Boolean get() = input.isNotBlank() && !isSending
}

data class ChatLawyerUi(
    val name: String,
    /** "Sardor" for the typing indicator. */
    val firstName: String,
    val initials: String,
    val isOnline: Boolean,
    val isVerified: Boolean,
    val avatarTone: AvatarTone,
)

data class ChatMessageUi(
    val id: Long,
    val fromMe: Boolean,
    val text: String,
    val time: String,
    val isRead: Boolean,
    val file: ChatFileUi? = null,
    val article: ChatArticleUi? = null,
)

data class ChatFileUi(val name: String, val meta: String)

data class ChatArticleUi(val number: Int, val title: String)
