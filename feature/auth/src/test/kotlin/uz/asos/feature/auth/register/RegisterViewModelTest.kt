package uz.asos.feature.auth.register

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import uz.asos.core.testing.MainDispatcherRule
import uz.asos.feature.auth.common.OtpStep
import uz.asos.core.common.result.AsosResult
import uz.asos.feature.auth.data.FakeEmailAuthRepository
import uz.asos.feature.auth.data.model.AuthErrorCode
import uz.asos.feature.auth.data.model.AuthPurpose

class RegisterViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val auth = FakeEmailAuthRepository()

    private fun createViewModel() = RegisterViewModel(auth)

    private fun RegisterViewModel.fill(name: String = "Aziza Karimova", acceptTerms: Boolean = true) {
        onAction(RegisterAction.OnNameChange(name))
        onAction(RegisterAction.OnEmailChange("aziza@example.uz"))
        if (acceptTerms) onAction(RegisterAction.OnTermsToggle)
    }

    @Test
    fun `code can be requested only with name, email and accepted terms`() {
        val viewModel = createViewModel()

        viewModel.fill(acceptTerms = false)
        assertFalse(viewModel.uiState.value.canRequestCode)

        viewModel.onAction(RegisterAction.OnTermsToggle)
        assertTrue(viewModel.uiState.value.canRequestCode)
    }

    @Test
    fun `short name shows an error and sends nothing`() {
        val viewModel = createViewModel()
        viewModel.fill(name = "A")

        viewModel.onAction(RegisterAction.OnRequestCodeClick)

        assertTrue(viewModel.uiState.value.showNameError)
        assertTrue(auth.requestedEmails.isEmpty())
    }

    @Test
    fun `fixing the name hides the error`() {
        val viewModel = createViewModel()
        viewModel.fill(name = "A")
        viewModel.onAction(RegisterAction.OnRequestCodeClick)

        viewModel.onAction(RegisterAction.OnNameChange("Aziza"))

        assertFalse(viewModel.uiState.value.showNameError)
    }

    @Test
    fun `full code registers with the trimmed name`() = runTest {
        val viewModel = createViewModel()
        viewModel.fill(name = "  Aziza Karimova  ")
        viewModel.onAction(RegisterAction.OnRequestCodeClick)
        assertEquals(OtpStep.CODE, viewModel.uiState.value.otp.step)

        viewModel.events.test {
            viewModel.onAction(RegisterAction.OnCodeChange("123456"))
            assertEquals(RegisterEvent.Registered, awaitItem())
        }
        assertEquals(listOf("Aziza Karimova"), auth.registeredNames)
        assertEquals(listOf("aziza@example.uz" to "123456"), auth.verifiedCodes)
    }

    @Test
    fun `the code is asked for as a sign-up`() {
        val viewModel = createViewModel()
        viewModel.fill()

        viewModel.onAction(RegisterAction.OnRequestCodeClick)

        assertEquals(listOf(AuthPurpose.REGISTER), auth.requestedPurposes)
    }

    @Test
    fun `a taken address is turned away and points at sign-in`() {
        auth.requestCodeResult = AsosResult.Error(
            message = "Bu email allaqachon ro‘yxatdan o‘tgan. Kirish oynasidan foydalaning.",
            code = AuthErrorCode.ALREADY_REGISTERED,
        )
        val viewModel = createViewModel()
        viewModel.fill()

        viewModel.onAction(RegisterAction.OnRequestCodeClick)

        assertEquals(OtpStep.EMAIL, viewModel.uiState.value.otp.step)
        assertTrue(viewModel.uiState.value.otp.needsSignIn)
    }
}
