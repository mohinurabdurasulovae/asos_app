package uz.asos.feature.cases.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import uz.asos.core.designsystem.component.AsosButton
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.feature.cases.R
import uz.asos.feature.cases.data.model.CaseStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CasesFilterSheet(
    statusFilter: CaseStatus?,
    onlyBookmarked: Boolean,
    onStatusSelect: (CaseStatus?) -> Unit,
    onOnlyBookmarkedChange: (Boolean) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = AsosColors.Surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AsosSpacing.xl, vertical = AsosSpacing.s)
                .padding(bottom = AsosSpacing.xxxl),
            verticalArrangement = Arrangement.spacedBy(AsosSpacing.l),
        ) {
            Text(
                text = stringResource(R.string.cases_filter_sheet_title),
                style = AsosTextStyles.SectionTitle,
            )

            Text(
                text = stringResource(R.string.cases_filter_by_status),
                style = AsosTextStyles.Subtitle,
                color = AsosColors.Text2,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(AsosSpacing.s)) {
                StatusChip(
                    label = stringResource(R.string.cases_filter_all),
                    isSelected = statusFilter == null,
                    onClick = { onStatusSelect(null) },
                )
                CaseStatus.entries.forEach { status ->
                    StatusChip(
                        label = status.label,
                        isSelected = statusFilter == status,
                        onClick = { onStatusSelect(status) },
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.cases_filter_only_saved),
                    style = AsosTextStyles.RowTitleRegular,
                )
                Switch(
                    checked = onlyBookmarked,
                    onCheckedChange = onOnlyBookmarkedChange,
                    colors = SwitchDefaults.colors(checkedTrackColor = AsosColors.Brand600),
                )
            }

            AsosButton(
                text = stringResource(R.string.cases_filter_apply),
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatusChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(text = label, style = AsosTextStyles.Tag) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = AsosColors.Brand50,
            selectedLabelColor = AsosColors.Brand600,
            labelColor = AsosColors.Navy,
        ),
    )
}
