// :feature:community  ·  WP-09
// Hamjamiyat (feed) + Muhokama (one discussion).
// NEVER depend on another :feature:* module. Navigate through core:navigation routes.
plugins {
    alias(libs.plugins.asos.android.feature)
}

android {
    namespace = "uz.asos.feature.community"
}

dependencies {
    implementation(project(":core:data"))
}
