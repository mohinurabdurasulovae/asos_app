package uz.asos.feature.constitution.article.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.constitution.R
import uz.asos.feature.constitution.article.DiscussionUi

@Composable
internal fun DiscussionCard(
    discussion: DiscussionUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AsosColors.Navy, AsosShapes.R20)
            .clip(AsosShapes.R20)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(AsosColors.BrandOnNavy.copy(alpha = 0.18f), AsosShapes.R12),
            contentAlignment = Alignment.Center,
        ) {
            Icon(AsosIcons.Comment, contentDescription = null, tint = AsosColors.BrandOnNavySoft, modifier = Modifier.size(20.dp))
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = stringResource(R.string.constitution_discussion_count, discussion.count),
                style = AsosTextStyles.RowTitle.copy(fontSize = 14.5.sp),
                color = AsosColors.Surface,
            )
            Text(
                text = discussion.lastTitle ?: stringResource(R.string.constitution_discussion_empty),
                style = AsosTextStyles.Meta,
                color = AsosColors.TextOnNavy,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Icon(AsosIcons.ChevronRight, contentDescription = null, tint = AsosColors.Surface, modifier = Modifier.size(18.dp))
    }
}

@Preview
@Composable
private fun DiscussionCardPreview() {
    AsosTheme {
        DiscussionCard(
            discussion = DiscussionUi(24, "Kontrakt to‘lovi kechiksa, darsga kiritmaslik mumkinmi?"),
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
