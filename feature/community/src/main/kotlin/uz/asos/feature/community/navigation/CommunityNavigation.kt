/*
 * CommunityNavigation.kt
 * Module: :feature:community   ·   Work package: WP-09
 *
 * Public entry point of this feature: the only file the app module uses.
 *  - CommunityRoute(articleNumber) → Hamjamiyat (tab); a number narrows the feed to one article
 *  - PostRoute(postId)             → Muhokama
 */
package uz.asos.feature.community.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import uz.asos.core.navigation.ArticleRoute
import uz.asos.core.navigation.AsosRoute
import uz.asos.core.navigation.BookmarksRoute
import uz.asos.core.navigation.ChatRoute
import uz.asos.core.navigation.LoginRoute
import uz.asos.core.navigation.SearchRoute
import uz.asos.feature.community.feed.CommunityFeedRoute
import uz.asos.feature.community.post.PostDetailRoute
import uz.asos.core.navigation.CommunityRoute as CommunityDestination
import uz.asos.core.navigation.PostRoute as PostDestination

fun NavController.navigateToCommunity(
    articleNumber: Int = 0,
    navOptions: NavOptions? = null,
) {
    navigate(CommunityDestination(articleNumber), navOptions)
}

fun NavController.navigateToPost(postId: Int, navOptions: NavOptions? = null) {
    navigate(PostDestination(postId), navOptions)
}

/**
 * @param navController used for back.
 * @param onNavigate opens screens of other features through AsosRoutes.
 */
fun NavGraphBuilder.communityScreens(
    navController: NavController,
    onNavigate: (AsosRoute) -> Unit,
) {
    composable<CommunityDestination> {
        CommunityFeedRoute(
            onOpenPost = { postId -> onNavigate(PostDestination(postId)) },
            onOpenSearch = { onNavigate(SearchRoute) },
            onOpenSaved = { onNavigate(BookmarksRoute) },
            onSignIn = { onNavigate(LoginRoute) },
        )
    }

    composable<PostDestination> {
        PostDetailRoute(
            onBack = { navController.navigateUp() },
            onOpenArticle = { number -> onNavigate(ArticleRoute(number)) },
            onOpenChat = { lawyerId -> onNavigate(ChatRoute(lawyerId)) },
            onSignIn = { onNavigate(LoginRoute) },
        )
    }
}
