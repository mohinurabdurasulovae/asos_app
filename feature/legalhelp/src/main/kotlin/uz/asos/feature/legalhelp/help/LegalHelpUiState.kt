/*
 * LegalHelpUiState.kt
 * Module: :feature:legalhelp   ·   Work package: WP-10
 * Screen: Huquqiy yordam (design artboard: Yordam.dc.html)
 *
 * Immutable state the screen renders. Texts the API sends (AI card, errors) are kept as-is;
 * labels that need string resources are built in the composables.
 */
package uz.asos.feature.legalhelp.help

import uz.asos.feature.legalhelp.ui.AvatarTone

data class LegalHelpUiState(
    /** First load (skeleton). */
    val isLoading: Boolean = true,
    /** First load failed and there is nothing to show. */
    val errorMessage: String? = null,
    val isSignedIn: Boolean = false,
    /** Article 29 quote. null = use the built-in text. */
    val quote: QuoteUi? = null,
    val assistant: AssistantUi = AssistantUi(),
    val hotline: HotlineUi? = null,
    val lawyers: List<LawyerUi> = emptyList(),
    /** From the API; null when it sends none (then we count the list). */
    val lawyersOnline: Int? = null,
    /** Filter chips after "Barchasi". */
    val specialties: List<String> = emptyList(),
    /** null = "Barchasi". */
    val selectedSpecialty: String? = null,
    /** Lawyer whose chat is being opened (disables "Yozish" to avoid double taps). */
    val openingChatForLawyerId: Int? = null,
    val callbackDialog: CallbackDialogUi? = null,
    val bookingDialog: BookingDialogUi? = null,
) {
    val visibleLawyers: List<LawyerUi>
        get() = selectedSpecialty?.let { selected ->
            lawyers.filter { lawyer -> lawyer.specialties.any { it.equals(selected, ignoreCase = true) } }
        } ?: lawyers

    val onlineCount: Int get() = lawyersOnline ?: lawyers.count { it.isOnline }
}

data class QuoteUi(val articleNumber: Int, val text: String)

data class AssistantUi(
    /** Empty = use the string resource. */
    val title: String = "",
    val subtitle: String = "",
    val suggestions: List<String> = emptyList(),
    val disclaimer: String = "",
    val input: String = "",
    val isSending: Boolean = false,
    val answer: AssistantAnswerUi? = null,
    /** Server error text, shown as-is. */
    val errorMessage: String? = null,
    /** The question is shorter than [MIN_QUESTION_LENGTH]. */
    val isTooShort: Boolean = false,
    /** POST /v1/ai/ask needs a signed-in user. */
    val needsSignIn: Boolean = false,
) {
    val canSend: Boolean get() = input.isNotBlank() && !isSending

    companion object {
        /** openapi: /v1/ai/ask text minLength. */
        const val MIN_QUESTION_LENGTH = 10
    }
}

data class AssistantAnswerUi(
    val text: String,
    val articles: List<AnswerArticleUi>,
)

data class AnswerArticleUi(
    val number: Int,
    val title: String,
    /** First plain-language bullet, if any. */
    val hint: String?,
)

data class HotlineUi(
    /** "+998 (71) 000-00-00" as shown. */
    val displayNumber: String,
    /** "+998710000000" for the dialer. */
    val dialNumber: String,
    /** "09:00–21:00" */
    val hours: String,
    val everyDay: Boolean,
    val isFree: Boolean,
    val isOpenNow: Boolean,
    val note: String?,
)

data class LawyerUi(
    val id: Int,
    val name: String,
    val initials: String,
    val specialties: List<String>,
    /** "Uy-joy va fuqarolik ishlari" */
    val specialtyLine: String,
    val experienceYears: Int,
    /** "4,9", or null when there are no reviews yet. */
    val ratingLabel: String?,
    val reviewCount: Int,
    val isOnline: Boolean,
    val isVerified: Boolean,
    /** Offline lawyers' next free slot. */
    val nextFree: FreeSlotUi?,
    val avatarTone: AvatarTone,
)

data class FreeSlotUi(
    /** Sent back to POST /v1/bookings unchanged. */
    val raw: String,
    val isToday: Boolean,
    /** "21.11" */
    val dateLabel: String,
    /** "15:00" */
    val time: String,
)

data class CallbackDialogUi(
    val phone: String = "+998 ",
    val isSending: Boolean = false,
    val isInvalid: Boolean = false,
    val errorMessage: String? = null,
)

data class BookingDialogUi(
    val lawyerId: Int,
    val lawyerName: String,
    val slot: FreeSlotUi,
    val isSending: Boolean = false,
    val errorMessage: String? = null,
)
