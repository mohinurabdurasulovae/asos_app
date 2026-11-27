package uz.asos.huquqiyyordam

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

internal class GeminiException(message: String) : IOException(message)

internal class GeminiClient(
    private val endpoint: URI = URI("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash-lite:generateContent"),
) {
    suspend fun generateJson(
        key: String,
        instructions: String,
        requestData: JSONObject,
        schema: JSONObject,
        outputTokens: Int,
    ): JSONObject = withContext(Dispatchers.IO) {
        val body = JSONObject()
            .put("systemInstruction", JSONObject().put("parts", JSONArray().put(JSONObject().put("text", instructions))))
            .put("contents", JSONArray().put(content("user", requestData.toString())))
            .put("generationConfig", JSONObject()
                .put("maxOutputTokens", outputTokens)
                .put("responseMimeType", "application/json")
                .put("responseJsonSchema", schema))

        val connection = endpoint.toURL().openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "POST"
            connection.instanceFollowRedirects = false
            connection.connectTimeout = 10_000
            connection.readTimeout = 90_000
            connection.setRequestProperty("Accept", "application/json")
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
            connection.setRequestProperty("x-goog-api-key", key)
            connection.doOutput = true
            connection.outputStream.use { it.write(body.toString().toByteArray(Charsets.UTF_8)) }

            val status = connection.responseCode
            val stream = if (status in 200..299) connection.inputStream else connection.errorStream
            val raw = stream?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }.orEmpty()
            val json = try { JSONObject(raw) } catch (_: JSONException) { null }
            if (status !in 200..299) throw geminiError(status, json?.optJSONObject("error"))

            val candidate = json?.optJSONArray("candidates")?.optJSONObject(0)
            val finishReason = candidate?.optString("finishReason")
            if (json?.optJSONObject("promptFeedback")?.optString("blockReason")
                    ?.takeIf(String::isNotBlank) != null ||
                finishReason in setOf("SAFETY", "BLOCKLIST", "PROHIBITED_CONTENT", "SPII")
            ) {
                throw GeminiException("Gemini bu savolga javob bera olmadi. Savolni boshqacha yozib ko‘ring.")
            }
            val parts = candidate?.optJSONObject("content")?.optJSONArray("parts")
            val text = buildString {
                if (parts != null) for (index in 0 until parts.length()) {
                    val part = parts.optJSONObject(index) ?: continue
                    if (!part.optBoolean("thought") && !part.isNull("text")) append(part.optString("text"))
                }
            }.trim()
            if (text.isEmpty()) throw GeminiException("Gemini bo‘sh javob qaytardi. Qayta urinib ko‘ring.")
            if (finishReason == "MAX_TOKENS") {
                throw GeminiException("Gemini javobi yakunlanmadi. Savolni qisqartirib ko‘ring.")
            }
            try { JSONObject(text) } catch (_: JSONException) {
                throw GeminiException("Gemini javobini tekshirib bo‘lmadi. Qayta urinib ko‘ring.")
            }
        } finally {
            connection.disconnect()
        }
    }

    private fun content(role: String, text: String): JSONObject =
        JSONObject().put("role", role).put("parts", JSONArray().put(JSONObject().put("text", text)))

    private fun geminiError(code: Int, error: JSONObject?): GeminiException {
        val details = error?.optJSONArray("details")
        val keyRejected = details != null && (0 until details.length()).any {
            details.optJSONObject(it)?.optString("reason") in
                setOf("API_KEY_INVALID", "API_KEY_EXPIRED", "API_KEY_SERVICE_BLOCKED", "API_KEY_INVALID_OR_UNAUTHORIZED")
        }
        return when {
            keyRejected || code == 401 || code == 403 ||
                (code == 400 && error?.optString("message")?.contains("API key not valid", ignoreCase = true) == true) ->
                GeminiException("Gemini xizmatiga ruxsat berilmadi.")
            code == 429 -> GeminiException("Gemini so‘rov limiti tugagan. Keyinroq qayta urinib ko‘ring.")
            code >= 500 -> GeminiException("Gemini vaqtincha javob bera olmayapti.")
            else -> GeminiException("Gemini so‘rovni bajara olmadi (HTTP $code).")
        }
    }

}
