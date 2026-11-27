package uz.asos.core.testing.repository

import uz.asos.core.common.result.AsosErrorCode
import uz.asos.core.common.result.AsosResult
import uz.asos.core.data.repository.LegalHelpRepository
import uz.asos.core.model.legalhelp.AssistantAnswer
import uz.asos.core.model.legalhelp.BookingResult
import uz.asos.core.model.legalhelp.CallbackRequestResult
import uz.asos.core.model.legalhelp.ChatMessage
import uz.asos.core.model.legalhelp.Conversation
import uz.asos.core.model.legalhelp.ConversationDetail
import uz.asos.core.model.legalhelp.HelpOverview
import uz.asos.core.model.legalhelp.Lawyer
import uz.asos.core.testing.data.LegalHelpSampleData

class FakeLegalHelpRepository : LegalHelpRepository {

    var helpResult: AsosResult<HelpOverview> = AsosResult.Success(LegalHelpSampleData.help)
    var askResult: AsosResult<AssistantAnswer> = AsosResult.Success(LegalHelpSampleData.rentAnswer)
    var openConversationResult: AsosResult<Conversation> = AsosResult.Success(LegalHelpSampleData.conversation)
    var conversationResult: AsosResult<ConversationDetail> = AsosResult.Success(LegalHelpSampleData.conversationDetail)
    var olderMessagesResult: AsosResult<List<ChatMessage>> = AsosResult.Success(emptyList())
    var callbackResult: AsosResult<CallbackRequestResult> =
        AsosResult.Success(CallbackRequestResult(requestId = 7, phone = "+998901234567", status = "new"))
    var bookingResult: AsosResult<BookingResult> =
        AsosResult.Success(BookingResult(bookingId = 3, slotAt = "2026-11-21 15:00", status = "pending"))
    var sendResult: AsosResult<ChatMessage>? = null

    val askedQuestions = mutableListOf<String>()
    val openedLawyerIds = mutableListOf<Int>()
    val sentMessages = mutableListOf<Pair<Int, String>>()
    val readConversationIds = mutableListOf<Int>()
    val callbackTopics = mutableListOf<String?>()
    val bookings = mutableListOf<Pair<Int, String>>()
    var helpCalls = 0
        private set

    private var nextMessageId = 1_000

    override suspend fun getHelp(): AsosResult<HelpOverview> {
        helpCalls++
        return helpResult
    }

    override suspend fun getLawyers(specialty: String?, onlineOnly: Boolean): AsosResult<List<Lawyer>> =
        when (val help = helpResult) {
            is AsosResult.Success -> AsosResult.Success(
                help.data.lawyers
                    .filter { specialty == null || it.specialties.any { s -> s.equals(specialty, ignoreCase = true) } }
                    .filter { !onlineOnly || it.isOnline },
            )
            is AsosResult.Error -> help
        }

    override suspend fun askAssistant(text: String, threadId: Int?): AsosResult<AssistantAnswer> {
        askedQuestions += text
        return askResult
    }

    override suspend fun openConversation(lawyerId: Int, firstMessage: String?): AsosResult<Conversation> {
        openedLawyerIds += lawyerId
        return openConversationResult
    }

    override suspend fun getConversations(): AsosResult<List<Conversation>> =
        when (val result = openConversationResult) {
            is AsosResult.Success -> AsosResult.Success(listOf(result.data))
            is AsosResult.Error -> result
        }

    override suspend fun getConversation(conversationId: Int): AsosResult<ConversationDetail> = conversationResult

    override suspend fun getOlderMessages(conversationId: Int, beforeMessageId: Int): AsosResult<List<ChatMessage>> =
        olderMessagesResult

    override suspend fun sendMessage(conversationId: Int, text: String, articleNumbers: List<Int>): AsosResult<ChatMessage> {
        sentMessages += conversationId to text
        return sendResult ?: AsosResult.Success(
            ChatMessage(
                id = nextMessageId++,
                body = text.trim(),
                isMine = true,
                createdAt = "2026-11-21 14:10:00",
                readAt = null,
                attachments = emptyList(),
            ),
        )
    }

    override suspend fun markRead(conversationId: Int): AsosResult<Int> {
        readConversationIds += conversationId
        return AsosResult.Success(0)
    }

    override suspend fun requestCallback(phone: String?, topic: String?): AsosResult<CallbackRequestResult> {
        callbackTopics += topic
        return callbackResult
    }

    override suspend fun bookLawyer(lawyerId: Int, slotAt: String, note: String?): AsosResult<BookingResult> {
        bookings += lawyerId to slotAt
        return bookingResult
    }

    companion object {
        val SIGNED_OUT: AsosResult.Error =
            AsosResult.Error(message = "Avval tizimga kiring.", code = AsosErrorCode.UNAUTHORIZED, httpStatus = 401)
    }
}
