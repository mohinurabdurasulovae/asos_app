package uz.asos.feature.auth.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.asos.core.common.result.AsosResult
import uz.asos.feature.auth.data.model.AuthSession
import uz.asos.feature.auth.data.model.CodeRequest

internal class OtpController(
    private val scope: CoroutineScope,
    private val read: () -> OtpState,
    private val write: ((OtpState) -> OtpState) -> Unit,
    private val sendCode: suspend (email: String) -> AsosResult<CodeRequest>,
    private val checkCode: suspend (email: String, code: String) -> AsosResult<AuthSession>,
    private val onVerified: suspend (AuthSession) -> Unit,
) {

    private var requestedEmail: String = ""
    private var timerJob: Job? = null

    fun onEmailChange(input: String) {
        val email = input.filterNot(Char::isWhitespace).take(OtpState.EMAIL_MAX_LENGTH)
        write { it.copy(email = email, errorMessage = null, errorCode = null) }
    }

    fun onCodeChange(input: String) {
        val code = input.filter(Char::isDigit).take(OtpState.CODE_LENGTH)
        write { it.copy(code = code, errorMessage = null, errorCode = null) }
        if (read().isCodeComplete) verify()
    }

    fun requestCode() {
        val state = read()
        if (!state.isEmailValid || state.isSubmitting) return
        scope.launch {
            write { it.copy(isSubmitting = true, errorMessage = null, errorCode = null) }
            val email = state.email.trim()
            when (val result = sendCode(email)) {
                is AsosResult.Success -> {
                    requestedEmail = result.data.email.ifBlank { email }
                    write {
                        it.copy(
                            step = OtpStep.CODE,
                            code = "",
                            isSubmitting = false,
                            devCode = result.data.devCode,
                        )
                    }
                    startResendTimer(result.data.resendInSeconds)
                }
                is AsosResult.Error -> write {
                    it.copy(isSubmitting = false, errorMessage = result.message, errorCode = result.code)
                }
            }
        }
    }

    fun resend() {
        if (read().canResend) requestCode()
    }

    fun verify() {
        val state = read()
        if (!state.isCodeComplete || state.isSubmitting) return
        scope.launch {
            write { it.copy(isSubmitting = true, errorMessage = null, errorCode = null) }
            when (val result = checkCode(requestedEmail, state.code)) {
                is AsosResult.Success -> {
                    timerJob?.cancel()
                    write { it.copy(isSubmitting = false) }
                    onVerified(result.data)
                }
                is AsosResult.Error -> write {
                    it.copy(isSubmitting = false, code = "", errorMessage = result.message, errorCode = result.code)
                }
            }
        }
    }

    fun backToEmail() {
        timerJob?.cancel()
        write {
            it.copy(
                step = OtpStep.EMAIL,
                code = "",
                errorMessage = null,
                errorCode = null,
                resendSeconds = 0,
                devCode = null,
            )
        }
    }

    private fun startResendTimer(seconds: Int) {
        timerJob?.cancel()
        write { it.copy(resendSeconds = seconds.coerceAtLeast(0)) }
        timerJob = scope.launch {
            while (read().resendSeconds > 0) {
                delay(1_000)
                write { it.copy(resendSeconds = (it.resendSeconds - 1).coerceAtLeast(0)) }
            }
        }
    }
}
