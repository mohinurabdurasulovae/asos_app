/*
 * ChatUiState.kt
 * Module: :feature:legalhelp   ·   Work package: WP-10
 * Screen: Advokat bilan chat (design artboard: Chat.dc.html)
 *
 * Immutable state the chat screen renders.
 */
package uz.asos.feature.legalhelp.chat

import uz.asos.feature.legalhelp.ui.AvatarTone

data class ChatUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    /** The API answered 401: show the sign-in prompt instead of the chat. */
    val needsSignIn: Boolean = false,
    val lawyer: ChatLawyerUi? = null,
    val messages: List<ChatMessageUi> = emptyList(),
    val input: String = "",
    val isSending: Boolean = false,
    /** Send failed: server text, shown in a snackbar-like line above the composer. */
    val sendError: String? = null,
    /** "2026-11-27": messages of this day get the "Bugun" header. */
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
    /** "14:02" */
    val time: String,
    /** "2026-11-21"; "" if unknown. Used for the day headers. */
    val day: String,
    /** "21.11.2026" for headers of other days. */
    val dayLabel: String,
    val isRead: Boolean,
    val files: List<ChatFileUi> = emptyList(),
    val articles: List<ChatArticleUi> = emptyList(),
)

data class ChatFileUi(
    val name: String,
    /** "24 KB · Word" */
    val meta: String,
    val url: String?,
)

data class ChatArticleUi(val number: Int, val title: String)
