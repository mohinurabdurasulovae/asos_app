/*
 * ArticleStatusIcon.kt
 * Module: :feature:constitution   ·   Work package: WP-05
 * Screen: Konstitutsiya (design artboard: Konstitutsiya.dc.html)
 *
 * O‘qilgan (filled check), in progress (ring with percent), Boshlanmagan (empty circle).
 * Maps the screen's ReadState onto the shared StatusIcon.
 */
package uz.asos.feature.constitution.browse.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.AsosStatus
import uz.asos.core.designsystem.component.StatusIcon
import uz.asos.feature.constitution.browse.ReadState

@Composable
internal fun ArticleStatusIcon(
    state: ReadState,
    modifier: Modifier = Modifier,
    percent: Int = 50,
    size: Dp = 22.dp,
) {
    val status = when (state) {
        ReadState.READ -> AsosStatus.Read
        ReadState.IN_PROGRESS -> AsosStatus.InProgress(percent)
        ReadState.NOT_STARTED -> AsosStatus.NotStarted
    }
    StatusIcon(status = status, modifier = modifier, size = size)
}
