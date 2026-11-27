package uz.asos.feature.auth.login

import uz.asos.feature.auth.common.OtpState

data class LoginUiState(
    val isCheckingSession: Boolean = true,
    val otp: OtpState = OtpState(),
)

sealed interface LoginEvent {
    data object SignedIn : LoginEvent

    data object GoogleUnavailable : LoginEvent
}
