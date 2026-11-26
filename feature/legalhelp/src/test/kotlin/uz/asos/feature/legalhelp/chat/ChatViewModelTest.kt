/*
 * ChatViewModelTest.kt
 * Module: :feature:legalhelp   ·   Work package: WP-10
 * Screen: Advokat bilan chat (design artboard: Chat.dc.html)
 *
 * Unit tests for ChatViewModel using fakes from core:testing (no network).
 */
package uz.asos.feature.legalhelp.chat

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import uz.asos.core.common.result.AsosResult
import uz.asos.core.model.legalhelp.ChatMessage
import uz.asos.core.testing.MainDispatcherRule
import uz.asos.core.testing.data.LegalHelpSampleData
import uz.asos.core.testing.repository.FakeLegalHelpRepository

class ChatViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeLegalHelpRepository()

    private fun createViewModel(conversationId: Int = 42) = ChatViewModel(
        savedStateHandle = SavedStateHandle(mapOf(ChatViewModel.CHAT_ID_ARG to conversationId)),
        repository = repository,
    )

    private val ChatViewModel.state get() = uiState.value

    @Test
    fun `loads the lawyer and the conversation oldest first`() {
        val state = createViewModel().state

        assertFalse(state.isLoading)
        assertEquals("Sardor Rahimov", state.lawyer?.name)
        assertEquals(listOf(1, 2, 3, 4), state.messages.map { it.id })
        assertEquals("14:02", state.messages.first().time)
        assertTrue(state.messages.first().isRead)

        val last = state.messages.last()
        assertEquals("Ogohlantirish_xati.docx", last.files.single().name)
        assertEquals("24 KB · Word", last.files.single().meta)
        assertEquals(31, last.articles.single().number)
    }

    @Test
    fun `unread lawyer message is marked read once`() {
        val viewModel = createViewModel()
        assertEquals(listOf(42), repository.readConversationIds)

        viewModel.onAction(ChatAction.OnRetry)
        assertEquals(listOf(42), repository.readConversationIds)
    }

    @Test
    fun `sending clears the input and adds my message`() {
        val viewModel = createViewModel()
        viewModel.onAction(ChatAction.OnInputChange("Keyingi qadam qanday?"))
        viewModel.onAction(ChatAction.OnSend)

        assertEquals(listOf(42 to "Keyingi qadam qanday?"), repository.sentMessages)
        assertEquals("", viewModel.state.input)
        assertEquals(5, viewModel.state.messages.size)
        assertTrue(viewModel.state.messages.last().fromMe)
    }

    @Test
    fun `quick reply is sent without touching the typed text`() {
        val viewModel = createViewModel()
        viewModel.onAction(ChatAction.OnInputChange("draft"))
        viewModel.onAction(ChatAction.OnQuickReply("Rahmat!"))

        assertEquals("draft", viewModel.state.input)
        assertEquals("Rahmat!", viewModel.state.messages.last().text)
    }

    @Test
    fun `failed send puts the text back and reports the error`() {
        repository.sendResult = AsosResult.Error("Serverda xatolik yuz berdi.")
        val viewModel = createViewModel()
        viewModel.onAction(ChatAction.OnInputChange("Salom"))
        viewModel.onAction(ChatAction.OnSend)

        assertEquals("Salom", viewModel.state.input)
        assertEquals("Serverda xatolik yuz berdi.", viewModel.state.sendError)
        assertEquals(4, viewModel.state.messages.size)

        viewModel.onAction(ChatAction.OnSendErrorShown)
        assertNull(viewModel.state.sendError)
    }

    @Test
    fun `blank message is not sent`() {
        val viewModel = createViewModel()
        viewModel.onAction(ChatAction.OnInputChange("   "))
        viewModel.onAction(ChatAction.OnSend)

        assertTrue(repository.sentMessages.isEmpty())
    }

    @Test
    fun `signed out shows the sign-in prompt`() {
        repository.conversationResult = FakeLegalHelpRepository.SIGNED_OUT
        val state = createViewModel().state

        assertTrue(state.needsSignIn)
        assertNull(state.errorMessage)
    }

    @Test
    fun `while visible the chat refreshes and shows the lawyer's reply`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()
        viewModel.onAction(ChatAction.OnResume)

        val reply = ChatMessage(
            id = 5,
            body = "Ogohlantirish xatini imzolab topshiring.",
            isMine = false,
            createdAt = "2026-11-21 14:20:00",
            readAt = null,
            attachments = emptyList(),
        )
        val detail = LegalHelpSampleData.conversationDetail
        repository.conversationResult = AsosResult.Success(detail.copy(messages = detail.messages + reply))

        advanceTimeBy(ChatViewModel.POLL_MS + 1)
        runCurrent()
        assertEquals(5, viewModel.state.messages.last().id)

        // Hidden: no more refreshes.
        viewModel.onAction(ChatAction.OnPause)
        repository.conversationResult = AsosResult.Error("x")
        advanceTimeBy(ChatViewModel.POLL_MS * 3)
        runCurrent()
        assertEquals(5, viewModel.state.messages.size)
    }
}
