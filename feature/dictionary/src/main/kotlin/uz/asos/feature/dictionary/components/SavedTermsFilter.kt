/*
 * SavedTermsFilter.kt
 * Module: :feature:dictionary   ·   Work package: WP-06
 * Screen: Konstitutsiyaviy lug‘at (design artboard: Lugat.dc.html)
 *
 * "Barcha atamalar" / "Saqlangan [12]" filter pills. Selected = navy, as AsosChip.
 */
package uz.asos.feature.dictionary.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.dictionary.DictionaryFilter
import uz.asos.feature.dictionary.R

@Composable
internal fun SavedTermsFilter(
    selected: DictionaryFilter,
    savedCount: Int?,
    onSelect: (DictionaryFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FilterPill(
            label = stringResource(R.string.dictionary_filter_all),
            selected = selected == DictionaryFilter.ALL,
            onClick = { onSelect(DictionaryFilter.ALL) },
        )
        val savedLabel = stringResource(R.string.dictionary_filter_saved)
        val savedDescription = savedCount?.let { "$savedLabel, ${stringResource(R.string.dictionary_saved_count, it)}" }
        FilterPill(
            label = savedLabel,
            selected = selected == DictionaryFilter.SAVED,
            onClick = { onSelect(DictionaryFilter.SAVED) },
            badge = savedCount?.toString(),
            description = savedDescription,
        )
    }
}

@Composable
private fun FilterPill(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    badge: String? = null,
    description: String? = null,
) {
    val shape = AsosShapes.Pill
    Row(
        modifier = Modifier
            .heightIn(min = 44.dp)
            .clip(shape)
            .then(
                if (selected) {
                    Modifier.background(AsosColors.Navy, shape)
                } else {
                    Modifier
                        .background(AsosColors.Surface, shape)
                        .border(1.dp, AsosColors.Border, shape)
                },
            )
            .semantics {
                this.selected = selected
                if (description != null) contentDescription = description
            }
            .clickable(role = Role.Tab, onClick = onClick)
            .padding(start = 18.dp, end = if (badge != null) 8.dp else 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = label,
            style = AsosTextStyles.Chip,
            color = if (selected) AsosColors.Surface else AsosColors.Navy,
            maxLines = 1,
        )
        if (badge != null) {
            Box(
                modifier = Modifier
                    .height(24.dp)
                    .widthIn(min = 26.dp)
                    .background(if (selected) AsosColors.Brand500 else AsosColors.Brand50, shape)
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = badge,
                    style = AsosTextStyles.Tag.copy(fontSize = 12.sp),
                    color = if (selected) AsosColors.Surface else AsosColors.Brand600,
                    maxLines = 1,
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC)
@Composable
private fun SavedTermsFilterPreview() {
    AsosTheme { SavedTermsFilter(selected = DictionaryFilter.ALL, savedCount = 12, onSelect = {}) }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC)
@Composable
private fun SavedTermsFilterSavedPreview() {
    AsosTheme { SavedTermsFilter(selected = DictionaryFilter.SAVED, savedCount = 3, onSelect = {}) }
}
