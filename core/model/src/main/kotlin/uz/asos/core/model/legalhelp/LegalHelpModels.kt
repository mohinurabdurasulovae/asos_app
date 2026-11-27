package uz.asos.core.model.legalhelp

data class HelpOverview(
    val quote: QuotedArticle?,
    val assistant: AssistantIntro,
    val hotline: Hotline?,
    val lawyers: List<Lawyer>,
    val lawyersOnline: Int,
    val specialties: List<String>,
)

data class QuotedArticle(
    val number: Int,
    val title: String,
    val slug: String,
    val body: String,
)

data class AssistantIntro(
    val title: String,
    val subtitle: String,
    val suggestions: List<String>,
    val disclaimer: String,
)

data class Hotline(
    val phone: String,
    val hours: String,
    val isFree: Boolean,
    val everyDay: Boolean,
    val isOpen: Boolean,
    val note: String?,
)

data class Lawyer(
    val id: Int,
    val name: String,
    val initials: String,
    val specialties: List<String>,
    val years: Int,
    val rating: Double?,
    val reviewsCount: Int,
    val isVerified: Boolean,
    val isOnline: Boolean,
    val nextFreeAt: String?,
)

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

data class ConversationDetail(
    val conversation: Conversation,
    val messages: List<ChatMessage>,
)

data class ChatMessage(
    val id: Int,
    val body: String,
    val isMine: Boolean,
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

data class CallbackRequestResult(
    val requestId: Int,
    val phone: String?,
    val status: String,
)

data class BookingResult(
    val bookingId: Int,
    val slotAt: String,
    val status: String,
)
