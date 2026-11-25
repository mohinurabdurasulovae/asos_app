/*
 * ConstitutionNavigation.kt
 * Module: :feature:constitution   ·   Work package: WP-05
 *
 * Public entry point of this feature. Registers its screens in the app NavHost and exposes
 * navigateTo… helpers. The app module calls these and nothing else.
 *
 *  - ConstitutionScreen → ConstitutionRoute (tab "Konstitutsiya")
 *  - ArticleScreen      → ArticleRoute(key), deep link asos://article/{key}
 *
 * The screen composables are also called ConstitutionRoute / ArticleRoute (docs/ARCHITECTURE.md
 * "XRoute" pattern), so the route classes are imported here as …Destination.
 */
package uz.asos.feature.constitution.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import uz.asos.core.navigation.AsosRoute
import uz.asos.core.navigation.CasesRoute
import uz.asos.core.navigation.CommunityRoute
import uz.asos.core.navigation.DeepLinks
import uz.asos.core.navigation.DictionaryRoute
import uz.asos.core.navigation.LoginRoute
import uz.asos.core.navigation.QuizRoute
import uz.asos.feature.constitution.article.ArticleRoute
import uz.asos.feature.constitution.browse.ConstitutionRoute
import uz.asos.core.navigation.ArticleRoute as ArticleDestination
import uz.asos.core.navigation.ConstitutionRoute as ConstitutionDestination

fun NavController.navigateToConstitution(navOptions: NavOptions? = null) {
    navigate(ConstitutionDestination, navOptions)
}

fun NavController.navigateToArticle(key: String, navOptions: NavOptions? = null) {
    navigate(ArticleDestination(key), navOptions)
}

/**
 * @param navController used for back and for replacing the reader (previous / next article).
 * @param onNavigate opens screens of other features through AsosRoutes.
 */
fun NavGraphBuilder.constitutionScreens(
    navController: NavController,
    onNavigate: (AsosRoute) -> Unit,
) {
    composable<ConstitutionDestination> {
        ConstitutionRoute(
            onOpenArticle = { key -> onNavigate(ArticleDestination(key)) },
            onSignIn = { onNavigate(LoginRoute) },
        )
    }

    composable<ArticleDestination>(
        deepLinks = listOf(navDeepLink<ArticleDestination>(basePath = DeepLinks.ARTICLE)),
    ) {
        ArticleRoute(
            onBack = { navController.navigateUp() },
            onOpenArticle = { key -> onNavigate(ArticleDestination(key)) },
            onReplaceArticle = { key ->
                navController.navigate(ArticleDestination(key)) {
                    popUpTo<ArticleDestination> { inclusive = true }
                }
            },
            onOpenTerm = { slug -> onNavigate(DictionaryRoute(termSlug = slug)) },
            onOpenCommunity = { number -> onNavigate(CommunityRoute(articleNumber = number)) },
            onOpenCases = { onNavigate(CasesRoute) },
            onOpenQuiz = { quizId -> onNavigate(QuizRoute(quizId)) },
            onSignIn = { onNavigate(LoginRoute) },
        )
    }
}
