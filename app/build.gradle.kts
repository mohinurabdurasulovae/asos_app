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

    implementation(project(":feature:auth"))
    implementation(project(":feature:cases"))
    implementation(project(":feature:community"))
    implementation(project(":feature:home"))
    implementation(project(":feature:constitution"))
    implementation(project(":feature:dictionary"))
    implementation(project(":feature:learn"))
    implementation(project(":feature:legalhelp"))
    implementation(project(":feature:profile"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
}
