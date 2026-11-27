plugins {
    alias(libs.plugins.asos.android.library)
    alias(libs.plugins.asos.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "uz.asos.core.network"
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        val baseUrl = providers.gradleProperty("asos.baseUrl")
            .getOrElse("https://asos.api.berdiklichev.uz/")
        buildConfigField("String", "ASOS_BASE_URL", "\"$baseUrl\"")
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:datastore"))

    api(libs.kotlinx.serialization.json)
    api(libs.retrofit.core)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)
}
