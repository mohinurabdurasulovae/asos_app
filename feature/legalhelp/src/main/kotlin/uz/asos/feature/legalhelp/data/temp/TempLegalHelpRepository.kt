/*
 * TempLegalHelpRepository.kt
 * Module: :feature:legalhelp   ·   Work package: WP-10
 *
 * TODO(WP-02): TEMPORARY. core:data LegalHelpRepository and core:testing
 * FakeLegalHelpRepository are still placeholders. Until they land, this module runs on the
 * in-memory data below. Swap plan:
 *   1. WP-02 implements uz.asos.core.data.repository.LegalHelpRepository with these functions.
 *   2. Uncomment implementation(project(":core:data")) in build.gradle.kts.
 *   3. Change the ViewModel constructor types, delete data/temp/.
 *
 * API it stands in for (docs/api/openapi.yaml):
 *   GET /lawyers · GET /legal-help/hotline · POST /assistant/messages
 *   GET /chats/{chatId} · GET /chats/{chatId}/messages · POST /chats/{chatId}/messages
 */
package uz.asos.feature.legalhelp.data.temp

import kotlinx.coroutines.delay
import uz.asos.core.common.result.AsosResult
import uz.asos.core.common.result.AsosErrorCode
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

interface TempLegalHelpRepository {
    suspend fun getLawyers(): AsosResult<List<Lawyer>>
    suspend fun getHotline(): AsosResult<Hotline>
    suspend fun askAssistant(question: String): AsosResult<AssistantAnswer>
    suspend fun getChat(chatId: Int): AsosResult<ChatThread>

    /** Stores my message and returns it with its id and time. */
    suspend fun sendMessage(chatId: Int, text: String): AsosResult<ChatMessage>

    /** The lawyer's answer to [text]. Mock only: the real chat gets it over the network. */
    suspend fun awaitLawyerReply(chatId: Int, text: String): AsosResult<ChatMessage>
}

/**
 * In-memory data matching the design canvas (Yordam, Chat).
 * @param latencyMs fake network delay; tests pass 0.
 */
@Singleton
class InMemoryLegalHelpRepository(
    private val latencyMs: Long,
) : TempLegalHelpRepository {

    @Inject constructor() : this(latencyMs = 350L)

    private var nextId = 100L
    private val sentMessages = mutableMapOf<Int, MutableList<ChatMessage>>()

    override suspend fun getLawyers(): AsosResult<List<Lawyer>> {
        delay(latencyMs)
        return AsosResult.Success(LAWYERS)
    }

    override suspend fun getHotline(): AsosResult<Hotline> {
        delay(latencyMs)
        return AsosResult.Success(HOTLINE)
    }

    override suspend fun askAssistant(question: String): AsosResult<AssistantAnswer> {
        delay(latencyMs * 2)
        val q = question.lowercase()
        val answer = when {
            listOf("ijara", "kvartira", "uy egasi", "uy-joy", "uyga").any { it in q } -> AssistantAnswer(
                text = "Uy-joy daxlsiz. Ijarachi yashayotgan kvartiraga uy egasi ham sizning roziligingizsiz " +
                    "kira olmaydi. Avval uy egasiga yozma ogohlantirish yuboring.",
                articleNumber = 31,
                articleTitle = "Shaxsiy hayot va uy-joy daxlsizligi",
            )
            listOf("maosh", "ish haqi", "ish beruvchi", "ishdan", "mehnat").any { it in q } -> AssistantAnswer(
                text = "Har kim munosib mehnat va o‘z vaqtida to‘lanadigan ish haqi huquqiga ega. " +
                    "Kechikish sanalarini yozib boring va ish beruvchiga yozma murojaat qiling.",
                articleNumber = 42,
                articleTitle = "Mehnat qilish huquqi",
            )
            listOf("talaba", "o‘qish", "o'qish", "ta’lim", "ta'lim", "universitet", "maktab").any { it in q } ->
                AssistantAnswer(
                    text = "Har kim ta’lim olish huquqiga ega. O‘quv muassasasi bilan nizo bo‘lsa, " +
                        "avval rasmiy ariza yozing va javobni yozma so‘rang.",
                    articleNumber = 50,
                    articleTitle = "Ta’lim olish huquqi",
                )
            else -> AssistantAnswer(
                text = "Vaziyatingizga eng yaqin modda topildi. Aniq maslahat uchun advokatga yozing.",
                articleNumber = 54,
                articleTitle = "Huquq va erkinliklar kafolatlari",
            )
        }
        return AsosResult.Success(answer)
    }

    override suspend fun getChat(chatId: Int): AsosResult<ChatThread> {
        delay(latencyMs)
        val lawyer = LAWYERS.firstOrNull { it.id == chatId }
            ?: return AsosResult.Error("Suhbat topilmadi", code = AsosErrorCode.NOT_FOUND)
        val seeded = if (lawyer.id == SARDOR_ID) SARDOR_CHAT else emptyList()
        return AsosResult.Success(
            ChatThread(chatId = chatId, lawyer = lawyer, messages = seeded + sentMessages[chatId].orEmpty()),
        )
    }

    override suspend fun sendMessage(chatId: Int, text: String): AsosResult<ChatMessage> {
        val message = ChatMessage(id = nextId++, fromMe = true, text = text.trim(), time = now(), isRead = true)
        sentMessages.getOrPut(chatId) { mutableListOf() } += message
        return AsosResult.Success(message)
    }

    override suspend fun awaitLawyerReply(chatId: Int, text: String): AsosResult<ChatMessage> {
        val q = text.lowercase()
        val reply = when {
            "rahmat" in q -> "Arzimaydi! Savol bo‘lsa, yozavering."
            "keyingi" in q || "qadam" in q ->
                "Ogohlantirish xatini imzolab, uy egasiga topshiring va nusxasini saqlang. " +
                    "7 kun ichida javob bo‘lmasa, mahalla yoki sudga murojaat qilamiz."
            "qo‘ng‘iroq" in q || "qo'ng'iroq" in q || "telefon" in q ->
                "Albatta. Bugun 18:00 gacha qo‘ng‘iroq qilishingiz mumkin."
            else -> "Tushunarli. Batafsil ko‘rib chiqib, tez orada javob beraman."
        }
        val message = ChatMessage(id = nextId++, fromMe = false, text = reply, time = now())
        sentMessages.getOrPut(chatId) { mutableListOf() } += message
        return AsosResult.Success(message)
    }

    private fun now(): String = LocalTime.now().format(TIME_FORMAT)

    companion object {
        private val TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm")
        const val SARDOR_ID = 1

        val HOTLINE = Hotline(
            displayNumber = "+998 (71) 000-00-00",
            dialNumber = "+998710000000",
            opensAt = "09:00",
            closesAt = "21:00",
            isFree = true,
            isOpenNow = true,
        )

        val LAWYERS = listOf(
            Lawyer(
                id = SARDOR_ID,
                fullName = "Sardor Rahimov",
                initials = "SR",
                specialty = "Uy-joy va fuqarolik ishlari",
                experienceYears = 9,
                rating = 4.9,
                reviewCount = 210,
                isOnline = true,
                isVerified = true,
                nextFreeTime = null,
                categories = setOf(LawyerCategory.HOUSING),
                avatarTone = AvatarTone.NAVY,
            ),
            Lawyer(
                id = 2,
                fullName = "Malika Yusupova",
                initials = "MY",
                specialty = "Oila va ta’lim huquqi",
                experienceYears = 12,
                rating = 4.8,
                reviewCount = 164,
                isOnline = true,
                isVerified = true,
                nextFreeTime = null,
                categories = setOf(LawyerCategory.FAMILY, LawyerCategory.EDUCATION),
                avatarTone = AvatarTone.BRAND,
            ),
            Lawyer(
                id = 3,
                fullName = "Bekzod Karimov",
                initials = "BK",
                specialty = "Mehnat huquqi",
                experienceYears = 6,
                rating = null,
                reviewCount = 0,
                isOnline = false,
                isVerified = true,
                nextFreeTime = "15:00",
                categories = setOf(LawyerCategory.LABOR),
                avatarTone = AvatarTone.ORANGE,
            ),
        )

        val SARDOR_CHAT = listOf(
            ChatMessage(
                id = 1,
                fromMe = true,
                text = "Assalomu alaykum. Uy egasi men yo‘qligimda kvartiraga kirib turibdi.",
                time = "14:02",
                isRead = true,
            ),
            ChatMessage(
                id = 2,
                fromMe = false,
                text = "Va alaykum assalom, Aziza. Ijara shartnomangiz notarial tasdiqlanganmi?",
                time = "14:05",
            ),
            ChatMessage(id = 3, fromMe = true, text = "Yo‘q, oddiy yozma shartnoma.", time = "14:06", isRead = true),
            ChatMessage(
                id = 4,
                fromMe = false,
                text = "Tushunarli. Birinchi qadam — uy egasiga yozma ogohlantirish. " +
                    "Namunani va tayanadigan moddani yubordim:",
                time = "14:08",
                file = ChatFile(fileName = "Ogohlantirish_xati.docx", meta = "24 KB · Word"),
                article = ChatArticleLink(articleNumber = 31, title = "Shaxsiy hayot va uy-joy daxlsizligi"),
            ),
        )
    }
}
