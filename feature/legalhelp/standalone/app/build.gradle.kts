import java.io.StringReader
import java.net.URI
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}

val asosApiUrl = providers.gradleProperty("ASOS_API_BASE_URL")
    .getOrElse("https://asos.api.berdiklichev.uz").trim().trimEnd('/')
val asosApiUri = URI(asosApiUrl)
require(
    asosApiUri.scheme == "https" && asosApiUri.host != null &&
        asosApiUri.userInfo == null && asosApiUri.query == null &&
        asosApiUri.fragment == null && asosApiUri.path.isEmpty()
) { "ASOS_API_BASE_URL must be an HTTPS origin." }

val geminiConfig = rootProject.layout.projectDirectory.file("gemini.local.properties")
val geminiApiKey = providers.fileContents(geminiConfig).asText.map { contents ->
    val properties = Properties().apply { load(StringReader(contents)) }
    properties.getProperty("GEMINI_API_KEY", "").trim()
}.getOrElse("")
require(geminiApiKey.isEmpty() || Regex("[A-Za-z0-9_.-]{20,300}").matches(geminiApiKey)) {
    "Check the GEMINI_API_KEY value in gemini.local.properties."
}

android {
    namespace = "uz.asos.huquqiyyordam"
    compileSdk = 37

    defaultConfig {
        applicationId = "uz.asos.huquqiyyordam"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "ASOS_API_BASE_URL", "\"$asosApiUrl\"")
        buildConfigField("String", "GEMINI_API_KEY", "\"$geminiApiKey\"")
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
        }
    }

    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2026.08.00"))
    implementation("androidx.activity:activity-compose:1.13.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.9.4")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
}
