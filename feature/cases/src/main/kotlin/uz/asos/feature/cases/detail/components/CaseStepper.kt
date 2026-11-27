package uz.asos.feature.cases.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.feature.cases.R

@Composable
internal fun CaseStepper(
    currentStep: Int,
    modifier: Modifier = Modifier,
) {
    val labels = listOf(
        stringResource(R.string.cases_step_scenario),
        stringResource(R.string.cases_step_article),
        stringResource(R.string.cases_step_solution),
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AsosSpacing.s),
    ) {
        labels.forEachIndexed { index, label ->
            val stepNumber = index + 1
            val reached = stepNumber <= currentStep
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(AsosShapes.Pill)
                        .background(if (reached) AsosColors.Brand500 else AsosColors.Outline),
                )
                Spacer(modifier = Modifier.height(AsosSpacing.xs))
                Text(
                    text = label,
                    style = AsosTextStyles.Tag.copy(
                        fontWeight = if (stepNumber == currentStep) FontWeight.ExtraBold else FontWeight.Medium,
                    ),
                    color = if (reached) AsosColors.Brand600 else AsosColors.Text2,
                )
            }
        }
    }
}
