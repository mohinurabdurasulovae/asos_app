// :feature:legalhelp  ·  WP-10
// Huquqiy yordam (legal help) + Advokat bilan chat.
// NEVER depend on another :feature:* module. Navigate through core:navigation routes.
plugins {
    alias(libs.plugins.asos.android.feature)
}

android {
    namespace = "uz.asos.feature.legalhelp"
}

dependencies {
    // TODO(WP-02): uncomment when core:data has LegalHelpRepository, then delete data/temp/.
    // implementation(project(":core:data"))
}
