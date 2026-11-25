/*
 * AsosApiConfig.kt
 * Module: :core:network   ·   Work package: WP-02
 *
 * Base URL and timeouts. The base URL comes from the Gradle property `asos.baseUrl`
 * (gradle.properties). Endpoint paths already start with "v1/", as in openapi.yaml.
 */
package uz.asos.core.network

object AsosApiConfig {
    /** e.g. "https://asos.api.berdiklichev.uz/" (always ends with "/"). */
    val BASE_URL: String = BuildConfig.ASOS_BASE_URL.let { if (it.endsWith("/")) it else "$it/" }

    const val CONNECT_TIMEOUT_SECONDS = 15L
    const val READ_TIMEOUT_SECONDS = 30L

    /** Interface language sent in Accept-Language. */
    const val LANGUAGE = "uz-Latn"
}
