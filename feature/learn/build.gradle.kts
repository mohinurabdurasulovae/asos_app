// :feature:learn  ·  WP-07
// O‘rganish (learning hub) + Test savoli (quiz).
// NEVER depend on another :feature:* module. Navigate through core:navigation routes.
plugins {
    alias(libs.plugins.asos.android.feature)
}

android {
    namespace = "uz.asos.feature.learn"
}

dependencies {
    implementation(project(":core:data"))
}
