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

include(":feature:constitution")

// Still placeholders (their build.gradle.kts files are empty). Add each line when the
// work package lands, together with the module's dependency in app/build.gradle.kts:
//   :core:database   :core:domain
//   :feature:auth    :feature:home       :feature:dictionary   :feature:learn
//   :feature:cases   :feature:community  :feature:legalhelp    :feature:profile
