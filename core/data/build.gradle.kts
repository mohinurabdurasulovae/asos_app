// :core:data  ·  WP-02
// Repositories. Depends on :core:network, :core:datastore, :core:model.
// :core:database (Room, offline copy) will be added when WP-02 builds it.
plugins {
    alias(libs.plugins.asos.android.library)
    alias(libs.plugins.asos.hilt)
}

android {
    namespace = "uz.asos.core.data"
}

dependencies {
    api(project(":core:model"))
    api(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))
}
