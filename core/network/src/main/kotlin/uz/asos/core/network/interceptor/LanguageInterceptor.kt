package uz.asos.core.network.interceptor

import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response
import uz.asos.core.network.AsosApiConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageInterceptor @Inject constructor() : Interceptor {

    private val deviceName: String =
        listOf(Build.MANUFACTURER, Build.MODEL)
            .filter { !it.isNullOrBlank() }
            .joinToString(" ")
            .ifBlank { "Android" }
            .let { "$it (Android ${Build.VERSION.RELEASE})" }
            .filter { it.code in 0x20..0x7E }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Accept", "application/json")
            .header("Accept-Language", AsosApiConfig.LANGUAGE)
            .header("X-Asos-Device", deviceName)
            .build()
        return chain.proceed(request)
    }
}
