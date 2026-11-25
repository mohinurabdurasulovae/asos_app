// :core:common  ·  WP-00
// Pure Kotlin/JVM library: result type, dispatchers, Uzbek formatters.
plugins {
    alias(libs.plugins.asos.jvm.library)
    alias(libs.plugins.asos.hilt)
}

dependencies {
    api(libs.kotlinx.coroutines.core)
}
