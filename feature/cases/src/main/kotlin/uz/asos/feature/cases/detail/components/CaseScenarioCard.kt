package uz.asos.feature.cases.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.component.AsosCard
import uz.asos.core.designsystem.component.AsosCardStyle
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.feature.cases.detail.PersonaUi
import uz.asos.feature.cases.ui.CasesPalette

@Composable
internal fun CaseScenarioCard(
    persona: PersonaUi,
    storyText: String,
    questionText: String,
    modifier: Modifier = Modifier,
) {
    AsosCard(
        modifier = modifier.fillMaxWidth(),
        style = AsosCardStyle.Outlined,
    ) {
        Column(
            modifier = Modifier.padding(AsosSpacing.ll),
            verticalArrangement = Arrangement.spacedBy(AsosSpacing.mm),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AsosSpacing.m),
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(AsosShapes.Circle)
                        .background(CasesPalette.PersonaBg),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = persona.initials,
                        style = AsosTextStyles.CardTitle,
                        color = CasesPalette.PersonaText,
                    )
                }
                Column {
                    Text(
                        text = persona.name,
                        style = AsosTextStyles.RowTitleRegular,
                    )
                    Text(
                        text = persona.subtitle,
                        style = AsosTextStyles.Meta,
                        color = AsosColors.Text2,
                    )
                }
            }

            Text(
                text = storyText,
                style = AsosTextStyles.LegalBody.copy(fontSize = 16.5.sp, lineHeight = 25.sp),
            )

            Text(
                text = questionText,
                style = AsosTextStyles.RowTitleRegular,
                color = AsosColors.Brand600,
            )
        }
    }
}
