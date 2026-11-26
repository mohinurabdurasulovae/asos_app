/*
 * ChatViewModelTest.kt
 * Module: :feature:legalhelp   ·   Work package: WP-10
 * Screen: Advokat bilan chat (design artboard: Chat.dc.html)
 *
 * Unit tests for ChatViewModel on the in-memory repository (no network).
 * TODO(WP-02): switch to core:testing FakeLegalHelpRepository when it exists.
 */
package uz.asos.feature.legalhelp.chat

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import uz.asos.core.testing.MainDispatcherRule
import uz.asos.feature.legalhelp.data.temp.InMemoryLegalHelpRepository

class ChatViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createViewModel(chatId: Int = InMemoryLegalHelpRepository.SARDOR_ID) = ChatViewModel(
        savedStateHandle = SavedStateHandle(mapOf(ChatViewModel.CHAT_ID_ARG to chatId)),
        repository = InMemoryLegalHelpRepository(latencyMs = 0),
    )

    @Test
    fun `loads the lawyer and the seeded conversation`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()
        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals("Sardor Rahimov", state.lawyer?.name)
        assertEquals("Sardor", state.lawyer?.firstName)
        assertEquals(4, state.messages.size)

        val last = state.messages.last()
        assertEquals("Ogohlantirish_xati.docx", last.file?.name)
        assertEquals(31, last.article?.number)
    }

    @Test
    fun `sending clears the input, shows typing, then adds the reply`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onAction(ChatAction.OnInputChange("Keyingi qadam qanday?"))
        viewModel.onAction(ChatAction.OnSend)
        runCurrent()

        var state = viewModel.uiState.value
        assertEquals("", state.input)
        assertEquals(5, state.messages.size)
        assertTrue(state.messages.last().fromMe)
        assertTrue(state.isLawyerTyping)

        advanceTimeBy(ChatViewModel.TYPING_MS + 1)
        runCurrent()

        state = viewModel.uiState.value
        assertFalse(state.isLawyerTyping)
        assertEquals(6, state.messages.size)
        assertFalse(state.messages.last().fromMe)
    }

    @Test
    fun `quick reply is sent without touching the typed text`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onAction(ChatAction.OnInputChange("draft"))
        viewModel.onAction(ChatAction.OnQuickReply("Rahmat!"))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("draft", state.input)
        assertEquals("Rahmat!", state.messages[4].text)
        assertEquals(6, state.messages.size)
    }

    @Test
    fun `blank message is not sent`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onAction(ChatAction.OnInputChange("   "))
        viewModel.onAction(ChatAction.OnSend)
        advanceUntilIdle()

        assertEquals(4, viewModel.uiState.value.messages.size)
    }

    @Test
    fun `other lawyers start with an empty chat`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel(chatId = 2)
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.lawyer)
        assertTrue(viewModel.uiState.value.messages.isEmpty())
    }

    @Test
    fun `unknown chat shows an error`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel(chatId = 999)
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.lawyer)
        assertEquals("Suhbat topilmadi", viewModel.uiState.value.errorMessage)
    }
}
