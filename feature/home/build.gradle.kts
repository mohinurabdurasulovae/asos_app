plugins {
    alias(libs.plugins.asos.android.feature)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "uz.asos.feature.home"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:network"))
}
