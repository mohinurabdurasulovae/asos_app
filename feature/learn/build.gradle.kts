plugins {
    alias(libs.plugins.asos.android.feature)
}

android {
    namespace = "uz.asos.feature.learn"
}

dependencies {
    implementation(project(":core:data"))
}
