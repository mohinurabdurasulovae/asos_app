package uz.asos.core.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import uz.asos.core.designsystem.icon.AsosIcons
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val route: AsosRoute,
    val routeClass: KClass<*>,
    val icon: ImageVector,
    @StringRes val labelRes: Int,
) {
    HOME(HomeRoute, HomeRoute::class, AsosIcons.Home, R.string.navigation_tab_home),
    CONSTITUTION(ConstitutionRoute, ConstitutionRoute::class, AsosIcons.TabConstitution, R.string.navigation_tab_constitution),
    LEARN(LearnRoute, LearnRoute::class, AsosIcons.TabLearn, R.string.navigation_tab_learn),
    COMMUNITY(CommunityRoute(), CommunityRoute::class, AsosIcons.TabCommunity, R.string.navigation_tab_community),
    PROFILE(ProfileRoute, ProfileRoute::class, AsosIcons.Person, R.string.navigation_tab_profile),
}
