/*
 * AndroidApplicationConventionPlugin.kt
 * Module: :build-logic   ·   Work package: WP-00
 *
 * compileSdk/minSdk/targetSdk, Kotlin JVM target for :app.
 */
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import uz.asos.buildlogic.AsosSdk
import uz.asos.buildlogic.configureKotlinAndroid

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")
            pluginManager.apply("org.jetbrains.kotlin.android")

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = AsosSdk.TARGET
            }
        }
    }
}
