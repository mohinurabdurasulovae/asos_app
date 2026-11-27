/*
 * ProfileNavigation.kt
 * Module: :feature:profile   ·   Work package: WP-11
 *
 * Public entry point of this feature: the only file the app module uses.
 *  - ProfileRoute → Profil (tab)
 */
package uz.asos.feature.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import uz.asos.core.navigation.AsosRoute
import uz.asos.core.navigation.BookmarksRoute
import uz.asos.core.navigation.CommunityRoute
import uz.asos.core.navigation.DictionaryRoute
import uz.asos.core.navigation.LoginRoute
import uz.asos.core.model.user.BookmarkType
import uz.asos.feature.profile.ProfileRoute
import uz.asos.core.navigation.ProfileRoute as ProfileDestination

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    navigate(ProfileDestination, navOptions)
}

/**
 * @param onNavigate opens screens of other features through AsosRoutes.
 */
fun NavGraphBuilder.profileScreens(onNavigate: (AsosRoute) -> Unit) {
    composable<ProfileDestination> {
        ProfileRoute(
            onOpenSaved = { type -> onNavigate(type.savedRoute()) },
            onSignIn = { onNavigate(LoginRoute) },
            onLogout = { onNavigate(LoginRoute) },
        )
    }
}

/** Saved terms and discussions have their own screens; the rest go to Saqlanganlar. */
private fun BookmarkType.savedRoute(): AsosRoute = when (this) {
    BookmarkType.TERM -> DictionaryRoute()
    BookmarkType.POST -> CommunityRoute()
    BookmarkType.ARTICLE, BookmarkType.CASE -> BookmarksRoute
}
