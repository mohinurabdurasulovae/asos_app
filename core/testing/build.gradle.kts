// :core:testing  ·  WP-02
// Fakes, sample data and test rules. Used as testImplementation by every module.
plugins {
    alias(libs.plugins.asos.android.library)
}

android {
    namespace = "uz.asos.core.testing"
}

dependencies {
    api(project(":core:data"))
    api(project(":core:model"))
    api(project(":core:common"))
    api(libs.junit)
    api(libs.kotlinx.coroutines.test)
    api(libs.turbine)
}
