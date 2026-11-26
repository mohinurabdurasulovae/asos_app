/*
 * TempLegalHelpModels.kt
 * Module: :feature:legalhelp   ·   Work package: WP-10
 *
 * TODO(WP-02): TEMPORARY. These belong in core:model (LegalHelpModels.kt), which is still a
 * placeholder. When WP-02 lands, delete this file and import uz.asos.core.model.legalhelp.*
 * instead. Field names follow docs/api/openapi.yaml where possible, so the swap is small.
 */
package uz.asos.feature.legalhelp.data.temp

/** Filter chips under "Advokatlar". */
enum class LawyerCategory { HOUSING, FAMILY, LABOR, EDUCATION }

/** Avatar colours in the design: SR navy, MY light blue, BK orange. */
enum class AvatarTone { NAVY, BRAND, ORANGE }

data class Lawyer(
    val id: Int,
    val fullName: String,
    val initials: String,
    /** "Uy-joy va fuqarolik ishlari" */
    val specialty: String,
    val experienceYears: Int,
    /** null = no rating yet. */
    val rating: Double?,
    val reviewCount: Int,
    val isOnline: Boolean,
    val isVerified: Boolean,
    /** "15:00" when the lawyer is offline but has a free slot today. */
    val nextFreeTime: String?,
    val categories: Set<LawyerCategory>,
    val avatarTone: AvatarTone,
)

data class Hotline(
    /** "+998 (71) 000-00-00" as shown on screen. */
    val displayNumber: String,
    /** "+998710000000" for the dialer. */
    val dialNumber: String,
    val opensAt: String,
    val closesAt: String,
    val isFree: Boolean,
    val isOpenNow: Boolean,
)

data class AssistantAnswer(
    val text: String,
    val articleNumber: Int,
    val articleTitle: String,
)

data class ChatFile(
    val fileName: String,
    /** "24 KB · Word" */
    val meta: String,
)

data class ChatArticleLink(
    val articleNumber: Int,
    val title: String,
)

data class ChatMessage(
    val id: Long,
    val fromMe: Boolean,
    val text: String,
    /** "14:02" */
    val time: String,
    val isRead: Boolean = false,
    val file: ChatFile? = null,
    val article: ChatArticleLink? = null,
)

data class ChatThread(
    val chatId: Int,
    val lawyer: Lawyer,
    val messages: List<ChatMessage>,
)
