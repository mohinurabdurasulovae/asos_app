/*
 * AsosApp.kt
 * Module: :app   ·   Work package: WP-00
 *
 * Root scaffold: AsosNavHost + AsosBottomBar (visible on the 5 top-level tabs only).
 * Screens draw their own status-bar inset; the scaffold only reserves the bottom bar.
 */
package uz.asos.app.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import uz.asos.app.navigation.AsosNavHost
import uz.asos.core.designsystem.component.AsosBottomBar
import uz.asos.core.designsystem.component.AsosBottomBarItem
import uz.asos.core.designsystem.theme.AsosColors

@Composable
fun AsosApp(
    appState: AsosAppState = rememberAsosAppState(),
) {
    val currentTab = appState.currentTopLevelDestination

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AsosColors.Background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                AsosBottomBar {
                    appState.topLevelDestinations.forEach { destination ->
                        AsosBottomBarItem(
                            selected = destination == currentTab,
                            onClick = { appState.navigateToTopLevelDestination(destination) },
                            icon = destination.icon,
                            label = stringResource(destination.labelRes),
                        )
                    }
                }
            }
        },
    ) { padding ->
        AsosNavHost(
            navController = appState.navController,
            modifier = Modifier.padding(padding),
        )
    }
}
