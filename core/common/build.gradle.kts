plugins {
    alias(libs.plugins.asos.jvm.library)
    alias(libs.plugins.asos.hilt)
}

dependencies {
    api(libs.kotlinx.coroutines.core)
}
