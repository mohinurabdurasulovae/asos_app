package uz.asos.feature.auth.login

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import uz.asos.core.common.result.AsosResult
import uz.asos.core.testing.MainDispatcherRule
import uz.asos.core.testing.repository.FakeAuthRepository
import uz.asos.feature.auth.common.OtpStep
import uz.asos.feature.auth.data.FakeEmailAuthRepository
import uz.asos.feature.auth.data.model.CodeRequest

class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val session = FakeAuthRepository(signedIn = false)
    private val auth = FakeEmailAuthRepository()

    private fun createViewModel() = LoginViewModel(session, auth)

    private fun LoginViewModel.otp() = uiState.value.otp

    private fun LoginViewModel.requestCodeFor(email: String = "aziza@example.uz") {
        onAction(LoginAction.OnEmailChange(email))
        onAction(LoginAction.OnRequestCodeClick)
    }

    @Test
    fun `guest sees the form`() {
        val viewModel = createViewModel()

        assertFalse(viewModel.uiState.value.isCheckingSession)
        assertEquals(OtpStep.EMAIL, viewModel.otp().step)
    }

    @Test
    fun `signed in user skips the form`() = runTest {
        session.setSignedIn(true)

        createViewModel().events.test {
            assertEquals(LoginEvent.SignedIn, awaitItem())
        }
    }

    @Test
    fun `spaces are stripped and a complete address is accepted`() {
        val viewModel = createViewModel()

        viewModel.onAction(LoginAction.OnEmailChange(" aziza@example.uz "))

        assertEquals("aziza@example.uz", viewModel.otp().email)
        assertTrue(viewModel.otp().isEmailValid)
    }

    @Test
    fun `half-typed addresses are not valid`() {
        val viewModel = createViewModel()

        listOf("aziza", "aziza@", "aziza@example", "@example.uz", "aziza@@example.uz").forEach { input ->
            viewModel.onAction(LoginAction.OnEmailChange(input))
            assertFalse("“$input” should not pass", viewModel.otp().isEmailValid)
        }
    }

    @Test
    fun `an incomplete address does not request a code`() {
        val viewModel = createViewModel()

        viewModel.requestCodeFor("aziza@example")

        assertTrue(auth.requestedEmails.isEmpty())
        assertEquals(OtpStep.EMAIL, viewModel.otp().step)
    }

    @Test
    fun `requesting a code opens the code step with a resend timer`() {
        val viewModel = createViewModel()

        viewModel.requestCodeFor()

        assertEquals(listOf("aziza@example.uz"), auth.requestedEmails)
        assertEquals(OtpStep.CODE, viewModel.otp().step)
        assertEquals(60, viewModel.otp().resendSeconds)
        assertFalse(viewModel.otp().canResend)
    }

    @Test
    fun `the code screen names the address the mail went to`() {
        val viewModel = createViewModel()

        viewModel.requestCodeFor("aziza@example.uz")

        assertEquals("aziza@example.uz", viewModel.otp().maskedEmail)
    }

    @Test
    fun `api error is shown under the email field`() {
        auth.requestCodeResult = AsosResult.Error(message = "Email manzil noto‘g‘ri.", code = "bad_email")
        val viewModel = createViewModel()

        viewModel.requestCodeFor()

        assertEquals(OtpStep.EMAIL, viewModel.otp().step)
        assertEquals("Email manzil noto‘g‘ri.", viewModel.otp().errorMessage)
    }

    @Test
    fun `dev code from the server is shown`() {
        auth.requestCodeResult =
            AsosResult.Success(CodeRequest("aziza@example.uz", 300, 60, devCode = "123456"))
        val viewModel = createViewModel()

        viewModel.requestCodeFor()

        assertEquals("123456", viewModel.otp().devCode)
    }

    @Test
    fun `full code verifies and signs in`() = runTest {
        val viewModel = createViewModel()
        viewModel.requestCodeFor()

        viewModel.events.test {
            viewModel.onAction(LoginAction.OnCodeChange("123456"))
            assertEquals(LoginEvent.SignedIn, awaitItem())
        }
        assertEquals(listOf("aziza@example.uz" to "123456"), auth.verifiedCodes)
    }

    @Test
    fun `wrong code clears the field and shows the message`() {
        auth.verifyResult = AsosResult.Error(message = "Kod noto‘g‘ri.", code = "otp_wrong", httpStatus = 401)
        val viewModel = createViewModel()
        viewModel.requestCodeFor()

        viewModel.onAction(LoginAction.OnCodeChange("000000"))

        assertEquals("", viewModel.otp().code)
        assertEquals("Kod noto‘g‘ri.", viewModel.otp().errorMessage)
        assertEquals(OtpStep.CODE, viewModel.otp().step)
    }

    @Test
    fun `change email goes back to the first step and keeps what was typed`() {
        val viewModel = createViewModel()
        viewModel.requestCodeFor()

        viewModel.onAction(LoginAction.OnChangeEmailClick)

        assertEquals(OtpStep.EMAIL, viewModel.otp().step)
        assertEquals("aziza@example.uz", viewModel.otp().email)
        assertEquals(0, viewModel.otp().resendSeconds)
        assertNull(viewModel.otp().errorMessage)
    }

    @Test
    fun `Google says it is not wired up yet`() = runTest {
        val viewModel = createViewModel()

        viewModel.events.test {
            viewModel.onAction(LoginAction.OnGoogleClick)
            assertEquals(LoginEvent.GoogleUnavailable, awaitItem())
        }
    }
}
