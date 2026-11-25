// :core:ui  ·  WP-01
// Compose. Loading / error / empty states, preview helpers.
plugins {
    alias(libs.plugins.asos.android.library)
    alias(libs.plugins.asos.android.compose)
}

android {
    namespace = "uz.asos.core.ui"
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:model"))
}
