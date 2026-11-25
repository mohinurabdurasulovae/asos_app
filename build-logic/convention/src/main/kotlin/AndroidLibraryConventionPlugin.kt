/*
 * AndroidLibraryConventionPlugin.kt
 * Module: :build-logic   ·   Work package: WP-00
 *
 * Same SDK settings for every library module.
 */
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import uz.asos.buildlogic.configureKotlinAndroid
import uz.asos.buildlogic.lib
import uz.asos.buildlogic.libs

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")
            pluginManager.apply("org.jetbrains.kotlin.android")

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
            }

            dependencies {
                add("testImplementation", libs.lib("junit"))
            }
        }
    }
}
