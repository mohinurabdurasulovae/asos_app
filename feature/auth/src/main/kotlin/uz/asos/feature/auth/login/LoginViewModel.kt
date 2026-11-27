package uz.asos.feature.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.asos.core.data.repository.AuthRepository
import uz.asos.feature.auth.common.OtpController
import uz.asos.feature.auth.data.EmailAuthRepository
import uz.asos.feature.auth.data.model.AuthPurpose
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val emailAuthRepository: EmailAuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _events = Channel<LoginEvent>(Channel.BUFFERED)
    val events: Flow<LoginEvent> = _events.receiveAsFlow()

    private val otp = OtpController(
        scope = viewModelScope,
        read = { _uiState.value.otp },
        write = { transform -> _uiState.update { it.copy(otp = transform(it.otp)) } },
        sendCode = { email -> emailAuthRepository.requestCode(email, AuthPurpose.LOGIN) },
        checkCode = emailAuthRepository::verifyCode,
        onVerified = { _events.send(LoginEvent.SignedIn) },
    )

    init {
        checkSession()
    }

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnEmailChange -> otp.onEmailChange(action.email)
            LoginAction.OnRequestCodeClick -> otp.requestCode()
            is LoginAction.OnCodeChange -> otp.onCodeChange(action.code)
            LoginAction.OnVerifyClick -> otp.verify()
            LoginAction.OnResendClick -> otp.resend()
            LoginAction.OnChangeEmailClick -> otp.backToEmail()
            LoginAction.OnGoogleClick -> send(LoginEvent.GoogleUnavailable)
            LoginAction.OnSignUpClick -> Unit
        }
    }

    private fun checkSession() {
        viewModelScope.launch {
            if (authRepository.isSignedIn.first()) {
                _events.send(LoginEvent.SignedIn)
            } else {
                _uiState.update { it.copy(isCheckingSession = false) }
            }
        }
    }

    private fun send(event: LoginEvent) {
        viewModelScope.launch { _events.send(event) }
    }
}
