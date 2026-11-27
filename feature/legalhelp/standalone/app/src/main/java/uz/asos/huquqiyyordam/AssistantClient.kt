package uz.asos.huquqiyyordam

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URI
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

internal data class LegalReference(val law: String, val article: String)

internal data class AssistantReply(val text: String, val sources: List<LegalReference> = emptyList())
internal data class AssistantTurn(val question: String, val reply: AssistantReply)
internal data class Hotline(
    val phone: String,
    val hours: String,
    val everyDay: Boolean,
    val isFree: Boolean,
    val isOpen: Boolean,
) {
    val dialable: Boolean
        get() {
            val digits = phone.filter { it in '0'..'9' }
            return digits.length in 9..15 && !digits.endsWith("0000000")
        }
}
internal data class HelpPage(
    val quote: Pair<Int, String>?,
    val aiTitle: String?,
    val aiSubtitle: String?,
    val suggestions: List<String>?,
    val disclaimer: String?,
    val hotline: Hotline?,
)
internal data class LawyerMessage(
    val id: Long,
    val body: String,
    val isMine: Boolean,
    val sentAt: String? = null,
    val readAt: String? = null,
    val attachments: List<ChatAttachment> = emptyList(),
)

internal sealed interface ChatAttachment {
    data class Article(val number: Int, val title: String?) : ChatAttachment
    data class File(val name: String, val bytes: Int, val url: String?) : ChatAttachment
}

internal data class ArticleContent(val title: String?, val parts: List<String>)

internal class AsosApiException(val status: Int, message: String) : IOException(message)

internal class AsosApi(private val baseUrl: String = BuildConfig.ASOS_API_BASE_URL) {
    suspend fun help(): HelpPage {
        val json = request("/v1/help")
        val quote = json.optJSONObject("quote")?.let {
            it.value("body")?.let { body -> it.getInt("number") to body }
        }
        val ai = json.optJSONObject("ai")
        val hotline = json.optJSONObject("hotline")?.toHotline() ?: fallbackHotline()
        return HelpPage(
            quote, ai?.value("title"), ai?.value("subtitle"),
            ai?.optJSONArray("suggestions")?.strings(), ai?.value("disclaimer"), hotline,
        )
    }

    suspend fun lawyers(): List<Lawyer> =
        request("/v1/lawyers").getJSONArray("items").objects().map { lawyer ->
            val name = lawyer.getString("name")
            val specialties = lawyer.optJSONArray("specialties").strings()
            val years = lawyer.optInt("years")
            Lawyer(
                id = lawyer.getLong("id").toString(),
                initials = lawyer.value("initials") ?: name.split(' ').take(2).mapNotNull { it.firstOrNull() }.joinToString(""),
                name = name,
                experience = (specialties.map(::specialtyLabel) + listOfNotNull(years.takeIf { it > 0 }?.let { "$it yil" })).joinToString(" · "),
                specialties = Specialty.entries.filter { it != Specialty.All && specialties.any(it::matches) }.toSet(),
                rating = lawyer.optDouble("rating").takeIf { it.isFinite() && it > 0 }?.let {
                    "${String.format(Locale.ROOT, "%.1f", it).replace('.', ',')} · ${lawyer.optInt("reviewsCount")} ta baho"
                },
                online = lawyer.optBoolean("isOnline"),
                verified = lawyer.optBoolean("isVerified"),
                nextFreeAt = lawyer.value("nextFreeAt")?.takeIf(::isFutureSlot),
            )
        }

    suspend fun requestCode(phone: String): String? =
        request("/v1/auth/request-code", payload = JSONObject().put("phone", phone)).value("devCode")

    suspend fun verify(phone: String, code: String): String =
        request("/v1/auth/verify", payload = JSONObject().put("phone", phone).put("code", code))
            .getJSONObject("token").value("accessToken")?.takeUnless { token ->
                token.any { it.isWhitespace() || it.isISOControl() }
            } ?: throw AsosApiException(0, "Server yaroqsiz kirish tokenini qaytardi.")

    suspend fun callback(phone: String, topic: String) {
        val payload = JSONObject().put("phone", phone)
        if (topic.isNotBlank()) payload.put("topic", topic)
        request("/v1/callback-requests", payload = payload)
    }

    suspend fun openConversation(lawyerId: Long, token: String): Long =
        request("/v1/lawyers/$lawyerId/conversations", token, JSONObject())
            .getJSONObject("conversation").getLong("id")

    suspend fun messages(id: Long, token: String): List<LawyerMessage> =
        request("/v1/conversations/$id", token).optJSONArray("messages").objects().map(::lawyerMessage).sortedBy { it.id }

    suspend fun sendMessage(id: Long, text: String, token: String): LawyerMessage =
        lawyerMessage(request("/v1/conversations/$id/messages", token, JSONObject().put("body", text))
            .getJSONObject("message"))

    suspend fun book(lawyerId: Long, slot: String, token: String) {
        request("/v1/bookings", token, JSONObject().put("lawyerId", lawyerId).put("slotAt", slot))
    }

    suspend fun article(number: Int): ArticleContent {
        val article = request("/v1/articles/$number").getJSONObject("article")
        return ArticleContent(
            article.value("title"),
            article.optJSONArray("parts").objects().mapNotNull { it.value("body") },
        )
    }

    private suspend fun fallbackHotline(): Hotline? = try {
        request("/v1/hotline").optJSONObject("hotline")?.toHotline()
    } catch (_: IOException) {
        null
    } catch (_: org.json.JSONException) {
        null
    }

    private fun lawyerMessage(json: JSONObject): LawyerMessage {
        val attachments = json.optJSONArray("attachments").objects().mapNotNull { attachment ->
            when (attachment.optString("kind")) {
                "article" -> attachment.optJSONObject("article")?.let {
                    val number = it.optInt("number")
                    if (number > 0) ChatAttachment.Article(number, it.value("title")) else null
                }
                "file" -> attachment.value("fileName")?.let { name ->
                    ChatAttachment.File(name, attachment.optInt("bytes"), attachment.value("url")?.let(::fileUrl))
                }
                else -> null
            }
        }
        return LawyerMessage(
            json.getLong("id"), json.optString("body"), json.optBoolean("isMine"),
            json.value("createdAt"), json.value("readAt"), attachments,
        )
    }

    private fun fileUrl(value: String): String? {
        val origin = URI(baseUrl)
        val address = try { origin.resolve(value) } catch (_: IllegalArgumentException) { return null }
        return address.toString().takeIf {
            address.scheme == "https" && address.host.equals(origin.host, ignoreCase = true) &&
                address.port == origin.port && address.userInfo == null
        }
    }

    private suspend fun request(
        path: String,
        token: String? = null,
        payload: JSONObject? = null,
        timeout: Int = 25_000,
    ): JSONObject = withContext(Dispatchers.IO) {
        val connection = URI("$baseUrl$path").toURL().openConnection() as HttpURLConnection
        try {
            connection.instanceFollowRedirects = false
            connection.connectTimeout = 10_000
            connection.readTimeout = timeout
            connection.setRequestProperty("Accept", "application/json")
            token?.let { connection.setRequestProperty("Authorization", "Bearer $it") }
            if (payload != null) {
                val bytes = payload.toString().toByteArray(Charsets.UTF_8)
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
                connection.outputStream.use { it.write(bytes) }
            }
            val status = connection.responseCode
            val stream = if (status in 200..299) connection.inputStream else connection.errorStream
            val raw = stream?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }.orEmpty()
            val json = try { JSONObject(raw) } catch (_: org.json.JSONException) { null }
            if (status !in 200..299 || json?.optBoolean("ok") != true) {
                throw AsosApiException(status, json?.value("message") ?: when (status) {
                    401 -> "Kirish talab qilinadi."
                    429 -> "So‘rovlar limiti. Birozdan keyin urinib ko‘ring."
                    in 200..299 -> "Server javobini o‘qib bo‘lmadi."
                    else -> "Asos API javobida xato (HTTP $status)."
                })
            }
            json
        } finally {
            connection.disconnect()
        }
    }
}

private fun JSONObject.toHotline(): Hotline? = value("phone")?.let { phone ->
    Hotline(phone, optString("hours"), optBoolean("everyDay"), optBoolean("isFree"), optBoolean("isOpen"))
}

private fun JSONObject.value(key: String): String? =
    if (isNull(key)) null else optString(key).trim().takeIf(String::isNotEmpty)

private fun JSONArray?.objects(): List<JSONObject> =
    if (this == null) emptyList() else List(length(), ::optJSONObject).filterNotNull()

private fun JSONArray?.strings(): List<String> =
    if (this == null) emptyList() else (0 until length()).mapNotNull {
        if (isNull(it)) null else optString(it).trim().takeIf(String::isNotEmpty)
    }

private fun specialtyLabel(value: String): String = when (value.lowercase()) {
    "uy-joy" -> "Uy-joy"
    "fuqarolik" -> "Fuqarolik"
    "oila" -> "Oila"
    "talim" -> "Ta’lim"
    "mehnat" -> "Mehnat"
    else -> value
}

private fun slotInstant(value: String): Instant? = try {
    LocalDateTime.parse(value.replace(' ', 'T')).toInstant(ZoneOffset.UTC)
} catch (_: DateTimeParseException) {
    null
}

internal fun isFutureSlot(value: String): Boolean = slotInstant(value)?.isAfter(Instant.now()) == true

internal fun localSlotLabel(value: String): String = slotInstant(value)?.atZone(ZoneId.of("Asia/Tashkent"))
    ?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", Locale.ROOT)) ?: value

internal fun localChatTime(value: String) = slotInstant(value)?.atZone(ZoneId.of("Asia/Tashkent"))
