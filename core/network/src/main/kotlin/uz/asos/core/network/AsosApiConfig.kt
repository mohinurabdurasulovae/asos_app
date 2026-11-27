package uz.asos.core.network

object AsosApiConfig {
    val BASE_URL: String = BuildConfig.ASOS_BASE_URL.let { if (it.endsWith("/")) it else "$it/" }

    const val CONNECT_TIMEOUT_SECONDS = 15L
    const val READ_TIMEOUT_SECONDS = 30L

    const val LANGUAGE = "uz-Latn"
}
