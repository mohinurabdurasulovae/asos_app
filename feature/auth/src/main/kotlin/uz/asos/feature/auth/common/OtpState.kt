package uz.asos.feature.auth.common

import uz.asos.feature.auth.data.model.AuthErrorCode

enum class OtpStep { EMAIL, CODE }

data class OtpState(
    val step: OtpStep = OtpStep.EMAIL,
    val email: String = "",
    val code: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val errorCode: String? = null,
    val resendSeconds: Int = 0,
    val devCode: String? = null,
) {
    val isEmailValid: Boolean get() = email.isValidEmail()
    val isCodeComplete: Boolean get() = code.length == CODE_LENGTH
    val canResend: Boolean get() = resendSeconds == 0 && !isSubmitting

    val maskedEmail: String get() = email.trim()

    /** Kirishda: bu manzil bo'yicha akkaunt yo'q, ro'yxatdan o'tish kerak. */
    val needsRegistration: Boolean get() = errorCode == AuthErrorCode.NOT_REGISTERED

    /** Ro'yxatdan o'tishda: manzil band, kirish oynasiga o'tish kerak. */
    val needsSignIn: Boolean get() = errorCode == AuthErrorCode.ALREADY_REGISTERED

    companion object {
        const val CODE_LENGTH = 6
        const val EMAIL_MAX_LENGTH = 254
    }
}

private val EmailPattern = Regex("^[^\\s@]+@[^\\s@.]+(\\.[^\\s@.]+)+$")

internal fun String.isValidEmail(): Boolean = trim().let {
    it.length <= OtpState.EMAIL_MAX_LENGTH && EmailPattern.matches(it)
}
