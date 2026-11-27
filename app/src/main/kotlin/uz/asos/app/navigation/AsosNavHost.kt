package uz.asos.app.navigation

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uz.asos.app.R
import uz.asos.app.ui.ComingSoonScreen
import uz.asos.core.navigation.AsosRoute
import uz.asos.core.navigation.BookmarksRoute
import uz.asos.core.navigation.HomeRoute
import uz.asos.core.navigation.SearchRoute
import uz.asos.feature.auth.navigation.authScreens
import uz.asos.feature.cases.navigation.casesScreens
import uz.asos.feature.community.navigation.communityScreens
import uz.asos.feature.constitution.navigation.constitutionScreens
import uz.asos.feature.dictionary.navigation.dictionaryScreens
import uz.asos.feature.home.navigation.homeScreens
import uz.asos.feature.learn.navigation.learnScreens
import uz.asos.feature.legalhelp.navigation.legalHelpScreens
import uz.asos.feature.profile.navigation.profileScreens

@Composable
fun AsosNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val onNavigate: (AsosRoute) -> Unit = { route -> navController.navigate(route) }
    val onBack: () -> Unit = { navController.navigateUp() }

    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier,
    ) {
        authScreens(navController = navController)
        homeScreens(onNavigate = onNavigate)
        constitutionScreens(navController = navController, onNavigate = onNavigate)
        dictionaryScreens(navController = navController, onNavigate = onNavigate)
        learnScreens(navController = navController, onNavigate = onNavigate)
        casesScreens(navController = navController, onNavigate = onNavigate)
        communityScreens(navController = navController, onNavigate = onNavigate)
        legalHelpScreens(navController = navController, onNavigate = onNavigate)
        profileScreens(onNavigate = onNavigate)

        comingSoon<SearchRoute>(R.string.app_screen_search, onBack)
        comingSoon<BookmarksRoute>(R.string.app_screen_bookmarks, onBack)
    }
}

private inline fun <reified T : Any> NavGraphBuilder.comingSoon(
    @StringRes titleRes: Int,
    noinline onBack: (() -> Unit)?,
) {
    composable<T> {
        ComingSoonScreen(titleRes = titleRes, onBack = onBack)
    }
}
