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

    suspend fun getHelp(): AsosResult<HelpOverview>

    suspend fun getLawyers(specialty: String? = null, onlineOnly: Boolean = false): AsosResult<List<Lawyer>>

    suspend fun askAssistant(text: String, threadId: Int? = null): AsosResult<AssistantAnswer>

    suspend fun openConversation(lawyerId: Int, firstMessage: String? = null): AsosResult<Conversation>

    suspend fun getConversations(): AsosResult<List<Conversation>>

    suspend fun getConversation(conversationId: Int): AsosResult<ConversationDetail>

    suspend fun getOlderMessages(conversationId: Int, beforeMessageId: Int): AsosResult<List<ChatMessage>>

    suspend fun sendMessage(
        conversationId: Int,
        text: String,
        articleNumbers: List<Int> = emptyList(),
    ): AsosResult<ChatMessage>

    suspend fun markRead(conversationId: Int): AsosResult<Int>

    suspend fun requestCallback(phone: String? = null, topic: String? = null): AsosResult<CallbackRequestResult>

    suspend fun bookLawyer(lawyerId: Int, slotAt: String, note: String? = null): AsosResult<BookingResult>
}
