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
