// :feature:dictionary  ·  WP-06
// Konstitutsiyaviy lug'at.
// NEVER depend on another :feature:* module. Navigate through core:navigation routes.
plugins {
    alias(libs.plugins.asos.android.feature)
}

android {
    namespace = "uz.asos.feature.dictionary"
}

dependencies {
    implementation(project(":core:data"))
}
