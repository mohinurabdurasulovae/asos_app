package uz.asos.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import uz.asos.core.navigation.AsosRoute
import uz.asos.feature.home.HomeRoute
import uz.asos.core.navigation.HomeRoute as HomeDestination

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(HomeDestination, navOptions)
}

fun NavGraphBuilder.homeScreens(onNavigate: (AsosRoute) -> Unit) {
    composable<HomeDestination> {
        HomeRoute(onNavigate = onNavigate)
    }
}
