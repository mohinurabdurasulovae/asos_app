package uz.asos.feature.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import uz.asos.feature.auth.common.OtpController
import uz.asos.feature.auth.data.EmailAuthRepository
import uz.asos.feature.auth.data.model.AuthPurpose
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val emailAuthRepository: EmailAuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _events = Channel<RegisterEvent>(Channel.BUFFERED)
    val events: Flow<RegisterEvent> = _events.receiveAsFlow()

    private val otp = OtpController(
        scope = viewModelScope,
        read = { _uiState.value.otp },
        write = { transform -> _uiState.update { it.copy(otp = transform(it.otp)) } },
        sendCode = { email -> emailAuthRepository.requestCode(email, AuthPurpose.REGISTER) },
        checkCode = { email, code -> emailAuthRepository.register(email, code, _uiState.value.name.trim()) },
        onVerified = { _events.send(RegisterEvent.Registered) },
    )

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.OnNameChange -> onNameChange(action.name)
            RegisterAction.OnTermsToggle -> _uiState.update { it.copy(acceptedTerms = !it.acceptedTerms) }
            is RegisterAction.OnEmailChange -> otp.onEmailChange(action.email)
            RegisterAction.OnRequestCodeClick -> requestCode()
            is RegisterAction.OnCodeChange -> otp.onCodeChange(action.code)
            RegisterAction.OnVerifyClick -> otp.verify()
            RegisterAction.OnResendClick -> otp.resend()
            RegisterAction.OnChangeEmailClick -> otp.backToEmail()
            RegisterAction.OnBackClick, RegisterAction.OnSignInClick -> Unit
        }
    }

    private fun onNameChange(input: String) {
        val name = input.take(RegisterUiState.NAME_MAX_LENGTH)
        _uiState.update { state ->
            val updated = state.copy(name = name)
            updated.copy(showNameError = state.showNameError && !updated.isNameValid)
        }
    }

    private fun requestCode() {
        val state = _uiState.value
        if (!state.isNameValid) {
            _uiState.update { it.copy(showNameError = true) }
            return
        }
        if (state.canRequestCode) otp.requestCode()
    }
}
