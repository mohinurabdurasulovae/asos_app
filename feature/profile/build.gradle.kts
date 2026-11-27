plugins {
    alias(libs.plugins.asos.android.feature)
}

android {
    namespace = "uz.asos.feature.profile"
}

dependencies {
    implementation(project(":core:data"))
}
