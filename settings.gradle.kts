// Root settings  ·  WP-00
pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Asos"

include(":app")

include(":core:common")
include(":core:model")
include(":core:network")
include(":core:datastore")
include(":core:data")
include(":core:navigation")
include(":core:designsystem")
include(":core:ui")
include(":core:testing")

include(":feature:auth")
include(":feature:cases")
include(":feature:home")
include(":feature:community")
include(":feature:constitution")
include(":feature:dictionary")
include(":feature:learn")
include(":feature:legalhelp")
include(":feature:profile")

// Every feature module is in. Still placeholders (their build.gradle.kts files are empty),
// so they are not included yet:
//   :core:database   :core:domain
