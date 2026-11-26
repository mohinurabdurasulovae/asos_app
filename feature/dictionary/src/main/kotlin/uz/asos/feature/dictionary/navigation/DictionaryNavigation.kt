/*
 * DictionaryNavigation.kt
 * Module: :feature:dictionary   ·   Work package: WP-06
 *
 * Public entry point of this feature. Registers its screen in the app NavHost and exposes
 * navigateTo… helpers. The app module calls these and nothing else.
 *
 *  - DictionaryScreen → DictionaryRoute(termSlug) ("" = the list, a slug = open that term)
 *
 * The screen composable is also called DictionaryRoute (docs/ARCHITECTURE.md "XRoute"
 * pattern), so the route class is imported here as DictionaryDestination.
 */
package uz.asos.feature.dictionary.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import uz.asos.core.navigation.ArticleRoute
import uz.asos.core.navigation.AsosRoute
import uz.asos.core.navigation.LoginRoute
import uz.asos.feature.dictionary.DictionaryRoute
import uz.asos.core.navigation.DictionaryRoute as DictionaryDestination

fun NavController.navigateToDictionary(termSlug: String = "", navOptions: NavOptions? = null) {
    navigate(DictionaryDestination(termSlug), navOptions)
}

/**
 * @param navController used for back.
 * @param onNavigate opens screens of other features through AsosRoutes.
 */
fun NavGraphBuilder.dictionaryScreens(
    navController: NavController,
    onNavigate: (AsosRoute) -> Unit,
) {
    composable<DictionaryDestination> {
        DictionaryRoute(
            onBack = { navController.navigateUp() },
            onOpenArticle = { number -> onNavigate(ArticleRoute(number)) },
            onSignIn = { onNavigate(LoginRoute) },
        )
    }
}
