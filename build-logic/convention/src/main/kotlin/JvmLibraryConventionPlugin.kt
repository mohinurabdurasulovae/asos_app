/*
 * JvmLibraryConventionPlugin.kt
 * Module: :build-logic   ·   Work package: WP-00
 *
 * Pure Kotlin/JVM modules (:core:model, :core:common). No Android types allowed there.
 */
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import uz.asos.buildlogic.configureKotlinJvmTarget
import uz.asos.buildlogic.lib
import uz.asos.buildlogic.libs

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.jvm")

            extensions.configure<JavaPluginExtension> {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
            configureKotlinJvmTarget()

            dependencies {
                add("testImplementation", libs.lib("junit"))
            }
        }
    }
}
