plugins {
    alias(libs.plugins.asos.android.feature)
}

android {
    namespace = "uz.asos.feature.legalhelp"
}

dependencies {
    implementation(project(":core:data"))
}
