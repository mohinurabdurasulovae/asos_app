package uz.asos.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = AsosTextStyles.SectionTitle,
            modifier = Modifier
                .weight(1f, fill = false)
                .semantics { heading() },
        )
        if (actionLabel != null && onAction != null) {
            Text(
                text = actionLabel,
                style = AsosTextStyles.Link,
                modifier = Modifier
                    .clip(AsosShapes.R10)
                    .clickable(role = Role.Button, onClick = onAction)
                    .padding(horizontal = 6.dp, vertical = 12.dp),
            )
        }
    }
}

@Preview
@Composable
private fun SectionHeaderPreview() {
    AsosTheme {
        SectionHeader("Hayotiy misollar", actionLabel = "Kazuslar →", onAction = {}, modifier = Modifier.padding(16.dp))
    }
}
