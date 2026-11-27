package uz.asos.feature.auth.login

sealed interface LoginAction {
    data class OnEmailChange(val email: String) : LoginAction
    data object OnRequestCodeClick : LoginAction
    data class OnCodeChange(val code: String) : LoginAction
    data object OnVerifyClick : LoginAction
    data object OnResendClick : LoginAction
    data object OnChangeEmailClick : LoginAction
    data object OnGoogleClick : LoginAction

    data object OnSignUpClick : LoginAction
}
