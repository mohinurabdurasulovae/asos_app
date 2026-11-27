package uz.asos.feature.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.AsosCard
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.model.user.BookmarkCounts
import uz.asos.core.model.user.BookmarkType
import uz.asos.feature.profile.R

@Composable
internal fun SavedItemsSection(
    counts: BookmarkCounts,
    onOpen: (BookmarkType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AsosSpacing.m),
    ) {
        Text(
            text = stringResource(R.string.profile_saved),
            style = AsosTextStyles.CardTitle,
        )

        AsosCard(modifier = Modifier.fillMaxWidth()) {
            SavedRow(
                icon = AsosIcons.Document,
                label = stringResource(R.string.profile_saved_articles),
                count = counts.articles,
                onClick = { onOpen(BookmarkType.ARTICLE) },
            )
            HorizontalDivider(color = AsosColors.Divider)
            SavedRow(
                icon = AsosIcons.Comment,
                label = stringResource(R.string.profile_saved_posts),
                count = counts.posts,
                onClick = { onOpen(BookmarkType.POST) },
            )
            HorizontalDivider(color = AsosColors.Divider)
            SavedRow(
                icon = AsosIcons.Book,
                label = stringResource(R.string.profile_saved_terms),
                count = counts.terms,
                onClick = { onOpen(BookmarkType.TERM) },
            )
            if (counts.cases > 0) {
                HorizontalDivider(color = AsosColors.Divider)
                SavedRow(
                    icon = AsosIcons.Scales,
                    label = stringResource(R.string.profile_saved_cases),
                    count = counts.cases,
                    onClick = { onOpen(BookmarkType.CASE) },
                )
            }
        }
    }
}

@Composable
private fun SavedRow(
    icon: ImageVector,
    label: String,
    count: Int,
    onClick: () -> Unit,
) {
    Surface(onClick = onClick, color = AsosColors.Surface) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AsosSpacing.l, vertical = AsosSpacing.mm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AsosSpacing.m),
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(AsosShapes.R10)
                    .background(AsosColors.Brand50),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = AsosColors.Brand600,
                    modifier = Modifier.size(18.dp),
                )
            }

            Text(
                text = label,
                style = AsosTextStyles.RowTitleRegular,
                modifier = Modifier.weight(1f),
            )

            Text(
                text = count.toString(),
                style = AsosTextStyles.Tag,
                color = AsosColors.Text2,
            )

            Icon(
                imageVector = AsosIcons.ChevronRight,
                contentDescription = null,
                tint = AsosColors.SoftGray,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}
