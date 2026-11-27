plugins {
    alias(libs.plugins.asos.android.feature)
}

android {
    namespace = "uz.asos.feature.dictionary"
}

dependencies {
    implementation(project(":core:data"))
}
