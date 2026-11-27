package uz.asos.feature.dictionary.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import uz.asos.core.navigation.ArticleRoute
import uz.asos.core.navigation.AsosRoute
import uz.asos.feature.dictionary.DictionaryRoute
import uz.asos.core.navigation.DictionaryRoute as DictionaryDestination

fun NavController.navigateToDictionary(
    termSlug: String = "",
    navOptions: NavOptions? = null,
) {
    navigate(DictionaryDestination(termSlug), navOptions)
}

fun NavGraphBuilder.dictionaryScreens(
    navController: NavController,
    onNavigate: (AsosRoute) -> Unit,
) {
    composable<DictionaryDestination> {
        DictionaryRoute(
            onBack = { navController.navigateUp() },
            onOpenArticle = { number -> onNavigate(ArticleRoute(number)) },
        )
    }
}
