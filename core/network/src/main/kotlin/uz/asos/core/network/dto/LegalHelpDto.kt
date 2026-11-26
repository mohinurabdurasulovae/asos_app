/*
 * LegalHelpDto.kt
 * Module: :core:network   ·   Work package: WP-02 (legal-help slice written with WP-10)
 *
 * @Serializable DTOs for the "Yordam" endpoints. Field names mirror the OpenAPI schemas
 * (Hotline, Lawyer, Conversation, Message, Booking). Every field has a default so a missing
 * key never breaks parsing (Json is configured with ignoreUnknownKeys + coerceInputValues).
 */
package uz.asos.core.network.dto

import kotlinx.serialization.Serializable

// ---------------------------------------------------------------- shared schemas

@Serializable
data class HotlineDto(
    val phone: String = "",
    val hours: String = "",
    val isFree: Boolean = false,
    val everyDay: Boolean = false,
    val isOpen: Boolean = false,
    val note: String? = null,
)

@Serializable
data class LawyerDto(
    val id: Int = 0,
    val name: String = "",
    val initials: String = "",
    val specialties: List<String> = emptyList(),
    val years: Int = 0,
    val rating: Double? = null,
    val reviewsCount: Int = 0,
    val isVerified: Boolean = false,
    val isOnline: Boolean = false,
    val nextFreeAt: String? = null,
)

@Serializable
data class ConversationLastMessageDto(
    val excerpt: String = "",
    val isMine: Boolean = false,
    val at: String = "",
)

@Serializable
data class ConversationDto(
    val id: Int = 0,
    val subject: String? = null,
    val createdAt: String = "",
    val lastMessageAt: String? = null,
    val unreadCount: Int = 0,
    val lawyer: LawyerDto? = null,
    val lastMessage: ConversationLastMessageDto? = null,
)

@Serializable
data class MessageAttachmentDto(
    val id: Int = 0,
    /** "article" | "file" */
    val kind: String = "",
    val article: ArticleRefDto? = null,
    val fileName: String? = null,
    val mime: String? = null,
    val bytes: Long = 0,
    val url: String? = null,
)

@Serializable
data class MessageDto(
    val id: Int = 0,
    val body: String = "",
    val isMine: Boolean = false,
    val createdAt: String = "",
    val readAt: String? = null,
    val attachments: List<MessageAttachmentDto> = emptyList(),
)

@Serializable
data class BookingDto(
    val id: Int = 0,
    val slotAt: String = "",
    /** pending | confirmed | cancelled */
    val status: String = "",
    val note: String? = null,
    val lawyer: LawyerDto? = null,
)

// ---------------------------------------------------------------- GET /v1/help

@Serializable
data class HelpResponseDto(
    val ok: Boolean = true,
    val quote: HelpQuoteDto? = null,
    val ai: HelpAiDto = HelpAiDto(),
    val hotline: HotlineDto? = null,
    val lawyers: List<LawyerDto> = emptyList(),
    val lawyersOnline: Int = 0,
    val specialties: List<String> = emptyList(),
)

@Serializable
data class HelpQuoteDto(
    val number: Int = 0,
    val title: String = "",
    val slug: String = "",
    val body: String = "",
)

@Serializable
data class HelpAiDto(
    val title: String = "",
    val subtitle: String = "",
    val suggestions: List<String> = emptyList(),
    val disclaimer: String = "",
)

// ---------------------------------------------------------------- hotline / lawyers

@Serializable
data class HotlineResponseDto(
    val ok: Boolean = true,
    val hotline: HotlineDto = HotlineDto(),
)

@Serializable
data class LawyersResponseDto(
    val ok: Boolean = true,
    val items: List<LawyerDto> = emptyList(),
)

// ---------------------------------------------------------------- conversations

@Serializable
data class OpenConversationRequestDto(
    val subject: String? = null,
    val message: String? = null,
    val caseNumber: Int? = null,
)

@Serializable
data class ConversationResponseDto(
    val ok: Boolean = true,
    val conversation: ConversationDto = ConversationDto(),
)

@Serializable
data class ConversationsResponseDto(
    val ok: Boolean = true,
    val items: List<ConversationDto> = emptyList(),
)

@Serializable
data class ConversationDetailResponseDto(
    val ok: Boolean = true,
    val conversation: ConversationDto = ConversationDto(),
    val messages: List<MessageDto> = emptyList(),
)

@Serializable
data class MessagesResponseDto(
    val ok: Boolean = true,
    val items: List<MessageDto> = emptyList(),
)

@Serializable
data class SendMessageRequestDto(
    val body: String? = null,
    val articleNumbers: List<Int>? = null,
)

@Serializable
data class SendMessageResponseDto(
    val ok: Boolean = true,
    val message: MessageDto = MessageDto(),
)

@Serializable
data class MarkReadResponseDto(
    val ok: Boolean = true,
    val unreadCount: Int = 0,
)

// ---------------------------------------------------------------- AI assistant

@Serializable
data class AiSuggestionsResponseDto(
    val ok: Boolean = true,
    val items: List<String> = emptyList(),
    val disclaimer: String = "",
)

@Serializable
data class AiAskRequestDto(
    val text: String,
    val threadId: Int? = null,
)

@Serializable
data class AiArticleDto(
    val number: Int = 0,
    val title: String = "",
    val slug: String = "",
    val bullets: List<String> = emptyList(),
)

@Serializable
data class AiAskResponseDto(
    val ok: Boolean = true,
    val threadId: Int? = null,
    val answer: String = "",
    val articles: List<AiArticleDto> = emptyList(),
    val disclaimer: String? = null,
    /** "stub" | "anthropic" */
    val driver: String? = null,
)

// ---------------------------------------------------------------- callback / bookings

@Serializable
data class CallbackRequestDto(
    val phone: String? = null,
    val topic: String? = null,
)

@Serializable
data class CallbackResponseDto(
    val ok: Boolean = true,
    val requestId: Int = 0,
    val phone: String? = null,
    val status: String = "",
)

@Serializable
data class BookingRequestDto(
    val lawyerId: Int,
    /** "2026-11-21 15:00" */
    val slotAt: String,
    val note: String? = null,
)

@Serializable
data class BookingResponseDto(
    val ok: Boolean = true,
    val bookingId: Int = 0,
    val slotAt: String = "",
    val status: String = "",
)

@Serializable
data class BookingsResponseDto(
    val ok: Boolean = true,
    val items: List<BookingDto> = emptyList(),
)

@Serializable
data class CancelBookingResponseDto(
    val ok: Boolean = true,
    val status: String = "",
)
