// :feature:home  ·  WP-04
// Bosh sahifa.
// NEVER depend on another :feature:* module. Navigate through core:navigation routes.
//
// The home feed calls live in this module for now (data/), so the feature needs no core:*
// changes. They can move to core:data later without touching the screen.
plugins {
    alias(libs.plugins.asos.android.feature)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "uz.asos.feature.home"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:network"))
}
