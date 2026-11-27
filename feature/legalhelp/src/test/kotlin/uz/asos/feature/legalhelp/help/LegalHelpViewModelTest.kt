/*
 * LegalHelpViewModelTest.kt
 * Module: :feature:legalhelp   ·   Work package: WP-10
 * Screen: Huquqiy yordam (design artboard: Yordam.dc.html)
 *
 * Unit tests for LegalHelpViewModel using fakes from core:testing (no network).
 */
package uz.asos.feature.legalhelp.help

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import uz.asos.core.common.result.AsosResult
import uz.asos.core.testing.MainDispatcherRule
import uz.asos.core.testing.repository.FakeAuthRepository
import uz.asos.core.testing.repository.FakeLegalHelpRepository

class LegalHelpViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeLegalHelpRepository()
    private val auth = FakeAuthRepository(signedIn = true)

    private fun createViewModel() = LegalHelpViewModel(repository = repository, authRepository = auth)

    private val LegalHelpViewModel.state get() = uiState.value

    @Test
    fun `loads the whole screen from GET help`() {
        val state = createViewModel().state

        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals(29, state.quote?.articleNumber)
        assertEquals("Asos AI yordamchi", state.assistant.title)
        assertEquals(3, state.assistant.suggestions.size)
        assertEquals("+998710000000", state.hotline?.dialNumber)
        assertEquals(3, state.lawyers.size)
        assertEquals(2, state.onlineCount)
        assertEquals(listOf("Uy-joy", "Oila", "Mehnat", "Ta’lim"), state.specialties)
    }

    @Test
    fun `lawyer rows are formatted for the cards`() {
        val lawyers = createViewModel().state.lawyers

        val sardor = lawyers.first { it.id == 1 }
        assertEquals("4,9", sardor.ratingLabel)
        assertEquals("Uy-joy va fuqarolik ishlari", sardor.specialtyLine)
        assertNull(sardor.nextFree)

        val bekzod = lawyers.first { it.id == 3 }
        assertNull(bekzod.ratingLabel)
        assertEquals("15:00", bekzod.nextFree?.time)
        assertEquals("2026-11-21 15:00", bekzod.nextFree?.raw)
    }

    @Test
    fun `specialty chip filters the list and Barchasi resets it`() {
        val viewModel = createViewModel()

        viewModel.onAction(LegalHelpAction.OnSpecialtySelect("Mehnat"))
        assertEquals(listOf(3), viewModel.state.visibleLawyers.map { it.id })

        viewModel.onAction(LegalHelpAction.OnSpecialtySelect("Ta’lim"))
        assertEquals(listOf(2), viewModel.state.visibleLawyers.map { it.id })

        viewModel.onAction(LegalHelpAction.OnSpecialtySelect(null))
        assertEquals(3, viewModel.state.visibleLawyers.size)
    }

    @Test
    fun `failed first load shows the error and retry recovers`() {
        repository.helpResult = AsosResult.Error("Internet aloqasi yo‘q.")
        val viewModel = createViewModel()
        assertEquals("Internet aloqasi yo‘q.", viewModel.state.errorMessage)

        repository.helpResult = AsosResult.Success(uz.asos.core.testing.data.LegalHelpSampleData.help)
        viewModel.onAction(LegalHelpAction.OnRetry)

        assertNull(viewModel.state.errorMessage)
        assertEquals(3, viewModel.state.lawyers.size)
    }

    // ------------------------------------------------------------------ AI

    @Test
    fun `assistant sends the question and shows the answer with its articles`() {
        val viewModel = createViewModel()
        viewModel.onAction(LegalHelpAction.OnSuggestionClick("Ijara muammosi"))
        viewModel.onAction(LegalHelpAction.OnAssistantSend)

        assertEquals(listOf("Ijara muammosi"), repository.askedQuestions)
        val answer = viewModel.state.assistant.answer
        assertNotNull(answer)
        assertEquals(31, answer!!.articles.single().number)
        assertEquals("Har kim uy-joy daxlsizligi huquqiga ega.", answer.articles.single().hint)
        assertFalse(viewModel.state.assistant.isSending)
    }

    @Test
    fun `too short question is not sent`() {
        val viewModel = createViewModel()
        viewModel.onAction(LegalHelpAction.OnAssistantInputChange("ijara"))
        viewModel.onAction(LegalHelpAction.OnAssistantSend)

        assertTrue(viewModel.state.assistant.isTooShort)
        assertTrue(repository.askedQuestions.isEmpty())
    }

    @Test
    fun `signed out user is asked to sign in instead of calling the AI`() {
        auth.setSignedIn(false)
        val viewModel = createViewModel()
        viewModel.onAction(LegalHelpAction.OnAssistantInputChange("Uy egasi kvartiraga kirib turibdi"))
        viewModel.onAction(LegalHelpAction.OnAssistantSend)

        assertTrue(viewModel.state.assistant.needsSignIn)
        assertTrue(repository.askedQuestions.isEmpty())

        auth.setSignedIn(true)
        assertFalse(viewModel.state.assistant.needsSignIn)
    }

    @Test
    fun `expired token on ask shows the sign-in hint`() {
        repository.askResult = FakeLegalHelpRepository.SIGNED_OUT
        val viewModel = createViewModel()
        viewModel.onAction(LegalHelpAction.OnAssistantInputChange("Uy egasi kvartiraga kirib turibdi"))
        viewModel.onAction(LegalHelpAction.OnAssistantSend)

        assertTrue(viewModel.state.assistant.needsSignIn)
        assertNull(viewModel.state.assistant.errorMessage)
    }

    // ------------------------------------------------------------------ chat

    @Test
    fun `Yozish opens the conversation and sends its id`() = runTest {
        val viewModel = createViewModel()
        viewModel.events.test {
            viewModel.onAction(LegalHelpAction.OnWriteToLawyer(1))
            assertEquals(LegalHelpEvent.OpenChat(42), awaitItem())
        }
        assertEquals(listOf(1), repository.openedLawyerIds)
        assertNull(viewModel.state.openingChatForLawyerId)
    }

    @Test
    fun `Yozish while signed out asks to sign in`() = runTest {
        auth.setSignedIn(false)
        val viewModel = createViewModel()
        viewModel.events.test {
            viewModel.onAction(LegalHelpAction.OnWriteToLawyer(1))
            assertEquals(LegalHelpEvent.NeedsSignIn, awaitItem())
        }
        assertTrue(repository.openedLawyerIds.isEmpty())
    }

    // ------------------------------------------------------------------ callback

    @Test
    fun `callback while signed in is sent at once`() = runTest {
        val viewModel = createViewModel()
        viewModel.events.test {
            viewModel.onAction(LegalHelpAction.OnCallbackRequest)
            assertEquals(LegalHelpEvent.CallbackSent, awaitItem())
        }
        assertNull(viewModel.state.callbackDialog)
        assertEquals(1, repository.callbackTopics.size)
    }

    @Test
    fun `callback while signed out asks for a valid phone first`() = runTest {
        auth.setSignedIn(false)
        val viewModel = createViewModel()
        viewModel.onAction(LegalHelpAction.OnCallbackRequest)
        assertNotNull(viewModel.state.callbackDialog)

        viewModel.onAction(LegalHelpAction.OnCallbackPhoneChange("+998 90 12"))
        viewModel.onAction(LegalHelpAction.OnCallbackConfirm)
        assertTrue(viewModel.state.callbackDialog!!.isInvalid)
        assertTrue(repository.callbackTopics.isEmpty())

        viewModel.events.test {
            viewModel.onAction(LegalHelpAction.OnCallbackPhoneChange("+998 90 123 45 67"))
            viewModel.onAction(LegalHelpAction.OnCallbackConfirm)
            assertEquals(LegalHelpEvent.CallbackSent, awaitItem())
        }
        assertNull(viewModel.state.callbackDialog)
    }

    @Test
    fun `phone check accepts only +998 and 9 digits`() {
        assertTrue(LegalHelpViewModel.isValidUzPhone("+998901234567"))
        assertFalse(LegalHelpViewModel.isValidUzPhone("+99890123456"))
        assertFalse(LegalHelpViewModel.isValidUzPhone("901234567"))
    }

    // ------------------------------------------------------------------ booking

    @Test
    fun `booking asks to confirm the free slot, then books it`() = runTest {
        val viewModel = createViewModel()
        viewModel.onAction(LegalHelpAction.OnBookLawyer(3))
        val dialog = viewModel.state.bookingDialog
        assertEquals("Bekzod Karimov", dialog?.lawyerName)

        viewModel.events.test {
            viewModel.onAction(LegalHelpAction.OnBookingConfirm)
            assertEquals(LegalHelpEvent.BookingDone, awaitItem())
        }
        assertEquals(listOf(3 to "2026-11-21 15:00"), repository.bookings)
        assertNull(viewModel.state.bookingDialog)
    }

    @Test
    fun `taken slot keeps the dialog open with the server message`() {
        repository.bookingResult = AsosResult.Error("Bu vaqt band", httpStatus = 409)
        val viewModel = createViewModel()
        viewModel.onAction(LegalHelpAction.OnBookLawyer(3))
        viewModel.onAction(LegalHelpAction.OnBookingConfirm)

        assertEquals("Bu vaqt band", viewModel.state.bookingDialog?.errorMessage)
        assertFalse(viewModel.state.bookingDialog!!.isSending)
    }

    @Test
    fun `lawyer without a free slot cannot be booked`() = runTest {
        val viewModel = createViewModel()
        viewModel.events.test {
            viewModel.onAction(LegalHelpAction.OnBookLawyer(1))
            assertEquals(LegalHelpEvent.NoFreeSlot, awaitItem())
        }
        assertNull(viewModel.state.bookingDialog)
    }
}
