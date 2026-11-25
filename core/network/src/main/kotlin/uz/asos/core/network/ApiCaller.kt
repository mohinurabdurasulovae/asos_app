/*
 * ApiCaller.kt
 * Module: :core:network   ·   Work package: WP-02
 *
 * Runs one Retrofit call and turns every outcome into AsosResult:
 *  - success                → AsosResult.Success
 *  - HTTP error with JSON    → AsosResult.Error(message = API "message", code = API "error")
 *  - no connection / timeout → AsosResult.Error(code = network)
 * The API's Uzbek "message" is safe to show unchanged (openapi.yaml, Umumiy qoidalar).
 */
package uz.asos.core.network

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import uz.asos.core.common.result.AsosErrorCode
import uz.asos.core.common.result.AsosResult
import uz.asos.core.network.dto.ErrorDto
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class ApiCaller @Inject constructor(
    private val json: Json,
) {
    suspend fun <T> call(block: suspend () -> T): AsosResult<T> = try {
        AsosResult.Success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: HttpException) {
        httpError(e)
    } catch (e: IOException) {
        AsosResult.Error(message = MSG_NETWORK, code = AsosErrorCode.NETWORK, cause = e)
    } catch (e: SerializationException) {
        AsosResult.Error(message = MSG_BAD_RESPONSE, cause = e)
    } catch (e: Exception) {
        AsosResult.Error(message = MSG_UNKNOWN, cause = e)
    }

    private fun httpError(e: HttpException): AsosResult.Error {
        val status = e.code()
        val body = runCatching { e.response()?.errorBody()?.string() }.getOrNull()
        val parsed = body?.let { runCatching { json.decodeFromString(ErrorDto.serializer(), it) }.getOrNull() }

        val code = parsed?.error ?: when (status) {
            401 -> AsosErrorCode.UNAUTHORIZED
            404 -> AsosErrorCode.NOT_FOUND
            429 -> AsosErrorCode.RATE_LIMITED
            else -> AsosErrorCode.UNKNOWN
        }
        val message = parsed?.message?.takeIf { it.isNotBlank() } ?: when (status) {
            401 -> MSG_UNAUTHORIZED
            404 -> MSG_NOT_FOUND
            429 -> MSG_RATE_LIMITED
            in 500..599 -> MSG_SERVER
            else -> MSG_UNKNOWN
        }
        return AsosResult.Error(message = message, code = code, httpStatus = status, cause = e)
    }

    private companion object {
        const val MSG_NETWORK = "Internet aloqasi yo‘q. Ulanishni tekshirib, qayta urinib ko‘ring."
        const val MSG_UNAUTHORIZED = "Avval tizimga kiring."
        const val MSG_NOT_FOUND = "Ma’lumot topilmadi."
        const val MSG_RATE_LIMITED = "Juda ko‘p so‘rov yuborildi. Birozdan keyin qayta urinib ko‘ring."
        const val MSG_SERVER = "Serverda xatolik yuz berdi. Birozdan keyin qayta urinib ko‘ring."
        const val MSG_BAD_RESPONSE = "Server javobini o‘qib bo‘lmadi."
        const val MSG_UNKNOWN = "Nimadir xato ketdi. Qayta urinib ko‘ring."
    }
}
