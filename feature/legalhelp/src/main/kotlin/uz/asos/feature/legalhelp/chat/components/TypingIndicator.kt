/*
 * TypingIndicator.kt
 * Module: :feature:legalhelp   ·   Work package: WP-10
 *
 * Three animated dots in a small bubble + "Sardor yozmoqda…".
 */
package uz.asos.feature.legalhelp.chat.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.legalhelp.R

@Composable
internal fun TypingIndicator(
    name: String,
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition(label = "typing")
    Row(
        modifier = modifier.semantics { liveRegion = LiveRegionMode.Polite },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .background(AsosColors.Surface, RoundedCornerShape(16.dp, 16.dp, 16.dp, 6.dp))
                .padding(horizontal = AsosSpacing.m, vertical = AsosSpacing.sm),
            horizontalArrangement = Arrangement.spacedBy(AsosSpacing.xxs),
        ) {
            repeat(3) { index ->
                val alpha by transition.animateFloat(
                    initialValue = 0.3f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 450, delayMillis = index * 150),
                        repeatMode = RepeatMode.Reverse,
                    ),
                    label = "dot$index",
                )
                Box(
                    Modifier
                        .size(6.dp)
                        .alpha(alpha)
                        .background(AsosColors.SoftGray, AsosShapes.Circle),
                )
            }
        }
        Text(
            text = stringResource(R.string.legalhelp_chat_typing, name),
            style = AsosTextStyles.Meta.copy(color = AsosColors.Brand600),
            modifier = Modifier.padding(start = AsosSpacing.s),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC)
@Composable
private fun TypingIndicatorPreview() {
    AsosTheme { TypingIndicator(name = "Sardor", modifier = Modifier.padding(16.dp)) }
}
