// :feature:auth  ·  WP-03
// Kirish + Ro‘yxatdan o‘tish.
// NEVER depend on another :feature:* module. Navigate through core:navigation routes.
//
// The phone sign-in calls live in this module for now (data/), so the feature needs no
// core:* changes. They can move to core:data later without touching the screens.
plugins {
    alias(libs.plugins.asos.android.feature)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "uz.asos.feature.auth"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))
    implementation(libs.androidx.activity.compose)
}
