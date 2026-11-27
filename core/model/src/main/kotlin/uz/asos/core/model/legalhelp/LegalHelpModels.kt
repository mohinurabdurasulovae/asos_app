/*
 * LegalHelpModels.kt
 * Module: :core:model   ·   Work package: WP-02 (legal-help slice written with WP-10)
 *
 * Domain models shared by every layer for "Huquqiy yordam" and the lawyer chat.
 * Mirrors docs/api/openapi.yaml (tag: Yordam) without network types.
 */
package uz.asos.core.model.legalhelp

/** GET /v1/help: the whole Huquqiy yordam screen in one call. */
data class HelpOverview(
    /** Article 29, quoted at the top. null if the server sends none. */
    val quote: QuotedArticle?,
    val assistant: AssistantIntro,
    val hotline: Hotline?,
    /** Online lawyers first (server order). */
    val lawyers: List<Lawyer>,
    val lawyersOnline: Int,
    /** Filter chips under "Advokatlar", e.g. "Uy-joy", "Oila", "Mehnat". */
    val specialties: List<String>,
)

data class QuotedArticle(
    val number: Int,
    val title: String,
    val slug: String,
    val body: String,
)

/** Texts of the "Asos AI yordamchi" card. */
data class AssistantIntro(
    val title: String,
    val subtitle: String,
    val suggestions: List<String>,
    val disclaimer: String,
)

data class Hotline(
    val phone: String,
    /** "09:00–21:00" */
    val hours: String,
    val isFree: Boolean,
    val everyDay: Boolean,
    /** Open right now (Tashkent time, decided by the server). */
    val isOpen: Boolean,
    val note: String?,
)

data class Lawyer(
    val id: Int,
    val name: String,
    val initials: String,
    val specialties: List<String>,
    val years: Int,
    /** null = no reviews yet. */
    val rating: Double?,
    val reviewsCount: Int,
    val isVerified: Boolean,
    val isOnline: Boolean,
    /** Next free slot, e.g. "2026-11-21 15:00". null when unknown. */
    val nextFreeAt: String?,
)

/** POST /v1/ai/ask */
data class AssistantAnswer(
    val threadId: Int?,
    val answer: String,
    val articles: List<AssistantArticle>,
    val disclaimer: String?,
)

data class AssistantArticle(
    val number: Int,
    val title: String,
    val slug: String,
    val bullets: List<String>,
)

data class Conversation(
    val id: Int,
    val subject: String?,
    val lawyer: Lawyer?,
    val unreadCount: Int,
    val createdAt: String,
    val lastMessageAt: String?,
)

/** GET /v1/conversations/{id} */
data class ConversationDetail(
    val conversation: Conversation,
    /** Oldest first, ready to show top to bottom. */
    val messages: List<ChatMessage>,
)

data class ChatMessage(
    val id: Int,
    val body: String,
    val isMine: Boolean,
    /** Server time, e.g. "2026-11-21 14:02:10" or ISO-8601. */
    val createdAt: String,
    val readAt: String?,
    val attachments: List<MessageAttachment>,
)

sealed interface MessageAttachment {
    data class ArticleLink(val number: Int, val title: String, val slug: String) : MessageAttachment

    data class File(
        val fileName: String,
        val mime: String?,
        val bytes: Long,
        val url: String?,
    ) : MessageAttachment
}

/** POST /v1/callback-requests */
data class CallbackRequestResult(
    val requestId: Int,
    val phone: String?,
    val status: String,
)

/** POST /v1/bookings */
data class BookingResult(
    val bookingId: Int,
    val slotAt: String,
    val status: String,
)
