/*
 * Spacing.kt
 * Module: :core:designsystem   ·   Work package: WP-01
 *
 * Spacing scale (4, 8, 10, 12, 14, 16, 18, 20, 24, 28 dp) and screen gutters.
 */
package uz.asos.core.designsystem.theme

import androidx.compose.ui.unit.dp

object AsosSpacing {
    val xxs = 4.dp
    val xs = 6.dp
    val s = 8.dp
    val sm = 10.dp
    val m = 12.dp
    val mm = 14.dp
    val l = 16.dp
    val ll = 18.dp
    val xl = 20.dp
    val xxl = 24.dp
    val xxxl = 28.dp

    /** Side padding of most screens. */
    val screenGutter = 20.dp
    /** Side padding of the reading screen (Modda). */
    val readerGutter = 24.dp

    /** Minimum touch target (TEAM_PLAN accessibility rule: 44 dp). */
    val minTouch = 44.dp
}
