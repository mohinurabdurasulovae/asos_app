package uz.asos.huquqiyyordam

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.json.JSONException
import java.io.IOException

internal const val AssistantQuestionMin = 1
internal const val AssistantQuestionLimit = 2_000

internal data class AssistantChatState(
    val turns: List<AssistantTurn> = emptyList(),
    val pendingQuestion: String? = null,
    val sending: Boolean = false,
    val error: String? = null,
)

internal sealed interface HelpDialog {
    data class Login(val phone: String? = null, val devCode: String? = null) : HelpDialog
    data object Callback : HelpDialog
    data class Chat(val lawyer: Lawyer, val id: Long? = null, val messages: List<LawyerMessage> = emptyList()) : HelpDialog
    data class Booking(val lawyer: Lawyer, val slot: String) : HelpDialog
    data class BookingUnavailable(val lawyer: Lawyer) : HelpDialog
    data class Notice(val title: String, val text: String) : HelpDialog
}

internal class AssistantViewModel : ViewModel() {
    private val api = AsosApi()
    private val legalAssistant = LegalAssistant(GeminiClient())
    private var token: String? = null
    private var afterLogin: (() -> Unit)? = null

    var state by mutableStateOf(AssistantChatState())
        private set
    var help by mutableStateOf<HelpPage?>(null)
        private set
    var helpError by mutableStateOf<String?>(null)
        private set
    var loadingHelp by mutableStateOf(false)
        private set
    var lawyers by mutableStateOf<List<Lawyer>?>(null)
        private set
    var lawyersError by mutableStateOf<String?>(null)
        private set
    private var loadingLawyers = false

    var dialog by mutableStateOf<HelpDialog?>(null)
        private set
    var busy by mutableStateOf(false)
        private set
    var dialogError by mutableStateOf<String?>(null)
        private set

    val chatHasSession: Boolean
        get() = token != null

    init {
        loadHelp()
        loadLawyers()
    }

    fun loadHelp() {
        if (loadingHelp) return
        loadingHelp = true
        helpError = null
        viewModelScope.launch {
            try {
                help = api.help()
            } catch (error: IOException) {
                helpError = error.displayMessage()
            } catch (_: JSONException) {
                helpError = invalidResponse
            } finally {
                loadingHelp = false
            }
        }
    }

    fun loadLawyers() {
        if (loadingLawyers) return
        loadingLawyers = true
        lawyersError = null
        viewModelScope.launch {
            try {
                lawyers = api.lawyers()
            } catch (error: IOException) {
                lawyersError = error.displayMessage()
            } catch (_: JSONException) {
                lawyersError = invalidResponse
            } finally {
                loadingLawyers = false
            }
        }
    }

    fun send(question: String) {
        val text = question.trim()
        if (state.sending || text.length !in AssistantQuestionMin..AssistantQuestionLimit) return
        state = state.copy(pendingQuestion = text, error = null, sending = true)
        val key = BuildConfig.GEMINI_API_KEY
        if (key.isBlank()) {
            showGeminiError("AI yordamchi hozircha sozlanmagan.")
            return
        }
        viewModelScope.launch {
            try {
                val reply = legalAssistant.ask(key, text, state.turns)
                state = AssistantChatState(turns = (state.turns + AssistantTurn(text, reply)).takeLast(20))
            } catch (error: GeminiException) {
                showGeminiError(error.message.orEmpty())
            } catch (_: IOException) {
                showGeminiError("Gemini’ga ulanib bo‘lmadi. Qayta urinib ko‘ring.")
            } catch (_: JSONException) {
                showGeminiError("Gemini javobini o‘qib bo‘lmadi. Qayta urinib ko‘ring.")
            }
        }
    }

    fun retry() { state.pendingQuestion?.let(::send) }

    private fun showGeminiError(message: String) {
        state = state.copy(sending = false, error = message)
    }

    fun requestCode(phone: String) = dialogRequest {
        val code = api.requestCode(phone)
        dialog = HelpDialog.Login(phone, code)
    }

    fun changePhone() { if (!busy) show(HelpDialog.Login()) }

    fun verify(code: String) {
        val phone = (dialog as? HelpDialog.Login)?.phone ?: return
        dialogRequest(onSuccess = {
            dialog = null
            val action = afterLogin
            afterLogin = null
            action?.invoke()
        }) {
            token = api.verify(phone, code)
        }
    }

    fun openCallback() = show(HelpDialog.Callback)

    fun requestCallback(phone: String, topic: String) = dialogRequest {
        api.callback(phone, topic)
        dialog = HelpDialog.Notice("So‘rov yuborildi", "Ishonch telefoni jamoasi sizga qo‘ng‘iroq qiladi.")
    }

    fun openChat(lawyer: Lawyer) {
        if (busy) return
        val chat = HelpDialog.Chat(lawyer)
        show(chat)
        val access = token ?: return
        connectChat(chat, access)
    }

    private fun connectChat(chat: HelpDialog.Chat, access: String) {
        val lawyerId = chat.lawyer.id.toLongOrNull() ?: return
        dialogRequest {
            val id = api.openConversation(lawyerId, access)
            dialog = chat.copy(id = id)
            dialog = chat.copy(id = id, messages = api.messages(id, access))
        }
    }

    fun refreshChat() {
        val chat = dialog as? HelpDialog.Chat ?: return
        val access = token ?: return
        val id = chat.id
        if (id == null) {
            connectChat(chat, access)
            return
        }
        dialogRequest { dialog = chat.copy(messages = api.messages(id, access)) }
    }

    fun sendMessage(text: String, onSent: () -> Unit) {
        val chat = dialog as? HelpDialog.Chat ?: return
        val id = chat.id ?: return
        val access = token ?: return
        dialogRequest {
            val message = api.sendMessage(id, text, access)
            dialog = chat.copy(messages = (chat.messages + message).distinctBy { it.id }.sortedBy { it.id })
            onSent()
        }
    }

    fun openBooking(lawyer: Lawyer) {
        val slot = lawyer.nextFreeAt?.takeIf(::isFutureSlot)
        if (slot == null) {
            show(HelpDialog.BookingUnavailable(lawyer))
            return
        }
        authorize({ openBooking(lawyer) }) { show(HelpDialog.Booking(lawyer, slot)) }
    }

    fun book() {
        val booking = dialog as? HelpDialog.Booking ?: return
        val id = booking.lawyer.id.toLongOrNull() ?: return
        if (!isFutureSlot(booking.slot)) {
            dialogError = "Bu vaqt o‘tib ketgan. Advokatlar ro‘yxatini yangilang."
            loadHelp()
            return
        }
        authorize({ openBooking(booking.lawyer) }) { access ->
            dialogRequest({ openBooking(booking.lawyer) }) {
                api.book(id, booking.slot, access)
                dialog = HelpDialog.Notice("Band qilish so‘rovi yuborildi", booking.lawyer.name)
                loadHelp()
            }
        }
    }

    suspend fun loadArticle(number: Int): ArticleContent = api.article(number)

    fun show(value: HelpDialog) {
        if (busy) return
        dialogError = null
        dialog = value
    }

    fun closeDialog() {
        if (busy) return
        dialog = null
        afterLogin = null
        dialogError = null
    }

    private fun authorize(resume: () -> Unit, action: (String) -> Unit) {
        val access = token
        if (access == null) {
            if (!busy) requireLogin(resume)
        } else {
            action(access)
        }
    }

    private fun requireLogin(resume: (() -> Unit)?) {
        token = null
        afterLogin = resume
        dialogError = null
        dialog = HelpDialog.Login()
    }

    private fun dialogRequest(
        onUnauthorized: (() -> Unit)? = null,
        onSuccess: () -> Unit = {},
        request: suspend () -> Unit,
    ) {
        if (busy) return
        dialogError = null
        busy = true
        viewModelScope.launch {
            val completed = try {
                request()
                true
            } catch (error: IOException) {
                if (onUnauthorized != null && error is AsosApiException && error.status == 401) {
                    requireLogin(onUnauthorized)
                } else {
                    dialogError = error.displayMessage()
                }
                false
            } catch (_: JSONException) {
                dialogError = invalidResponse
                false
            } finally {
                busy = false
            }
            if (completed) onSuccess()
        }
    }
}

private const val invalidResponse = "Asos API javobini o‘qib bo‘lmadi. Qayta urinib ko‘ring."

private fun IOException.displayMessage(): String =
    if (this is AsosApiException) message.orEmpty()
    else "Asos API serveriga ulanib bo‘lmadi. Internetni tekshirib, qayta urinib ko‘ring."
