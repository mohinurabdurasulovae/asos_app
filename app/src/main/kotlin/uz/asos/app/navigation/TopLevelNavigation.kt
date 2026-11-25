/*
 * TopLevelNavigation.kt
 * Module: :app   ·   Work package: WP-00
 *
 * Tab switching with a saved back stack per tab: leaving a tab keeps its screens, and
 * coming back restores them. Tapping the current tab again does nothing extra.
 */
package uz.asos.app.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.navOptions
import uz.asos.core.navigation.TopLevelDestination

internal fun navigateToTab(navController: NavController, destination: TopLevelDestination) {
    val options = navOptions {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
    navController.navigate(destination.route, options)
}
