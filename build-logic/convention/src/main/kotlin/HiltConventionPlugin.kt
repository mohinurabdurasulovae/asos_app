/*
 * HiltConventionPlugin.kt
 * Module: :build-logic   ·   Work package: WP-00
 *
 * Hilt + KSP. Android modules get the Hilt Gradle plugin and hilt-android; JVM modules get
 * hilt-core so they can declare @Module classes.
 */
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import uz.asos.buildlogic.lib
import uz.asos.buildlogic.libs

class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")

            dependencies {
                add("ksp", libs.lib("hilt-compiler"))
            }

            pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
                dependencies {
                    add("implementation", libs.lib("hilt-core"))
                }
            }

            pluginManager.withPlugin("com.android.base") {
                pluginManager.apply("com.google.dagger.hilt.android")
                dependencies {
                    add("implementation", libs.lib("hilt-android"))
                }
            }
        }
    }
}
