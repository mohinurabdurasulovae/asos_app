/*
 * LegalHelpMappers.kt
 * Module: :core:data   ·   Work package: WP-02 (legal-help slice written with WP-10)
 *
 * DTO → core:model mappers for the "Yordam" endpoints.
 */
package uz.asos.core.data.mapper

import uz.asos.core.model.legalhelp.AssistantAnswer
import uz.asos.core.model.legalhelp.AssistantArticle
import uz.asos.core.model.legalhelp.AssistantIntro
import uz.asos.core.model.legalhelp.BookingResult
import uz.asos.core.model.legalhelp.CallbackRequestResult
import uz.asos.core.model.legalhelp.ChatMessage
import uz.asos.core.model.legalhelp.Conversation
import uz.asos.core.model.legalhelp.ConversationDetail
import uz.asos.core.model.legalhelp.HelpOverview
import uz.asos.core.model.legalhelp.Hotline
import uz.asos.core.model.legalhelp.Lawyer
import uz.asos.core.model.legalhelp.MessageAttachment
import uz.asos.core.model.legalhelp.QuotedArticle
import uz.asos.core.network.dto.AiAskResponseDto
import uz.asos.core.network.dto.BookingResponseDto
import uz.asos.core.network.dto.CallbackResponseDto
import uz.asos.core.network.dto.ConversationDetailResponseDto
import uz.asos.core.network.dto.ConversationDto
import uz.asos.core.network.dto.HelpResponseDto
import uz.asos.core.network.dto.HotlineDto
import uz.asos.core.network.dto.LawyerDto
import uz.asos.core.network.dto.MessageAttachmentDto
import uz.asos.core.network.dto.MessageDto

internal fun HelpResponseDto.asModel() = HelpOverview(
    quote = quote?.takeIf { it.number > 0 }?.let {
        QuotedArticle(number = it.number, title = it.title, slug = it.slug, body = it.body)
    },
    assistant = AssistantIntro(
        title = ai.title,
        subtitle = ai.subtitle,
        suggestions = ai.suggestions.filter { it.isNotBlank() },
        disclaimer = ai.disclaimer,
    ),
    hotline = hotline?.takeIf { it.phone.isNotBlank() }?.asModel(),
    lawyers = lawyers.map { it.asModel() },
    lawyersOnline = lawyersOnline,
    specialties = specialties.filter { it.isNotBlank() },
)

internal fun HotlineDto.asModel() = Hotline(
    phone = phone,
    hours = hours,
    isFree = isFree,
    everyDay = everyDay,
    isOpen = isOpen,
    note = note?.takeIf { it.isNotBlank() },
)

internal fun LawyerDto.asModel() = Lawyer(
    id = id,
    name = name,
    initials = initials.ifBlank { name.initials() },
    specialties = specialties.filter { it.isNotBlank() },
    years = years,
    rating = rating?.takeIf { reviewsCount > 0 && it > 0.0 },
    reviewsCount = reviewsCount,
    isVerified = isVerified,
    isOnline = isOnline,
    nextFreeAt = nextFreeAt?.takeIf { it.isNotBlank() },
)

internal fun ConversationDto.asModel() = Conversation(
    id = id,
    subject = subject?.takeIf { it.isNotBlank() },
    lawyer = lawyer?.asModel(),
    unreadCount = unreadCount,
    createdAt = createdAt,
    lastMessageAt = lastMessageAt,
)

internal fun ConversationDetailResponseDto.asModel() = ConversationDetail(
    conversation = conversation.asModel(),
    // The API does not promise an order: show oldest first.
    messages = messages.map { it.asModel() }.sortedBy { it.id },
)

internal fun MessageDto.asModel() = ChatMessage(
    id = id,
    body = body,
    isMine = isMine,
    createdAt = createdAt,
    readAt = readAt,
    attachments = attachments.mapNotNull { it.asModel() },
)

private fun MessageAttachmentDto.asModel(): MessageAttachment? = when (kind) {
    "article" -> article?.takeIf { it.number > 0 }?.let {
        MessageAttachment.ArticleLink(number = it.number, title = it.title, slug = it.slug)
    }
    "file" -> fileName?.takeIf { it.isNotBlank() }?.let {
        MessageAttachment.File(fileName = it, mime = mime, bytes = bytes, url = url)
    }
    else -> null
}

internal fun AiAskResponseDto.asModel() = AssistantAnswer(
    threadId = threadId,
    answer = answer,
    articles = articles.filter { it.number > 0 }.map {
        AssistantArticle(number = it.number, title = it.title, slug = it.slug, bullets = it.bullets)
    },
    disclaimer = disclaimer?.takeIf { it.isNotBlank() },
)

internal fun CallbackResponseDto.asModel() = CallbackRequestResult(
    requestId = requestId,
    phone = phone,
    status = status,
)

internal fun BookingResponseDto.asModel() = BookingResult(
    bookingId = bookingId,
    slotAt = slotAt,
    status = status,
)

/** "Sardor Rahimov" → "SR" (fallback when the API sends no initials). */
private fun String.initials(): String =
    split(' ').filter { it.isNotBlank() }.take(2).joinToString("") { it.first().uppercase() }
