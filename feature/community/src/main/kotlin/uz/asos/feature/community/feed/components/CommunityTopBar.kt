package uz.asos.feature.community.feed.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.feature.community.R

@Composable
internal fun CommunityTopBar(
    onSearchClick: () -> Unit,
    onSavedClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AsosSpacing.xxs),
    ) {
        Text(
            text = stringResource(R.string.community_title),
            style = AsosTextStyles.ScreenTitle,
            modifier = Modifier
                .weight(1f)
                .semantics { heading() },
        )

        IconButton(onClick = onSearchClick) {
            Icon(
                imageVector = AsosIcons.Search,
                contentDescription = stringResource(R.string.community_search),
                tint = AsosColors.Navy,
                modifier = Modifier.size(22.dp),
            )
        }
        IconButton(onClick = onSavedClick) {
            Icon(
                imageVector = AsosIcons.Bookmark,
                contentDescription = stringResource(R.string.community_saved),
                tint = AsosColors.Navy,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}
