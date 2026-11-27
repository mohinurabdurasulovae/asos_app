/*
 * CasesNavigation.kt
 * Module: :feature:cases   ·   Work package: WP-08
 *
 * Public entry point of this feature: the only file the app module uses.
 *  - CasesRoute          → Kazuslar (reached from O‘rganish, so it has a back button)
 *  - CaseRoute(caseId)   → Kazus — yechim
 *
 * The screen composables are also called CasesRoute / CaseDetailRoute (docs/ARCHITECTURE.md
 * "XRoute" pattern), so the route classes are imported here as …Destination.
 */
package uz.asos.feature.cases.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import uz.asos.core.navigation.ArticleRoute
import uz.asos.core.navigation.AsosRoute
import uz.asos.feature.cases.detail.CaseDetailRoute
import uz.asos.feature.cases.list.CasesRoute
import uz.asos.core.navigation.CaseRoute as CaseDestination
import uz.asos.core.navigation.CasesRoute as CasesDestination

fun NavController.navigateToCases(navOptions: NavOptions? = null) {
    navigate(CasesDestination, navOptions)
}

fun NavController.navigateToCase(caseId: Int, navOptions: NavOptions? = null) {
    navigate(CaseDestination(caseId.toString()), navOptions)
}

/**
 * @param navController used for back.
 * @param onNavigate opens screens of other features through AsosRoutes (the article reader).
 */
fun NavGraphBuilder.casesScreens(
    navController: NavController,
    onNavigate: (AsosRoute) -> Unit,
) {
    composable<CasesDestination> {
        CasesRoute(
            onOpenCase = { id -> onNavigate(CaseDestination(id.toString())) },
            onBack = { navController.navigateUp() },
        )
    }

    composable<CaseDestination> {
        CaseDetailRoute(
            onBack = { navController.navigateUp() },
            onOpenCase = { id -> onNavigate(CaseDestination(id.toString())) },
            onOpenArticle = { number -> onNavigate(ArticleRoute(number)) },
        )
    }
}
