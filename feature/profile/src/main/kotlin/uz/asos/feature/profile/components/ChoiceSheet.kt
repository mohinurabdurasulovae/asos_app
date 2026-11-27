package uz.asos.feature.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles

internal data class SheetChoice<T>(
    val value: T,
    val label: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun <T> ChoiceSheet(
    title: String,
    choices: List<SheetChoice<T>>,
    selectedValue: T,
    onSelect: (T) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = AsosColors.Surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AsosSpacing.xl)
                .padding(bottom = AsosSpacing.xxl),
            verticalArrangement = Arrangement.spacedBy(AsosSpacing.s),
        ) {
            Text(text = title, style = AsosTextStyles.SectionTitle)

            choices.forEach { choice ->
                val isSelected = choice.value == selectedValue
                Surface(
                    onClick = { onSelect(choice.value) },
                    color = AsosColors.Surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { selected = isSelected },
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = AsosSpacing.m),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AsosSpacing.m),
                    ) {
                        Text(
                            text = choice.label,
                            style = AsosTextStyles.RowTitleRegular,
                            color = if (isSelected) AsosColors.Brand600 else AsosColors.Navy,
                            modifier = Modifier.weight(1f),
                        )
                        if (isSelected) {
                            Icon(
                                imageVector = AsosIcons.Check,
                                contentDescription = null,
                                tint = AsosColors.Brand600,
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}
