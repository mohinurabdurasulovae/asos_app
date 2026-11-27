/*
 * FeedSortTabs.kt
 * Module: :feature:community   ·   Work package: WP-09
 * Screen: Hamjamiyat (design artboard: Hamjamiyat.dc.html)
 *
 * Ommabop · Yangi · Javobsiz · Obunalar.
 */
package uz.asos.feature.community.feed.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.model.community.PostSort
import uz.asos.feature.community.R

@Composable
internal fun FeedSortTabs(
    selectedSort: PostSort,
    onSelect: (PostSort) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AsosSpacing.s),
    ) {
        PostSort.entries.forEach { sort ->
            val isSelected = sort == selectedSort
            Surface(
                onClick = { onSelect(sort) },
                shape = AsosShapes.Pill,
                color = if (isSelected) AsosColors.Brand50 else AsosColors.SurfaceMuted,
                modifier = Modifier.semantics { selected = isSelected },
            ) {
                Text(
                    text = stringResource(sort.labelRes()),
                    style = AsosTextStyles.Segment,
                    color = if (isSelected) AsosColors.Brand600 else AsosColors.Text2,
                    modifier = Modifier.padding(
                        horizontal = AsosSpacing.m,
                        vertical = AsosSpacing.s,
                    ),
                )
            }
        }
    }
}

private fun PostSort.labelRes(): Int = when (this) {
    PostSort.POPULAR -> R.string.community_sort_popular
    PostSort.NEW -> R.string.community_sort_new
    PostSort.UNANSWERED -> R.string.community_sort_unanswered
    PostSort.SUBSCRIPTIONS -> R.string.community_sort_subscriptions
}
