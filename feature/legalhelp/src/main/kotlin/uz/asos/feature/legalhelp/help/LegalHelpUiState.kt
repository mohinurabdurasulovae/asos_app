/*
 * LegalHelpUiState.kt
 * Module: :feature:legalhelp   ·   Work package: WP-10
 * Screen: Huquqiy yordam (design artboard: Yordam.dc.html)
 *
 * Immutable state the screen renders. Numbers are already formatted ("4,9"), labels that
 * need string resources are built in the composables.
 */
package uz.asos.feature.legalhelp.help

import uz.asos.feature.legalhelp.data.temp.AvatarTone
import uz.asos.feature.legalhelp.data.temp.LawyerCategory

data class LegalHelpUiState(
    /** First load (skeleton). */
    val isLoading: Boolean = true,
    /** First load failed and there is nothing to show. */
    val errorMessage: String? = null,
    val assistant: AssistantUi = AssistantUi(),
    val hotline: HotlineUi? = null,
    val lawyers: List<LawyerUi> = emptyList(),
    /** null = "Barchasi". */
    val selectedCategory: LawyerCategory? = null,
) {
    val visibleLawyers: List<LawyerUi>
        get() = if (selectedCategory == null) lawyers else lawyers.filter { selectedCategory in it.categories }

    val onlineCount: Int get() = lawyers.count { it.isOnline }
}

data class AssistantUi(
    val input: String = "",
    val isSending: Boolean = false,
    val answer: AssistantAnswerUi? = null,
    val errorMessage: String? = null,
) {
    val canSend: Boolean get() = input.isNotBlank() && !isSending
}

data class AssistantAnswerUi(
    val text: String,
    val articleNumber: Int,
    val articleTitle: String,
)

data class HotlineUi(
    val displayNumber: String,
    val dialNumber: String,
    val opensAt: String,
    val closesAt: String,
    val isFree: Boolean,
    val isOpenNow: Boolean,
)

data class LawyerUi(
    val id: Int,
    val name: String,
    val initials: String,
    val specialty: String,
    val experienceYears: Int,
    /** "4,9", or null when there are no reviews yet. */
    val ratingLabel: String?,
    val reviewCount: Int,
    val isOnline: Boolean,
    val isVerified: Boolean,
    /** "15:00" for offline lawyers with a free slot today. */
    val nextFreeTime: String?,
    val categories: Set<LawyerCategory>,
    val avatarTone: AvatarTone,
)
