plugins {
    alias(libs.plugins.asos.android.library)
    alias(libs.plugins.asos.android.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "uz.asos.core.navigation"
}

dependencies {
    implementation(project(":core:designsystem"))
    api(libs.androidx.navigation.compose)
    api(libs.kotlinx.serialization.json)
}
