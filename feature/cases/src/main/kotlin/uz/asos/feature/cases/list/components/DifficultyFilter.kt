/*
 * DifficultyFilter.kt
 * Module: :feature:cases   ·   Work package: WP-08
 * Screen: Kazuslar (design artboard: Kazuslar.dc.html)
 *
 * "Qiyinlik:" Barchasi / Oson / O'rta / Murakkab, the selected one underlined.
 */
package uz.asos.feature.cases.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.feature.cases.R
import uz.asos.feature.cases.data.model.CaseDifficulty

@Composable
internal fun DifficultyFilter(
    selected: CaseDifficulty?,
    onSelect: (CaseDifficulty?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AsosSpacing.xxs, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AsosSpacing.mm),
    ) {
        Text(
            text = stringResource(R.string.cases_difficulty_label),
            style = AsosTextStyles.Subtitle,
            color = AsosColors.Text2,
        )
        DifficultyTab(
            label = stringResource(R.string.cases_filter_all),
            isSelected = selected == null,
            onClick = { onSelect(null) },
        )
        CaseDifficulty.entries.forEach { difficulty ->
            DifficultyTab(
                label = difficulty.label,
                isSelected = selected == difficulty,
                onClick = { onSelect(difficulty) },
            )
        }
    }
}

@Composable
private fun DifficultyTab(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .semantics { selected = isSelected },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            style = AsosTextStyles.Chip.copy(
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
            ),
            color = if (isSelected) AsosColors.Brand600 else AsosColors.Text2,
        )
        Spacer(modifier = Modifier.height(3.dp))
        Box(
            modifier = Modifier
                .width(if (isSelected) 36.dp else 0.dp)
                .height(2.dp)
                .background(AsosColors.Brand600, RoundedCornerShape(2.dp)),
        )
    }
}
