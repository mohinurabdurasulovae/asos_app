/*
 * AsosTheme.kt
 * Module: :core:designsystem   ·   Work package: WP-01
 *
 * AsosTheme { } wraps MaterialTheme with the Asos colours, typography and shapes.
 * The design is light-only for now.
 */
package uz.asos.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AsosLightColors = lightColorScheme(
    primary = AsosColors.Brand600,
    onPrimary = AsosColors.Surface,
    primaryContainer = AsosColors.Brand50,
    onPrimaryContainer = AsosColors.Brand600,
    secondary = AsosColors.Navy,
    onSecondary = AsosColors.Surface,
    secondaryContainer = AsosColors.SegmentTrack,
    onSecondaryContainer = AsosColors.Navy,
    tertiary = AsosColors.Teal,
    onTertiary = AsosColors.Surface,
    background = AsosColors.Background,
    onBackground = AsosColors.Navy,
    surface = AsosColors.Surface,
    onSurface = AsosColors.Navy,
    surfaceVariant = AsosColors.SurfaceMuted,
    onSurfaceVariant = AsosColors.Text2,
    surfaceContainerLow = AsosColors.Surface,
    surfaceContainer = AsosColors.Surface,
    surfaceContainerHigh = AsosColors.Surface,
    outline = AsosColors.Border,
    outlineVariant = AsosColors.Divider,
    error = AsosColors.Error,
    onError = AsosColors.Surface,
    errorContainer = AsosColors.ErrorBg,
    onErrorContainer = AsosColors.ErrorOnLight,
    inverseSurface = AsosColors.Navy,
    inverseOnSurface = AsosColors.Surface,
)

@Composable
fun AsosTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AsosLightColors,
        typography = AsosMaterialTypography,
        shapes = AsosMaterialShapes,
        content = content,
    )
}
