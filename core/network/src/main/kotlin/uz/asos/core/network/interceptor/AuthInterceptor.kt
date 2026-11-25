/*
 * AuthInterceptor.kt
 * Module: :core:network   ·   Work package: WP-02
 *
 * Adds "Authorization: Bearer <accessToken>" when the user is signed in. The API has no
 * refresh endpoint, so a 401 on a request that carried a token means the token expired:
 * it is cleared and the app carries on as a guest until the user signs in again.
 */
package uz.asos.core.network.interceptor

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import uz.asos.core.datastore.TokenStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenStore: TokenStore,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { tokenStore.current() }
        val request = if (token.isNullOrBlank()) {
            chain.request()
        } else {
            chain.request().newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        }

        val response = chain.proceed(request)
        if (response.code == 401 && !token.isNullOrBlank()) {
            runBlocking { tokenStore.clear() }
        }
        return response
    }
}
