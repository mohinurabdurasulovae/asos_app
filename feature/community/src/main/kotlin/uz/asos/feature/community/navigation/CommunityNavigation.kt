package uz.asos.feature.community.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import uz.asos.core.navigation.AsosRoute
import uz.asos.core.navigation.ArticleRoute
import uz.asos.core.navigation.BookmarksRoute
import uz.asos.core.navigation.ChatRoute
import uz.asos.core.navigation.CommunityRoute
import uz.asos.core.navigation.PostRoute
import uz.asos.feature.community.feed.CommunityRoute as CommunityFeedDestinationRoute
import uz.asos.feature.community.post.PostDetailRoute

fun NavController.navigateToCommunity(
    articleNumber: Int = 0,
    navOptions: NavOptions? = null,
) {
    navigate(CommunityRoute(articleNumber = articleNumber), navOptions)
}

fun NavController.navigateToPost(
    postId: Int,
    navOptions: NavOptions? = null,
) {
    navigate(PostRoute(postId = postId), navOptions)
}

fun NavGraphBuilder.communityScreens(
    navController: NavController,
    onNavigate: (AsosRoute) -> Unit,
) {
    composable<CommunityRoute> {
        CommunityFeedDestinationRoute(
            onOpenPost = { postId -> onNavigate(PostRoute(postId = postId)) },
            onOpenBookmarks = { onNavigate(BookmarksRoute) },
        )
    }

    composable<PostRoute> { backStackEntry ->
        val route: PostRoute = backStackEntry.toRoute()
        PostDetailRoute(
            postId = route.postId,
            onBack = { navController.navigateUp() },
            onOpenCommunity = {
                navController.navigateUp()
            },
            onOpenChat = { lawyerId -> onNavigate(ChatRoute(chatId = lawyerId)) },
            onOpenArticle = { articleKey -> onNavigate(ArticleRoute(key = articleKey)) },
        )
    }
}
