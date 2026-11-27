package uz.asos.feature.cases.list.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.feature.cases.R
import uz.asos.feature.cases.data.model.CaseTopic

@Composable
internal fun CaseCategoryChips(
    selected: CaseTopic?,
    onSelect: (CaseTopic?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(AsosSpacing.s),
    ) {
        TopicChip(
            label = stringResource(R.string.cases_filter_all),
            isSelected = selected == null,
            onClick = { onSelect(null) },
        )
        CaseTopic.entries.forEach { topic ->
            TopicChip(
                label = topic.label,
                isSelected = selected == topic,
                onClick = { onSelect(topic) },
            )
        }
    }
}

@Composable
private fun TopicChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = AsosShapes.Pill,
        color = if (isSelected) AsosColors.Navy else AsosColors.Surface,
        border = if (isSelected) null else BorderStroke(1.dp, AsosColors.Border),
        modifier = Modifier.semantics { selected = isSelected },
    ) {
        Text(
            text = label,
            style = AsosTextStyles.Chip,
            color = if (isSelected) AsosColors.Surface else AsosColors.Navy,
            modifier = Modifier.padding(horizontal = AsosSpacing.l, vertical = 9.dp),
        )
    }
}
