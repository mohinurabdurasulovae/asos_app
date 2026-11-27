package uz.asos.feature.auth.data

import uz.asos.core.common.result.AsosResult
import uz.asos.feature.auth.data.model.AuthProvider
import uz.asos.feature.auth.data.model.AuthPurpose
import uz.asos.feature.auth.data.model.AuthSession
import uz.asos.feature.auth.data.model.CodeRequest

interface EmailAuthRepository {

    /**
     * Tasdiqlash kodini yuboradi.
     *
     * [purpose] kirish va ro'yxatdan o'tishni ajratadi: server ro'yxatdan
     * o'tmagan manzilga kirish kodini yubormaydi.
     */
    suspend fun requestCode(email: String, purpose: AuthPurpose): AsosResult<CodeRequest>

    suspend fun verifyCode(email: String, code: String): AsosResult<AuthSession>

    suspend fun register(email: String, code: String, name: String): AsosResult<AuthSession>

    suspend fun signInWithProvider(
        provider: AuthProvider,
        subject: String,
        email: String? = null,
        name: String? = null,
    ): AsosResult<AuthSession>
}
