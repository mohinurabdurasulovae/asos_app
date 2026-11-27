package uz.asos.feature.constitution.article.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.SectionHeader
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.constitution.R
import uz.asos.feature.constitution.article.ExampleUi

@Composable
internal fun RealLifeExamplesSection(
    examples: List<ExampleUi>,
    onCasesClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        SectionHeader(
            title = stringResource(R.string.constitution_examples_title),
            actionLabel = stringResource(R.string.constitution_examples_cases_link),
            onAction = onCasesClick,
        )
        examples.forEach { example -> ExampleCard(example) }
    }
}

@Composable
private fun ExampleCard(example: ExampleUi) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, AsosColors.BorderSoft, AsosShapes.R22)
            .clip(AsosShapes.R22),
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(R.string.constitution_example_situation).uppercase(),
                style = AsosTextStyles.Overline,
                color = AsosColors.Text2,
            )
            Text(text = example.situation, style = AsosTextStyles.Body)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (example.isLawful) AsosColors.SuccessBgSoft else AsosColors.ErrorBg)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                imageVector = if (example.isLawful) AsosIcons.CheckCircleFilled else AsosIcons.XCircleFilled,
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(top = 1.dp)
                    .size(20.dp),
            )
            Text(
                text = example.verdict,
                style = AsosTextStyles.BodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = if (example.isLawful) AsosColors.SuccessText else AsosColors.ErrorOnLight,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RealLifeExamplesSectionPreview() {
    AsosTheme {
        RealLifeExamplesSection(
            examples = listOf(
                ExampleUi(
                    "Nodirning ota-onasi maktabga “homiylik puli” bermagani uchun uni darsdan chetlatishmoqchi.",
                    "Umumiy o‘rta ta’lim bepul. To‘lov qilmagani uchun o‘quvchini darsdan chetlatish 50-moddaga zid.",
                    isLawful = false,
                ),
            ),
            onCasesClick = {},
            modifier = Modifier.padding(24.dp),
        )
    }
}
