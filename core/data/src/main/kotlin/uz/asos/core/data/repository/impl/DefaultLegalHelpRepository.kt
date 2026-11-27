package uz.asos.core.data.repository.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import uz.asos.core.common.dispatcher.AsosDispatchers
import uz.asos.core.common.dispatcher.Dispatcher
import uz.asos.core.common.result.AsosResult
import uz.asos.core.common.result.mapSuccess
import uz.asos.core.data.mapper.asModel
import uz.asos.core.data.repository.LegalHelpRepository
import uz.asos.core.model.legalhelp.AssistantAnswer
import uz.asos.core.model.legalhelp.BookingResult
import uz.asos.core.model.legalhelp.CallbackRequestResult
import uz.asos.core.model.legalhelp.ChatMessage
import uz.asos.core.model.legalhelp.Conversation
import uz.asos.core.model.legalhelp.ConversationDetail
import uz.asos.core.model.legalhelp.HelpOverview
import uz.asos.core.model.legalhelp.Lawyer
import uz.asos.core.network.ApiCaller
import uz.asos.core.network.api.LegalHelpApi
import uz.asos.core.network.dto.AiAskRequestDto
import uz.asos.core.network.dto.BookingRequestDto
import uz.asos.core.network.dto.CallbackRequestDto
import uz.asos.core.network.dto.OpenConversationRequestDto
import uz.asos.core.network.dto.SendMessageRequestDto
import javax.inject.Inject

internal class DefaultLegalHelpRepository @Inject constructor(
    private val legalHelpApi: LegalHelpApi,
    private val apiCaller: ApiCaller,
    @Dispatcher(AsosDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : LegalHelpRepository {

    override suspend fun getHelp(): AsosResult<HelpOverview> = io {
        apiCaller.call { legalHelpApi.getHelp() }.mapSuccess { it.asModel() }
    }

    override suspend fun getLawyers(specialty: String?, onlineOnly: Boolean): AsosResult<List<Lawyer>> = io {
        apiCaller.call {
            legalHelpApi.getLawyers(online = if (onlineOnly) "1" else null, specialty = specialty)
        }.mapSuccess { response -> response.items.map { it.asModel() } }
    }

    override suspend fun askAssistant(text: String, threadId: Int?): AsosResult<AssistantAnswer> = io {
        apiCaller.call { legalHelpApi.askAi(AiAskRequestDto(text = text.trim(), threadId = threadId)) }
            .mapSuccess { it.asModel() }
    }

    override suspend fun openConversation(lawyerId: Int, firstMessage: String?): AsosResult<Conversation> = io {
        apiCaller.call {
            legalHelpApi.openConversation(
                lawyerId = lawyerId,
                body = OpenConversationRequestDto(message = firstMessage?.trim()?.takeIf { it.isNotEmpty() }),
            )
        }.mapSuccess { it.conversation.asModel() }
    }

    override suspend fun getConversations(): AsosResult<List<Conversation>> = io {
        apiCaller.call { legalHelpApi.getConversations() }
            .mapSuccess { response -> response.items.map { it.asModel() } }
    }

    override suspend fun getConversation(conversationId: Int): AsosResult<ConversationDetail> = io {
        apiCaller.call { legalHelpApi.getConversation(conversationId) }.mapSuccess { it.asModel() }
    }

    override suspend fun getOlderMessages(conversationId: Int, beforeMessageId: Int): AsosResult<List<ChatMessage>> = io {
        apiCaller.call { legalHelpApi.getMessages(conversationId, before = beforeMessageId) }
            .mapSuccess { response -> response.items.map { it.asModel() }.sortedBy { it.id } }
    }

    override suspend fun sendMessage(
        conversationId: Int,
        text: String,
        articleNumbers: List<Int>,
    ): AsosResult<ChatMessage> = io {
        apiCaller.call {
            legalHelpApi.sendMessage(
                conversationId = conversationId,
                body = SendMessageRequestDto(
                    body = text.trim().takeIf { it.isNotEmpty() },
                    articleNumbers = articleNumbers.take(MAX_ARTICLES).takeIf { it.isNotEmpty() },
                ),
            )
        }.mapSuccess { it.message.asModel() }
    }

    override suspend fun markRead(conversationId: Int): AsosResult<Int> = io {
        apiCaller.call { legalHelpApi.markRead(conversationId) }.mapSuccess { it.unreadCount }
    }

    override suspend fun requestCallback(phone: String?, topic: String?): AsosResult<CallbackRequestResult> = io {
        apiCaller.call { legalHelpApi.requestCallback(CallbackRequestDto(phone = phone, topic = topic)) }
            .mapSuccess { it.asModel() }
    }

    override suspend fun bookLawyer(lawyerId: Int, slotAt: String, note: String?): AsosResult<BookingResult> = io {
        apiCaller.call { legalHelpApi.createBooking(BookingRequestDto(lawyerId = lawyerId, slotAt = slotAt, note = note)) }
            .mapSuccess { it.asModel() }
    }

    private suspend fun <T> io(block: suspend () -> T): T = withContext(ioDispatcher) { block() }

    private companion object {
        const val MAX_ARTICLES = 5
    }
}
