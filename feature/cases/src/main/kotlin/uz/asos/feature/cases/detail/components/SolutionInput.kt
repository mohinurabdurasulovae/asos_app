/*
 * SolutionInput.kt
 * Module: :feature:cases   ·   Work package: WP-08
 * Screen: Kazus — yechim (design artboard: Kazus.dc.html)
 *
 * "Sizning yechimingiz" text area + "Yechimni tekshirish" button. The button stays disabled
 * until an article is picked and a few words are written; the field locks after checking.
 */
package uz.asos.feature.cases.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.AsosButton
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.feature.cases.R

@Composable
internal fun SolutionInput(
    solution: String,
    isSubmitted: Boolean,
    canSubmit: Boolean,
    onSolutionChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AsosSpacing.sm),
    ) {
        Text(
            text = stringResource(R.string.cases_solution_heading),
            style = AsosTextStyles.CardTitle,
        )

        OutlinedTextField(
            value = solution,
            onValueChange = onSolutionChange,
            enabled = !isSubmitted,
            textStyle = AsosTextStyles.BodySmall,
            placeholder = {
                Text(
                    text = stringResource(R.string.cases_solution_placeholder),
                    style = AsosTextStyles.BodySmall,
                    color = AsosColors.SoftGray,
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 104.dp),
            shape = AsosShapes.R16,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = AsosColors.Surface,
                unfocusedContainerColor = AsosColors.Surface,
                disabledContainerColor = AsosColors.Surface,
                focusedBorderColor = AsosColors.Brand600,
                unfocusedBorderColor = AsosColors.Border,
                disabledBorderColor = AsosColors.Border,
                disabledTextColor = AsosColors.Navy,
            ),
        )

        if (!isSubmitted) {
            AsosButton(
                text = stringResource(R.string.cases_solution_check),
                onClick = onSubmit,
                enabled = canSubmit,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
