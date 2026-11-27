/*
 * LegalHelpViewModel.kt
 * Module: :feature:legalhelp   ·   Work package: WP-10
 * Screen: Huquqiy yordam (design artboard: Yordam.dc.html)
 *
 * Holds StateFlow<LegalHelpUiState> and handles LegalHelpAction. No Android UI types.
 *
 * API used (through LegalHelpRepository, see docs/api/openapi.yaml):
 *  - GET  /v1/help                        whole screen (public)
 *  - POST /v1/ai/ask                      AI assistant (token)
 *  - POST /v1/lawyers/{id}/conversations  "Yozish" (token)
 *  - POST /v1/callback-requests           "Menga qo‘ng‘iroq" (token optional)
 *  - POST /v1/bookings                    "Vaqt band qilish" (token)
 */
package uz.asos.feature.legalhelp.help

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.asos.core.common.result.AsosErrorCode
import uz.asos.core.common.result.AsosResult
import uz.asos.core.common.text.UzbekFormatters
import uz.asos.core.data.repository.AuthRepository
import uz.asos.core.data.repository.LegalHelpRepository
import uz.asos.core.model.legalhelp.AssistantAnswer
import uz.asos.core.model.legalhelp.HelpOverview
import uz.asos.core.model.legalhelp.Hotline
import uz.asos.core.model.legalhelp.Lawyer
import uz.asos.feature.legalhelp.ui.AvatarTone
import uz.asos.feature.legalhelp.ui.LegalHelpFormat
import javax.inject.Inject

@HiltViewModel
class LegalHelpViewModel @Inject constructor(
    private val repository: LegalHelpRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LegalHelpUiState())
    val uiState: StateFlow<LegalHelpUiState> = _uiState.asStateFlow()

    private val _events = Channel<LegalHelpEvent>(Channel.BUFFERED)
    val events: Flow<LegalHelpEvent> = _events.receiveAsFlow()

    private var loadJob: Job? = null
    private var assistantJob: Job? = null
    private var assistantThreadId: Int? = null
    private var hasLoaded = false

    init {
        load(showSkeleton = true)
        observeSession()
    }

    fun onAction(action: LegalHelpAction) {
        when (action) {
            is LegalHelpAction.OnAssistantInputChange -> updateAssistant {
                it.copy(input = action.text, errorMessage = null, isTooShort = false)
            }
            is LegalHelpAction.OnSuggestionClick -> updateAssistant {
                it.copy(input = action.text, errorMessage = null, isTooShort = false)
            }
            LegalHelpAction.OnAssistantSend -> askAssistant()
            is LegalHelpAction.OnSpecialtySelect -> _uiState.update { it.copy(selectedSpecialty = action.specialty) }
            LegalHelpAction.OnRetry -> load(showSkeleton = true)
            // Coming back from a chat: online status may have changed.
            LegalHelpAction.OnResume -> if (hasLoaded) load(showSkeleton = false)

            is LegalHelpAction.OnWriteToLawyer -> openChat(action.lawyerId)

            LegalHelpAction.OnCallbackRequest -> onCallbackRequest()
            is LegalHelpAction.OnCallbackPhoneChange -> _uiState.update { state ->
                state.copy(callbackDialog = state.callbackDialog?.copy(phone = action.phone, isInvalid = false, errorMessage = null))
            }
            LegalHelpAction.OnCallbackConfirm -> confirmCallback()
            LegalHelpAction.OnCallbackDismiss -> _uiState.update { it.copy(callbackDialog = null) }

            is LegalHelpAction.OnBookLawyer -> onBookLawyer(action.lawyerId)
            LegalHelpAction.OnBookingConfirm -> confirmBooking()
            LegalHelpAction.OnBookingDismiss -> _uiState.update { it.copy(bookingDialog = null) }

            // Navigation and system actions are handled by LegalHelpRoute.
            LegalHelpAction.OnBackClick,
            LegalHelpAction.OnSignInClick,
            is LegalHelpAction.OnArticleClick,
            is LegalHelpAction.OnHotlineCall,
            LegalHelpAction.OnVoiceInputClick,
            is LegalHelpAction.OnCallLawyer,
            is LegalHelpAction.OnVideoCallLawyer,
            -> Unit
        }
    }

    // ------------------------------------------------------------------ loading

    private fun observeSession() {
        viewModelScope.launch {
            authRepository.isSignedIn.collect { signedIn ->
                _uiState.update { state ->
                    state.copy(
                        isSignedIn = signedIn,
                        // Signed in now: the "sign in to ask" hint is no longer true.
                        assistant = if (signedIn) state.assistant.copy(needsSignIn = false) else state.assistant,
                    )
                }
            }
        }
    }

    private fun load(showSkeleton: Boolean) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            if (showSkeleton) _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = repository.getHelp()) {
                is AsosResult.Success -> {
                    hasLoaded = true
                    apply(result.data)
                }
                is AsosResult.Error -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        // Keep old content on a quiet refresh.
                        errorMessage = if (it.lawyers.isEmpty()) result.message else null,
                    )
                }
            }
        }
    }

    private fun apply(help: HelpOverview) {
        val today = LegalHelpFormat.today()
        _uiState.update { state ->
            val specialties = help.specialties.map { UzbekFormatters.display(it) }
            state.copy(
                isLoading = false,
                errorMessage = null,
                quote = help.quote?.let { QuoteUi(articleNumber = it.number, text = UzbekFormatters.display(it.body)) },
                assistant = state.assistant.copy(
                    title = UzbekFormatters.display(help.assistant.title),
                    subtitle = UzbekFormatters.display(help.assistant.subtitle),
                    suggestions = help.assistant.suggestions.map { UzbekFormatters.display(it) },
                    disclaimer = UzbekFormatters.display(help.assistant.disclaimer),
                ),
                hotline = help.hotline?.toUi(),
                lawyers = help.lawyers.map { it.toUi(today) },
                lawyersOnline = help.lawyersOnline,
                specialties = specialties,
                // Drop a selection that no longer exists.
                selectedSpecialty = state.selectedSpecialty?.takeIf { it in specialties },
            )
        }
    }

    // ------------------------------------------------------------------ AI assistant

    private fun askAssistant() {
        val question = _uiState.value.assistant.input.trim()
        if (question.isEmpty() || _uiState.value.assistant.isSending) return
        if (question.length < AssistantUi.MIN_QUESTION_LENGTH) {
            updateAssistant { it.copy(isTooShort = true) }
            return
        }
        if (!_uiState.value.isSignedIn) {
            updateAssistant { it.copy(needsSignIn = true) }
            return
        }

        assistantJob?.cancel()
        assistantJob = viewModelScope.launch {
            updateAssistant { it.copy(isSending = true, errorMessage = null, isTooShort = false, needsSignIn = false) }
            when (val result = repository.askAssistant(question, assistantThreadId)) {
                is AsosResult.Success -> {
                    assistantThreadId = result.data.threadId ?: assistantThreadId
                    updateAssistant { it.copy(isSending = false, answer = result.data.toUi()) }
                }
                is AsosResult.Error -> updateAssistant {
                    if (result.code == AsosErrorCode.UNAUTHORIZED) {
                        it.copy(isSending = false, needsSignIn = true)
                    } else {
                        it.copy(isSending = false, errorMessage = result.message)
                    }
                }
            }
        }
    }

    private inline fun updateAssistant(crossinline transform: (AssistantUi) -> AssistantUi) {
        _uiState.update { it.copy(assistant = transform(it.assistant)) }
    }

    // ------------------------------------------------------------------ chat

    private fun openChat(lawyerId: Int) {
        if (_uiState.value.openingChatForLawyerId != null) return
        if (!_uiState.value.isSignedIn) {
            _events.trySend(LegalHelpEvent.NeedsSignIn)
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(openingChatForLawyerId = lawyerId) }
            val result = repository.openConversation(lawyerId)
            _uiState.update { it.copy(openingChatForLawyerId = null) }
            when (result) {
                is AsosResult.Success -> _events.send(LegalHelpEvent.OpenChat(result.data.id))
                is AsosResult.Error -> _events.send(result.toEvent())
            }
        }
    }

    // ------------------------------------------------------------------ callback

    private fun onCallbackRequest() {
        // Signed in: the server takes the number from the profile.
        if (_uiState.value.isSignedIn) {
            viewModelScope.launch {
                when (val result = repository.requestCallback(topic = _uiState.value.selectedSpecialty)) {
                    is AsosResult.Success -> _events.send(LegalHelpEvent.CallbackSent)
                    is AsosResult.Error -> _events.send(LegalHelpEvent.ShowError(result.message))
                }
            }
        } else {
            _uiState.update { it.copy(callbackDialog = CallbackDialogUi()) }
        }
    }

    private fun confirmCallback() {
        val dialog = _uiState.value.callbackDialog ?: return
        if (dialog.isSending) return
        val phone = LegalHelpFormat.dialNumber(dialog.phone)
        if (!isValidUzPhone(phone)) {
            _uiState.update { it.copy(callbackDialog = dialog.copy(isInvalid = true)) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(callbackDialog = dialog.copy(isSending = true, errorMessage = null)) }
            when (val result = repository.requestCallback(phone = phone, topic = _uiState.value.selectedSpecialty)) {
                is AsosResult.Success -> {
                    _uiState.update { it.copy(callbackDialog = null) }
                    _events.send(LegalHelpEvent.CallbackSent)
                }
                is AsosResult.Error -> _uiState.update { state ->
                    state.copy(callbackDialog = state.callbackDialog?.copy(isSending = false, errorMessage = result.message))
                }
            }
        }
    }

    // ------------------------------------------------------------------ booking

    private fun onBookLawyer(lawyerId: Int) {
        val lawyer = _uiState.value.lawyers.firstOrNull { it.id == lawyerId } ?: return
        val slot = lawyer.nextFree
        when {
            !_uiState.value.isSignedIn -> _events.trySend(LegalHelpEvent.NeedsSignIn)
            slot == null -> _events.trySend(LegalHelpEvent.NoFreeSlot)
            else -> _uiState.update {
                it.copy(bookingDialog = BookingDialogUi(lawyerId = lawyer.id, lawyerName = lawyer.name, slot = slot))
            }
        }
    }

    private fun confirmBooking() {
        val dialog = _uiState.value.bookingDialog ?: return
        if (dialog.isSending) return
        viewModelScope.launch {
            _uiState.update { it.copy(bookingDialog = dialog.copy(isSending = true, errorMessage = null)) }
            when (val result = repository.bookLawyer(dialog.lawyerId, dialog.slot.raw)) {
                is AsosResult.Success -> {
                    _uiState.update { it.copy(bookingDialog = null) }
                    _events.send(LegalHelpEvent.BookingDone)
                    load(showSkeleton = false)
                }
                is AsosResult.Error -> if (result.code == AsosErrorCode.UNAUTHORIZED) {
                    _uiState.update { it.copy(bookingDialog = null) }
                    _events.send(LegalHelpEvent.NeedsSignIn)
                } else {
                    // e.g. 409 "Bu vaqt band": show the server's text in the dialog.
                    _uiState.update { state ->
                        state.copy(bookingDialog = state.bookingDialog?.copy(isSending = false, errorMessage = result.message))
                    }
                }
            }
        }
    }

    private fun AsosResult.Error.toEvent(): LegalHelpEvent =
        if (code == AsosErrorCode.UNAUTHORIZED) LegalHelpEvent.NeedsSignIn else LegalHelpEvent.ShowError(message)

    internal companion object {
        /** +998 and 9 digits. */
        fun isValidUzPhone(phone: String): Boolean = Regex("""\+998\d{9}""").matches(phone)
    }
}

// ---------------------------------------------------------------------- mapping to UI models

private fun Lawyer.toUi(today: java.time.LocalDate) = LawyerUi(
    id = id,
    name = UzbekFormatters.display(name),
    initials = initials,
    specialties = specialties.map { UzbekFormatters.display(it) },
    specialtyLine = LegalHelpFormat.specialtyLine(specialties),
    experienceYears = years,
    ratingLabel = rating?.let { LegalHelpFormat.rating(it) },
    reviewCount = reviewsCount,
    isOnline = isOnline,
    isVerified = isVerified,
    nextFree = if (isOnline) null else LegalHelpFormat.freeSlot(nextFreeAt, today),
    avatarTone = AvatarTone.forId(id),
)

private fun Hotline.toUi() = HotlineUi(
    displayNumber = phone,
    dialNumber = LegalHelpFormat.dialNumber(phone),
    hours = hours,
    everyDay = everyDay,
    isFree = isFree,
    isOpenNow = isOpen,
    note = note?.let { UzbekFormatters.display(it) },
)

private fun AssistantAnswer.toUi() = AssistantAnswerUi(
    text = UzbekFormatters.display(answer),
    articles = articles.map {
        AnswerArticleUi(
            number = it.number,
            title = UzbekFormatters.display(it.title),
            hint = it.bullets.firstOrNull()?.let { bullet -> UzbekFormatters.display(bullet) },
        )
    },
)
