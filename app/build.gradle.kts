// :app  ·  WP-00
// The ONLY module that knows all features: it wires them together.
plugins {
    alias(libs.plugins.asos.android.application)
    alias(libs.plugins.asos.android.compose)
    alias(libs.plugins.asos.hilt)
}

android {
    namespace = "uz.asos.app"

    defaultConfig {
        applicationId = "uz.asos.app"
        versionCode = 1
        versionName = "0.1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation"))
    implementation(project(":core:ui"))
    implementation(project(":core:data"))

    implementation(project(":feature:constitution"))
    implementation(project(":feature:legalhelp"))
    // Add the other features here as they land: :feature:home, :feature:auth, ...

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
}
