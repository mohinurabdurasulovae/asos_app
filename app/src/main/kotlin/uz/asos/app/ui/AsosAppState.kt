package uz.asos.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import uz.asos.app.navigation.navigateToTab
import uz.asos.core.navigation.TopLevelDestination

@Composable
fun rememberAsosAppState(
    navController: NavHostController = rememberNavController(),
): AsosAppState = remember(navController) { AsosAppState(navController) }

@Stable
class AsosAppState(
    val navController: NavHostController,
) {
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    val currentDestination: NavDestination?
        @Composable get() {
            val entry by navController.currentBackStackEntryAsState()
            return entry?.destination
        }

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() {
            val destination = currentDestination ?: return null
            return topLevelDestinations.firstOrNull { tab ->
                destination.hierarchy.any { it.hasRoute(tab.routeClass) }
            }
        }

    val shouldShowBottomBar: Boolean
        @Composable get() = currentTopLevelDestination != null

    fun navigateToTopLevelDestination(destination: TopLevelDestination) {
        navigateToTab(navController, destination)
    }
}
