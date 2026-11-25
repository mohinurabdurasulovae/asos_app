// :feature:constitution  ·  WP-05
// Konstitutsiya (browse) + Modda (article reader).
// NEVER depend on another :feature:* module. Navigate through core:navigation routes.
plugins {
    alias(libs.plugins.asos.android.feature)
}

android {
    namespace = "uz.asos.feature.constitution"
}

dependencies {
    implementation(project(":core:data"))
}
