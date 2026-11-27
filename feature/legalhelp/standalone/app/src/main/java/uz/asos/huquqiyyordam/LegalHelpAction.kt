package uz.asos.huquqiyyordam

/** Actions the host app can connect to its navigation or services. */
sealed interface LegalHelpAction {
    data object Back : LegalHelpAction
    data class SubmitQuestion(val question: String) : LegalHelpAction
    data class CallHotline(val number: String) : LegalHelpAction
    data object RequestCallback : LegalHelpAction
    data class Chat(val lawyerId: String) : LegalHelpAction
    data class Book(val lawyerId: String) : LegalHelpAction
}

internal enum class Specialty(val label: String) {
    All("Barchasi"),
    Housing("Uy-joy"),
    Family("Oila"),
    Labor("Mehnat"),
    Education("Ta’lim");

    fun matches(value: String): Boolean {
        val lower = value.lowercase()
        return when (this) {
            All -> true
            Housing -> "uy" in lower || "ijara" in lower || "housing" in lower
            Family -> "oila" in lower || "family" in lower
            Labor -> "mehnat" in lower || "labor" in lower
            Education -> "ta’lim" in lower || "ta'lim" in lower || "taʼlim" in lower || "talim" in lower || "education" in lower
        }
    }
}

internal data class Lawyer(
    val id: String,
    val initials: String,
    val name: String,
    val experience: String,
    val specialties: Set<Specialty>,
    val rating: String? = null,
    val availability: String? = null,
    val online: Boolean = false,
    val verified: Boolean = true,
    val nextFreeAt: String? = null,
)

internal val previewLawyers = listOf(
    Lawyer(
        id = "sardor-rahimov",
        initials = "SR",
        name = "Sardor Rahimov",
        experience = "Uy-joy va fuqarolik ishlari · 9 yil",
        specialties = setOf(Specialty.Housing),
        rating = "4,9 · 210 ta baho",
        online = true,
    ),
    Lawyer(
        id = "malika-yusupova",
        initials = "MY",
        name = "Malika Yusupova",
        experience = "Oila va ta’lim huquqi · 12 yil",
        specialties = setOf(Specialty.Family, Specialty.Education),
        rating = "4,8 · 164 ta baho",
        online = true,
    ),
    Lawyer(
        id = "bekzod-karimov",
        initials = "BK",
        name = "Bekzod Karimov",
        experience = "Mehnat huquqi · 6 yil",
        specialties = setOf(Specialty.Labor),
        availability = "Bugun 15:00 dan bo‘sh",
    ),
)

internal val questionSuggestions = linkedMapOf(
    "Ish haqi kechikdi" to "Ish beruvchim ish haqimni vaqtida to‘lamayapti.",
    "Ijara muammosi" to "Ijara shartnomam bo‘yicha muammoga duch keldim.",
    "Talaba huquqlari" to "Talaba sifatida huquqlarimni bilmoqchiman.",
)
