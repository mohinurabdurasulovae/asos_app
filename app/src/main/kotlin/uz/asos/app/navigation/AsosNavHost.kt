/*
 * AsosNavHost.kt
 * Module: :app   ·   Work package: WP-00
 *
 * NavHost that calls each feature's <feature>Screens() from its navigation/ file.
 *
 * Implemented features: constitution (Konstitutsiya, Modda), dictionary (Lug‘at).
 * Every other route shows ComingSoonScreen so navigation from the finished screens never
 * crashes. When a feature lands, replace its comingSoon(...) lines with <feature>Screens().
 *
 * Start destination: Konstitutsiya for now. Switch to HomeRoute (or LoginRoute when signed
 * out) once :feature:home and :feature:auth are built.
 */
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
import uz.asos.core.navigation.CaseRoute
import uz.asos.core.navigation.CasesRoute
import uz.asos.core.navigation.ChatRoute
import uz.asos.core.navigation.CommunityRoute
import uz.asos.core.navigation.ConstitutionRoute
import uz.asos.core.navigation.HomeRoute
import uz.asos.core.navigation.LearnRoute
import uz.asos.core.navigation.LegalHelpRoute
import uz.asos.core.navigation.LoginRoute
import uz.asos.core.navigation.PostRoute
import uz.asos.core.navigation.ProfileRoute
import uz.asos.core.navigation.QuizRoute
import uz.asos.core.navigation.SearchRoute
import uz.asos.feature.constitution.navigation.constitutionScreens
import uz.asos.feature.dictionary.navigation.dictionaryScreens

@Composable
fun AsosNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val onNavigate: (AsosRoute) -> Unit = { route -> navController.navigate(route) }
    val onBack: () -> Unit = { navController.navigateUp() }

    NavHost(
        navController = navController,
        startDestination = ConstitutionRoute,
        modifier = modifier,
    ) {
        constitutionScreens(navController = navController, onNavigate = onNavigate)
        dictionaryScreens(navController = navController, onNavigate = onNavigate)

        // Not built yet ------------------------------------------------------------------
        comingSoon<LoginRoute>(R.string.app_screen_login, onBack)
        comingSoon<HomeRoute>(R.string.app_screen_home, onBack = null)
        comingSoon<LearnRoute>(R.string.app_screen_learn, onBack = null)
        comingSoon<QuizRoute>(R.string.app_screen_quiz, onBack)
        comingSoon<CasesRoute>(R.string.app_screen_cases, onBack)
        comingSoon<CaseRoute>(R.string.app_screen_case, onBack)
        comingSoon<CommunityRoute>(R.string.app_screen_community, onBack = null)
        comingSoon<PostRoute>(R.string.app_screen_post, onBack)
        comingSoon<LegalHelpRoute>(R.string.app_screen_legal_help, onBack)
        comingSoon<ChatRoute>(R.string.app_screen_chat, onBack)
        comingSoon<ProfileRoute>(R.string.app_screen_profile, onBack = null)
        comingSoon<SearchRoute>(R.string.app_screen_search, onBack)
        comingSoon<BookmarksRoute>(R.string.app_screen_bookmarks, onBack)
    }
}

/** Placeholder destination. [onBack] = null on tab screens (they have no back button). */
private inline fun <reified T : Any> NavGraphBuilder.comingSoon(
    @StringRes titleRes: Int,
    noinline onBack: (() -> Unit)?,
) {
    composable<T> {
        ComingSoonScreen(titleRes = titleRes, onBack = onBack)
    }
}
