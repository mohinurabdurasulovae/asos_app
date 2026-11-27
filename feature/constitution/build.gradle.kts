plugins {
    alias(libs.plugins.asos.android.feature)
}

android {
    namespace = "uz.asos.feature.constitution"
}

dependencies {
    implementation(project(":core:data"))
}
