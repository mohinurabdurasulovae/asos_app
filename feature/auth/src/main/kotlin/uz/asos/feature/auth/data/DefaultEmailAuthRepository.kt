package uz.asos.feature.auth.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import uz.asos.core.common.dispatcher.AsosDispatchers
import uz.asos.core.common.dispatcher.Dispatcher
import uz.asos.core.common.result.AsosResult
import uz.asos.core.common.result.mapSuccess
import uz.asos.core.datastore.TokenStore
import uz.asos.core.network.ApiCaller
import uz.asos.feature.auth.data.model.AuthProvider
import uz.asos.feature.auth.data.model.AuthPurpose
import uz.asos.feature.auth.data.model.AuthSession
import uz.asos.feature.auth.data.model.CodeRequest
import uz.asos.feature.auth.data.network.AuthResponseDto
import uz.asos.feature.auth.data.network.OAuthBodyDto
import uz.asos.feature.auth.data.network.RequestCodeBodyDto
import uz.asos.feature.auth.data.network.RequestCodeResponseDto
import uz.asos.feature.auth.data.network.SignInApi
import uz.asos.feature.auth.data.network.UpdateMeBodyDto
import uz.asos.feature.auth.data.network.VerifyCodeBodyDto
import javax.inject.Inject

internal class DefaultEmailAuthRepository @Inject constructor(
    private val api: SignInApi,
    private val apiCaller: ApiCaller,
    private val tokenStore: TokenStore,
    @Dispatcher(AsosDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : EmailAuthRepository {

    override suspend fun requestCode(email: String, purpose: AuthPurpose): AsosResult<CodeRequest> =
        withContext(ioDispatcher) {
            val address = email.normalise()
            apiCaller.call { api.requestCode(RequestCodeBodyDto(email = address, purpose = purpose.apiValue)) }
                .mapSuccess { it.asModel(fallbackEmail = address) }
        }

    override suspend fun verifyCode(email: String, code: String): AsosResult<AuthSession> = withContext(ioDispatcher) {
        saveSession(verifyCall(email, code, AuthPurpose.LOGIN))
    }

    override suspend fun register(email: String, code: String, name: String): AsosResult<AuthSession> =
        withContext(ioDispatcher) {
            val result = saveSession(verifyCall(email, code, AuthPurpose.REGISTER))
            if (result !is AsosResult.Success || name.isBlank()) return@withContext result

            /* Ism alohida so'rov bilan saqlanadi. U ketmasa ham akkaunt yaratilgan,
             * shuning uchun kirishni to'xtatmaymiz — ismni keyin profilda tahrirlash mumkin. */
            apiCaller.call { api.updateMe(UpdateMeBodyDto(name = name)) }
            AsosResult.Success(result.data.copy(userName = name))
        }

    private suspend fun verifyCall(email: String, code: String, purpose: AuthPurpose) = apiCaller.call {
        api.verify(VerifyCodeBodyDto(email = email.normalise(), code = code, purpose = purpose.apiValue))
    }

    override suspend fun signInWithProvider(
        provider: AuthProvider,
        subject: String,
        email: String?,
        name: String?,
    ): AsosResult<AuthSession> = withContext(ioDispatcher) {
        val body = OAuthBodyDto(provider = provider.apiValue, subject = subject, email = email, name = name)
        saveSession(apiCaller.call { api.oauth(body) })
    }

    private suspend fun saveSession(result: AsosResult<AuthResponseDto>): AsosResult<AuthSession> {
        if (result is AsosResult.Success) {
            tokenStore.save(accessToken = result.data.token.accessToken, expiresAt = result.data.token.expiresAt)
        }
        return result.mapSuccess { it.asModel() }
    }
}

private fun RequestCodeResponseDto.asModel(fallbackEmail: String) = CodeRequest(
    email = email.ifBlank { fallbackEmail },
    expiresInSeconds = expiresIn,
    resendInSeconds = resendIn,
    devCode = devCode,
)

private fun AuthResponseDto.asModel() = AuthSession(
    userName = user?.name.orEmpty(),
    isNewUser = isNew,
)

private fun String.normalise(): String = trim().lowercase()
