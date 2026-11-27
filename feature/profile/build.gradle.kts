// :feature:profile  ·  WP-11
// Profil.
// NEVER depend on another :feature:* module. Navigate through core:navigation routes.
plugins {
    alias(libs.plugins.asos.android.feature)
}

android {
    namespace = "uz.asos.feature.profile"
}

dependencies {
    implementation(project(":core:data"))
}
