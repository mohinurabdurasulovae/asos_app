// :feature:cases  ·  WP-08
// Kazuslar (list) + Kazus — yechim (solve).
// NEVER depend on another :feature:* module. Navigate through core:navigation routes.
//
// The cases themselves live in this module for now (data/, in-memory), so the feature needs
// no core:* changes. They move to core:data + the /v1/cases endpoints without touching the
// screens: only data/CasesRepository's implementation changes.
plugins {
    alias(libs.plugins.asos.android.feature)
}

android {
    namespace = "uz.asos.feature.cases"
}

dependencies {
    implementation(project(":core:data"))
}
