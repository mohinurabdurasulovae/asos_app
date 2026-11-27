plugins {
    alias(libs.plugins.asos.android.library)
    alias(libs.plugins.asos.hilt)
}

android {
    namespace = "uz.asos.core.datastore"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    api(libs.androidx.datastore.preferences)
}
