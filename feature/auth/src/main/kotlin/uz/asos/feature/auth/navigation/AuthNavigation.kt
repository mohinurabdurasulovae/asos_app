package uz.asos.feature.auth.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import uz.asos.core.navigation.AsosRoute
import uz.asos.core.navigation.HomeRoute
import uz.asos.feature.auth.login.LoginRoute
import uz.asos.feature.auth.register.RegisterRoute
import uz.asos.core.navigation.LoginRoute as LoginDestination

/**
 * Ro‘yxatdan o‘tish. Only reached from Kirish, so the route lives in this module instead of
 * core:navigation/AsosRoutes.kt.
 */
@Serializable
internal data object RegisterDestination : AsosRoute

private const val RETURN_AFTER_SIGN_IN = "auth_return_after_sign_in"

fun NavController.navigateToLogin(navOptions: NavOptions? = null) {
    navigate(LoginDestination, navOptions)
}

fun NavController.navigateToRegister(navOptions: NavOptions? = null) {
    navigate(RegisterDestination, navOptions)
}

fun NavGraphBuilder.authScreens(navController: NavController) {
    composable<LoginDestination> { entry ->
        remember(entry) {
            if (!entry.savedStateHandle.contains(RETURN_AFTER_SIGN_IN)) {
                entry.savedStateHandle[RETURN_AFTER_SIGN_IN] = navController.previousBackStackEntry != null
            }
        }
        LoginRoute(
            onSignedIn = { navController.finishAuth() },
            onOpenRegister = { navController.navigateToRegister() },
        )
    }

    composable<RegisterDestination> {
        RegisterRoute(
            onRegistered = { navController.finishAuth() },
            onBack = { navController.navigateUp() },
        )
    }
}

private fun NavController.finishAuth() {
    val loginEntry = runCatching { getBackStackEntry<LoginDestination>() }.getOrNull()
    val returnBack = loginEntry?.savedStateHandle?.get<Boolean>(RETURN_AFTER_SIGN_IN) == true
    if (returnBack) {
        popBackStack<LoginDestination>(inclusive = true)
    } else {
        navigate(HomeRoute) {
            popUpTo<LoginDestination> { inclusive = true }
        }
    }
}
