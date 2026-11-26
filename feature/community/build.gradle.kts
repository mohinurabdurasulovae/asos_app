// :feature:community  ·  WP-09
// PLACEHOLDER: build configuration goes here.
//
// Apply the convention plugin: asos.android.feature (brings Compose, Hilt, core:designsystem, core:ui, core:navigation).
// Extra dependencies: project(":core:domain"), project(":core:data").
// NEVER depend on another :feature:* module. Navigate through core:navigation routes.

plugins {
    id("asos.android.feature")
}

android {
    namespace = "uz.asos.feature.community"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:model"))

    testImplementation(kotlin("test"))
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
}
