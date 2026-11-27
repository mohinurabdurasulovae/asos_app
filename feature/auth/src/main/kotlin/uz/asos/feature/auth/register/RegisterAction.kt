package uz.asos.feature.auth.register

sealed interface RegisterAction {
    data class OnNameChange(val name: String) : RegisterAction
    data object OnTermsToggle : RegisterAction
    data class OnEmailChange(val email: String) : RegisterAction
    data object OnRequestCodeClick : RegisterAction
    data class OnCodeChange(val code: String) : RegisterAction
    data object OnVerifyClick : RegisterAction
    data object OnResendClick : RegisterAction
    data object OnChangeEmailClick : RegisterAction

    data object OnBackClick : RegisterAction
    data object OnSignInClick : RegisterAction
}
