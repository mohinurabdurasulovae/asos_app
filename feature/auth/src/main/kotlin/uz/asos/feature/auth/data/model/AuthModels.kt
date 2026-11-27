package uz.asos.feature.auth.data.model

enum class AuthProvider(val apiValue: String) {
    GOOGLE("google"),
}

/**
 * Kirish va ro'yxatdan o'tishni ajratadi.
 *
 * Server shu qiymatga qarab tekshiradi: [LOGIN] da email bazada bo'lishi
 * shart, [REGISTER] da esa aksincha — bo'lmasligi kerak.
 */
enum class AuthPurpose(val apiValue: String) {
    LOGIN("login"),
    REGISTER("register"),
}

/** Server qaytaradigan, ilova alohida ishlov beradigan xato kodlari. */
object AuthErrorCode {
    /** Kirishda: bunday email bilan akkaunt yo'q. */
    const val NOT_REGISTERED = "not_registered"

    /** Ro'yxatdan o'tishda: bu email allaqachon band. */
    const val ALREADY_REGISTERED = "already_registered"
}

data class CodeRequest(
    val email: String,
    val expiresInSeconds: Int,
    val resendInSeconds: Int,
    val devCode: String?,
) {
    companion object {
        /** The address the server mails the code from; shown in the spam notice. */
        const val CODE_SENDER = "noreply@asos.api.berdiklichev.uz"
    }
}

data class AuthSession(
    val userName: String,
    val isNewUser: Boolean,
)
