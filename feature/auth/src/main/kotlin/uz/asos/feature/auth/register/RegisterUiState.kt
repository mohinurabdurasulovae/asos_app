package uz.asos.feature.auth.register

import uz.asos.feature.auth.common.OtpState

data class RegisterUiState(
    val name: String = "",
    val showNameError: Boolean = false,
    val acceptedTerms: Boolean = false,
    val otp: OtpState = OtpState(),
) {
    val isNameValid: Boolean get() = name.trim().length in NAME_MIN_LENGTH..NAME_MAX_LENGTH
    val canRequestCode: Boolean
        get() = isNameValid && acceptedTerms && otp.isEmailValid && !otp.isSubmitting

    companion object {
        const val NAME_MIN_LENGTH = 2
        const val NAME_MAX_LENGTH = 120
    }
}

sealed interface RegisterEvent {
    data object Registered : RegisterEvent
}
