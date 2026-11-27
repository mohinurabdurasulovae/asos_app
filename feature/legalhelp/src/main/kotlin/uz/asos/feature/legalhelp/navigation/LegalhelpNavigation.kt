/*
 * LegalhelpNavigation.kt
 * Module: :feature:legalhelp   ·   Work package: WP-10
 *
 * Public entry point of this feature. Registers its screens in the app NavHost and exposes
 * navigateTo… helpers. The app module calls these and nothing else.
 *
 *  - LegalHelpScreen → LegalHelpRoute
 *  - ChatScreen      → ChatRoute(chatId), chatId = conversation id from the API
 *
 * The screen composables are also called LegalHelpRoute / ChatRoute (docs/ARCHITECTURE.md
 * "XRoute" pattern), so the route classes are imported here as …Destination.
 */
package uz.asos.feature.legalhelp.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import uz.asos.core.navigation.ArticleRoute
import uz.asos.core.navigation.AsosRoute
import uz.asos.core.navigation.LoginRoute
import uz.asos.feature.legalhelp.chat.ChatRoute
import uz.asos.feature.legalhelp.help.LegalHelpRoute
import uz.asos.core.navigation.ChatRoute as ChatDestination
import uz.asos.core.navigation.LegalHelpRoute as LegalHelpDestination

fun NavController.navigateToLegalHelp(navOptions: NavOptions? = null) {
    navigate(LegalHelpDestination, navOptions)
}

fun NavController.navigateToChat(conversationId: Int, navOptions: NavOptions? = null) {
    navigate(ChatDestination(conversationId), navOptions)
}

/**
 * @param navController used for back.
 * @param onNavigate opens screens of other features through AsosRoutes (the article reader).
 */
fun NavGraphBuilder.legalHelpScreens(
    navController: NavController,
    onNavigate: (AsosRoute) -> Unit,
) {
    composable<LegalHelpDestination> {
        LegalHelpRoute(
            onBack = { navController.navigateUp() },
            onOpenChat = { conversationId -> onNavigate(ChatDestination(conversationId)) },
            onOpenArticle = { number -> onNavigate(ArticleRoute(number)) },
            onSignIn = { onNavigate(LoginRoute) },
        )
    }

    composable<ChatDestination> {
        ChatRoute(
            onBack = { navController.navigateUp() },
            onOpenArticle = { number -> onNavigate(ArticleRoute(number)) },
            onSignIn = { onNavigate(LoginRoute) },
        )
    }
}
