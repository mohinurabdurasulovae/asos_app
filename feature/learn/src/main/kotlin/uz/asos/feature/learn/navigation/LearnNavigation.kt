/*
 * LearnNavigation.kt
 * Module: :feature:learn   ·   Work package: WP-07
 *
 * Public entry point of the feature: the only file the app module uses.
 *  - LearnRoute (tab)        → O‘rganish
 *  - QuizRoute(quizId)       → Test savoli (full screen, no bottom bar)
 */
package uz.asos.feature.learn.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import uz.asos.core.navigation.ArticleRoute
import uz.asos.core.navigation.AsosRoute
import uz.asos.core.navigation.CasesRoute
import uz.asos.core.navigation.LoginRoute
import uz.asos.core.navigation.ProfileRoute
import uz.asos.feature.learn.hub.LearnRoute
import uz.asos.feature.learn.quiz.QuizRoute
import uz.asos.core.navigation.LearnRoute as LearnDestination
import uz.asos.core.navigation.QuizRoute as QuizDestination

fun NavController.navigateToLearn(navOptions: NavOptions? = null) {
    navigate(LearnDestination, navOptions)
}

fun NavController.navigateToQuiz(quizId: String, navOptions: NavOptions? = null) {
    navigate(QuizDestination(quizId), navOptions)
}

/**
 * @param navController used to close the quiz.
 * @param onNavigate opens screens of other features through AsosRoutes.
 */
fun NavGraphBuilder.learnScreens(
    navController: NavController,
    onNavigate: (AsosRoute) -> Unit,
) {
    composable<LearnDestination> {
        LearnRoute(
            onOpenQuiz = { key -> onNavigate(QuizDestination(key)) },
            onOpenCases = { onNavigate(CasesRoute) },
            onOpenAchievements = { onNavigate(ProfileRoute) },
            onSignIn = { onNavigate(LoginRoute) },
        )
    }

    composable<QuizDestination> {
        QuizRoute(
            onClose = { navController.navigateUp() },
            onOpenArticle = { number -> onNavigate(ArticleRoute(number)) },
            onSignIn = { onNavigate(LoginRoute) },
        )
    }
}
