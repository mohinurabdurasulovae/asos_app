package uz.asos.feature.auth.data

import uz.asos.core.common.result.AsosResult
import uz.asos.feature.auth.data.model.AuthProvider
import uz.asos.feature.auth.data.model.AuthPurpose
import uz.asos.feature.auth.data.model.AuthSession
import uz.asos.feature.auth.data.model.CodeRequest

class FakeEmailAuthRepository : EmailAuthRepository {

    var requestCodeResult: AsosResult<CodeRequest> = AsosResult.Success(
        CodeRequest(
            email = "aziza@example.uz",
            expiresInSeconds = 300,
            resendInSeconds = 60,
            devCode = null,
        ),
    )
    var verifyResult: AsosResult<AuthSession> =
        AsosResult.Success(AuthSession(userName = "Aziza Karimova", isNewUser = false))
    var providerResult: AsosResult<AuthSession> = verifyResult

    val requestedEmails = mutableListOf<String>()
    val requestedPurposes = mutableListOf<AuthPurpose>()
    val verifiedCodes = mutableListOf<Pair<String, String>>()
    val registeredNames = mutableListOf<String>()
    val usedProviders = mutableListOf<AuthProvider>()

    override suspend fun requestCode(email: String, purpose: AuthPurpose): AsosResult<CodeRequest> {
        requestedEmails += email
        requestedPurposes += purpose
        return requestCodeResult
    }

    override suspend fun verifyCode(email: String, code: String): AsosResult<AuthSession> {
        verifiedCodes += email to code
        return verifyResult
    }

    override suspend fun register(email: String, code: String, name: String): AsosResult<AuthSession> {
        verifiedCodes += email to code
        registeredNames += name
        return verifyResult
    }

    override suspend fun signInWithProvider(
        provider: AuthProvider,
        subject: String,
        email: String?,
        name: String?,
    ): AsosResult<AuthSession> {
        usedProviders += provider
        return providerResult
    }
}
