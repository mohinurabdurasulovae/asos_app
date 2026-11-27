package uz.asos.core.common.result

sealed interface AsosResult<out T> {

    data class Success<out T>(val data: T) : AsosResult<T>

    data class Error(
        val message: String,
        val code: String = AsosErrorCode.UNKNOWN,
        val httpStatus: Int? = null,
        val cause: Throwable? = null,
    ) : AsosResult<Nothing>
}

object AsosErrorCode {
    const val UNKNOWN = "unknown"
    const val NETWORK = "network"
    const val UNAUTHORIZED = "unauthorized"
    const val NOT_FOUND = "not_found"
    const val RATE_LIMITED = "rate_limited"
}

inline fun <T, R> AsosResult<T>.mapSuccess(transform: (T) -> R): AsosResult<R> = when (this) {
    is AsosResult.Success -> AsosResult.Success(transform(data))
    is AsosResult.Error -> this
}

inline fun <T> AsosResult<T>.onSuccess(action: (T) -> Unit): AsosResult<T> {
    if (this is AsosResult.Success) action(data)
    return this
}

inline fun <T> AsosResult<T>.onError(action: (AsosResult.Error) -> Unit): AsosResult<T> {
    if (this is AsosResult.Error) action(this)
    return this
}

fun <T> AsosResult<T>.getOrNull(): T? = (this as? AsosResult.Success)?.data
