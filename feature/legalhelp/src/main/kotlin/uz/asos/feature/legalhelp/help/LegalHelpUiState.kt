package uz.asos.feature.legalhelp.help

import uz.asos.feature.legalhelp.ui.AvatarTone

data class LegalHelpUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isSignedIn: Boolean = false,
    val quote: QuoteUi? = null,
    val assistant: AssistantUi = AssistantUi(),
    val hotline: HotlineUi? = null,
    val lawyers: List<LawyerUi> = emptyList(),
    val lawyersOnline: Int? = null,
    val specialties: List<String> = emptyList(),
    val selectedSpecialty: String? = null,
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
    val title: String = "",
    val subtitle: String = "",
    val suggestions: List<String> = emptyList(),
    val disclaimer: String = "",
    val input: String = "",
    val isSending: Boolean = false,
    val answer: AssistantAnswerUi? = null,
    val errorMessage: String? = null,
    val isTooShort: Boolean = false,
    val needsSignIn: Boolean = false,
) {
    val canSend: Boolean get() = input.isNotBlank() && !isSending

    companion object {
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
    val hint: String?,
)

data class HotlineUi(
    val displayNumber: String,
    val dialNumber: String,
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
    val specialtyLine: String,
    val experienceYears: Int,
    val ratingLabel: String?,
    val reviewCount: Int,
    val isOnline: Boolean,
    val isVerified: Boolean,
    val nextFree: FreeSlotUi?,
    val avatarTone: AvatarTone,
)

data class FreeSlotUi(
    val raw: String,
    val isToday: Boolean,
    val dateLabel: String,
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
