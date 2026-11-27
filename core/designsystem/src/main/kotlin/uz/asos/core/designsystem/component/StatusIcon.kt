package uz.asos.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosTheme

sealed interface AsosStatus {
    data object Read : AsosStatus
    data class InProgress(val percent: Int) : AsosStatus
    data object NotStarted : AsosStatus
    data object Correct : AsosStatus
    data object Wrong : AsosStatus
}

@Composable
fun StatusIcon(
    status: AsosStatus,
    modifier: Modifier = Modifier,
    size: Dp = 22.dp,
) {
    when (status) {
        AsosStatus.Read -> Icon(
            imageVector = AsosIcons.CheckCircleFilled,
            contentDescription = "O‘qilgan",
            tint = Color.Unspecified,
            modifier = modifier.size(size),
        )
        AsosStatus.Correct -> Icon(
            imageVector = AsosIcons.CheckCircleFilled,
            contentDescription = "To‘g‘ri",
            tint = Color.Unspecified,
            modifier = modifier.size(size),
        )
        AsosStatus.Wrong -> Icon(
            imageVector = AsosIcons.XCircleFilled,
            contentDescription = "Noto‘g‘ri",
            tint = Color.Unspecified,
            modifier = modifier.size(size),
        )
        AsosStatus.NotStarted -> Icon(
            imageVector = AsosIcons.CircleOutline,
            contentDescription = "Boshlanmagan",
            tint = Color.Unspecified,
            modifier = modifier.size(size),
        )
        is AsosStatus.InProgress -> ProgressRing(
            progress = status.percent / 100f,
            size = size + 4.dp,
            strokeWidth = 3.dp,
            contentDescription = "${status.percent} foiz o‘qilgan",
            modifier = modifier,
        )
    }
}

@Preview
@Composable
private fun StatusIconPreview() {
    AsosTheme {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatusIcon(AsosStatus.Read)
            StatusIcon(AsosStatus.InProgress(62))
            StatusIcon(AsosStatus.NotStarted)
            StatusIcon(AsosStatus.Correct)
            StatusIcon(AsosStatus.Wrong)
        }
    }
}
