/*
 * LegalHelpRepository.kt
 * Module: :core:data   ·   Work package: WP-02 (legal-help slice written with WP-10)
 *
 * Interface. AI assistant, hotline, lawyers, bookings, chats. Feature ViewModels depend on
 * this interface only. Calls marked "token" return AsosResult.Error with
 * code = AsosErrorCode.UNAUTHORIZED when nobody is signed in.
 */
package uz.asos.core.data.repository

import uz.asos.core.common.result.AsosResult
import uz.asos.core.model.legalhelp.AssistantAnswer
import uz.asos.core.model.legalhelp.BookingResult
import uz.asos.core.model.legalhelp.CallbackRequestResult
import uz.asos.core.model.legalhelp.ChatMessage
import uz.asos.core.model.legalhelp.Conversation
import uz.asos.core.model.legalhelp.ConversationDetail
import uz.asos.core.model.legalhelp.HelpOverview
import uz.asos.core.model.legalhelp.Lawyer

interface LegalHelpRepository {

    /** GET /v1/help: the whole Huquqiy yordam screen in one call. Public. */
    suspend fun getHelp(): AsosResult<HelpOverview>

    /** GET /v1/lawyers. Public. */
    suspend fun getLawyers(specialty: String? = null, onlineOnly: Boolean = false): AsosResult<List<Lawyer>>

    /** POST /v1/ai/ask. [text] must be 10–2000 characters. Token. */
    suspend fun askAssistant(text: String, threadId: Int? = null): AsosResult<AssistantAnswer>

    /** POST /v1/lawyers/{id}/conversations: opens or returns the one chat with this lawyer. Token. */
    suspend fun openConversation(lawyerId: Int, firstMessage: String? = null): AsosResult<Conversation>

    /** GET /v1/conversations. Token. */
    suspend fun getConversations(): AsosResult<List<Conversation>>

    /** GET /v1/conversations/{id}: messages oldest first. Token. */
    suspend fun getConversation(conversationId: Int): AsosResult<ConversationDetail>

    /** GET /v1/conversations/{id}/messages?before=: older page, oldest first. Token. */
    suspend fun getOlderMessages(conversationId: Int, beforeMessageId: Int): AsosResult<List<ChatMessage>>

    /** POST /v1/conversations/{id}/messages. Text and/or up to 5 article numbers. Token. */
    suspend fun sendMessage(
        conversationId: Int,
        text: String,
        articleNumbers: List<Int> = emptyList(),
    ): AsosResult<ChatMessage>

    /** POST /v1/conversations/{id}/read. Returns the remaining unread count. Token. */
    suspend fun markRead(conversationId: Int): AsosResult<Int>

    /** POST /v1/callback-requests. Without a token [phone] is required. */
    suspend fun requestCallback(phone: String? = null, topic: String? = null): AsosResult<CallbackRequestResult>

    /** POST /v1/bookings. [slotAt] "2026-11-21 15:00". 409 = slot already taken. Token. */
    suspend fun bookLawyer(lawyerId: Int, slotAt: String, note: String? = null): AsosResult<BookingResult>
}
