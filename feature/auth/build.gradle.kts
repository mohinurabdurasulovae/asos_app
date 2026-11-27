plugins {
    alias(libs.plugins.asos.android.feature)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "uz.asos.feature.auth"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))
    implementation(libs.androidx.activity.compose)
}
