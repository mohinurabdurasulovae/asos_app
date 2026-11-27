package uz.asos.feature.dictionary.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.feature.dictionary.R

@Composable
internal fun SavedTermsFilter(
    savedOnly: Boolean,
    savedCount: Int,
    onSavedOnlyChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(AsosSpacing.s),
    ) {
        FilterPill(
            label = stringResource(R.string.dictionary_tab_all),
            isSelected = !savedOnly,
            onClick = { onSavedOnlyChange(false) },
        )
        FilterPill(
            label = stringResource(R.string.dictionary_tab_saved),
            isSelected = savedOnly,
            badge = savedCount.takeIf { it > 0 }?.toString(),
            onClick = { onSavedOnlyChange(true) },
        )
    }
}

@Composable
private fun FilterPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    badge: String? = null,
) {
    Surface(
        onClick = onClick,
        shape = AsosShapes.Pill,
        color = if (isSelected) AsosColors.Navy else AsosColors.Surface,
        border = if (isSelected) null else BorderStroke(1.dp, AsosColors.Border),
        modifier = Modifier
            .height(40.dp)
            .semantics { selected = isSelected },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = AsosSpacing.l),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AsosSpacing.xs),
        ) {
            Text(
                text = label,
                style = AsosTextStyles.Chip,
                color = if (isSelected) AsosColors.Surface else AsosColors.Navy,
            )
            if (badge != null) {
                Surface(
                    shape = AsosShapes.Pill,
                    color = if (isSelected) AsosColors.Brand600 else AsosColors.Brand50,
                ) {
                    Text(
                        text = badge,
                        style = AsosTextStyles.Tag,
                        color = if (isSelected) AsosColors.Surface else AsosColors.Brand600,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                    )
                }
            }
        }
    }
}
