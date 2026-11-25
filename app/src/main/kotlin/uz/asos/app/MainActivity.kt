/*
 * MainActivity.kt
 * Module: :app   ·   Work package: WP-00
 *
 * Single activity. Edge-to-edge with dark status-bar icons on the light background,
 * sets AsosTheme { AsosApp() }.
 */
package uz.asos.app

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import uz.asos.app.ui.AsosApp
import uz.asos.core.designsystem.theme.AsosTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
        )
        super.onCreate(savedInstanceState)
        setContent {
            AsosTheme {
                AsosApp()
            }
        }
    }
}
