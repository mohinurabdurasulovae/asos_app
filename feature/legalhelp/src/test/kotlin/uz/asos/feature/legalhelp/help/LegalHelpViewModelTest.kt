/*
 * LegalHelpViewModelTest.kt
 * Module: :feature:legalhelp   ·   Work package: WP-10
 * Screen: Huquqiy yordam (design artboard: Yordam.dc.html)
 *
 * Unit tests for LegalHelpViewModel on the in-memory repository (no network).
 * TODO(WP-02): switch to core:testing FakeLegalHelpRepository when it exists.
 */
package uz.asos.feature.legalhelp.help

import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import uz.asos.core.common.result.AsosResult
import uz.asos.core.testing.MainDispatcherRule
import uz.asos.feature.legalhelp.data.temp.AssistantAnswer
import uz.asos.feature.legalhelp.data.temp.ChatMessage
import uz.asos.feature.legalhelp.data.temp.ChatThread
import uz.asos.feature.legalhelp.data.temp.Hotline
import uz.asos.feature.legalhelp.data.temp.InMemoryLegalHelpRepository
import uz.asos.feature.legalhelp.data.temp.Lawyer
import uz.asos.feature.legalhelp.data.temp.LawyerCategory
import uz.asos.feature.legalhelp.data.temp.TempLegalHelpRepository

class LegalHelpViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createViewModel(repository: TempLegalHelpRepository = InMemoryLegalHelpRepository(latencyMs = 0)) =
        LegalHelpViewModel(repository)

    @Test
    fun `loads lawyers and hotline ready for the screen`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()
        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals(3, state.lawyers.size)
        assertEquals(2, state.onlineCount)
        assertEquals("4,9", state.lawyers.first().ratingLabel)
        assertEquals("+998 (71) 000-00-00", state.hotline?.displayNumber)
    }

    @Test
    fun `offline lawyer shows the free time, online lawyers do not`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()
        advanceUntilIdle()
        val lawyers = viewModel.uiState.value.lawyers

        assertEquals("15:00", lawyers.first { it.name == "Bekzod Karimov" }.nextFreeTime)
        assertTrue(lawyers.filter { it.isOnline }.all { it.nextFreeTime == null })
    }

    @Test
    fun `category chip filters the list and Barchasi resets it`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onAction(LegalHelpAction.OnCategorySelect(LawyerCategory.LABOR))
        assertEquals(listOf("Bekzod Karimov"), viewModel.uiState.value.visibleLawyers.map { it.name })

        viewModel.onAction(LegalHelpAction.OnCategorySelect(LawyerCategory.EDUCATION))
        assertEquals(listOf("Malika Yusupova"), viewModel.uiState.value.visibleLawyers.map { it.name })

        viewModel.onAction(LegalHelpAction.OnCategorySelect(null))
        assertEquals(3, viewModel.uiState.value.visibleLawyers.size)
    }

    @Test
    fun `suggestion chip fills the assistant input`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()
        viewModel.onAction(LegalHelpAction.OnSuggestionClick("Ijara muammosi"))

        assertEquals("Ijara muammosi", viewModel.uiState.value.assistant.input)
        assertTrue(viewModel.uiState.value.assistant.canSend)
    }

    @Test
    fun `assistant answers a rent question with article 31`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()
        viewModel.onAction(LegalHelpAction.OnAssistantInputChange("Uy egasi kvartiraga ruxsatsiz kiryapti"))
        viewModel.onAction(LegalHelpAction.OnAssistantSend)
        advanceUntilIdle()

        val assistant = viewModel.uiState.value.assistant
        assertFalse(assistant.isSending)
        assertEquals(31, assistant.answer?.articleNumber)
    }

    @Test
    fun `assistant answers a salary question with article 42`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()
        viewModel.onAction(LegalHelpAction.OnSuggestionClick("Ish haqi kechikdi"))
        viewModel.onAction(LegalHelpAction.OnAssistantSend)
        advanceUntilIdle()

        assertEquals(42, viewModel.uiState.value.assistant.answer?.articleNumber)
    }

    @Test
    fun `blank question is not sent`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()
        viewModel.onAction(LegalHelpAction.OnAssistantInputChange("   "))
        viewModel.onAction(LegalHelpAction.OnAssistantSend)
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.assistant.answer)
    }

    @Test
    fun `failed first load shows the error and retry recovers`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = FlakyRepository()
        val viewModel = createViewModel(repository)
        advanceUntilIdle()
        assertEquals("Internet aloqasi yo‘q", viewModel.uiState.value.errorMessage)

        repository.fail = false
        viewModel.onAction(LegalHelpAction.OnRetry)
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.errorMessage)
        assertEquals(3, viewModel.uiState.value.lawyers.size)
    }
}

/** Fails getLawyers() while [fail] is true; everything else comes from the in-memory data. */
private class FlakyRepository(
    private val delegate: TempLegalHelpRepository = InMemoryLegalHelpRepository(latencyMs = 0),
) : TempLegalHelpRepository {
    var fail = true

    override suspend fun getLawyers(): AsosResult<List<Lawyer>> =
        if (fail) AsosResult.Error("Internet aloqasi yo‘q") else delegate.getLawyers()

    override suspend fun getHotline(): AsosResult<Hotline> = delegate.getHotline()
    override suspend fun askAssistant(question: String): AsosResult<AssistantAnswer> = delegate.askAssistant(question)
    override suspend fun getChat(chatId: Int): AsosResult<ChatThread> = delegate.getChat(chatId)
    override suspend fun sendMessage(chatId: Int, text: String): AsosResult<ChatMessage> = delegate.sendMessage(chatId, text)
    override suspend fun awaitLawyerReply(chatId: Int, text: String): AsosResult<ChatMessage> =
        delegate.awaitLawyerReply(chatId, text)
}
