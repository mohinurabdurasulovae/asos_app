/*
 * CaseListItem.kt
 * Module: :feature:cases   ·   Work package: WP-08
 * Screen: Kazuslar (design artboard: Kazuslar.dc.html)
 *
 * Number, category tag, difficulty bars, status badge, title, summary.
 */
package uz.asos.feature.cases.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.AsosCard
import uz.asos.core.designsystem.component.AsosCardStyle
import uz.asos.core.designsystem.component.AsosTag
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTones
import uz.asos.feature.cases.data.model.CaseStatus
import uz.asos.feature.cases.list.CaseRowUi
import uz.asos.feature.cases.ui.CaseDifficultyBars
import uz.asos.feature.cases.ui.tone

@Composable
internal fun CaseListItem(
    case: CaseRowUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AsosCard(
        modifier = modifier.fillMaxWidth(),
        shape = AsosShapes.R18,
        style = AsosCardStyle.Outlined,
        onClick = onClick,
    ) {
        Column(modifier = Modifier.padding(AsosSpacing.l)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AsosSpacing.s),
                ) {
                    Text(
                        text = case.numberLabel,
                        style = AsosTextStyles.Meta,
                        color = AsosColors.SoftGray,
                    )
                    AsosTag(text = case.topic.label, tone = case.topic.tone())
                    CaseDifficultyBars(difficulty = case.difficulty, onDark = false)
                }

                CaseStatusBadge(status = case.status)
            }

            Spacer(modifier = Modifier.height(AsosSpacing.sm))

            Text(
                text = case.title,
                style = AsosTextStyles.CardTitle,
            )

            Spacer(modifier = Modifier.height(AsosSpacing.xxs))

            Text(
                text = case.shortDescription,
                style = AsosTextStyles.BodySmall,
                color = AsosColors.Text2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun CaseStatusBadge(status: CaseStatus) {
    when (status) {
        CaseStatus.YECHILGAN -> Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AsosSpacing.xxs),
        ) {
            Icon(
                imageVector = AsosIcons.CheckCircleFilled,
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = status.label,
                style = AsosTextStyles.Tag,
                color = AsosColors.SuccessOnLight,
            )
        }

        CaseStatus.YANGI -> Surface(
            shape = AsosShapes.R10,
            color = AsosColors.Brand50,
        ) {
            Text(
                text = status.label,
                style = AsosTextStyles.Tag,
                color = AsosColors.Brand600,
                modifier = Modifier.padding(horizontal = AsosSpacing.sm, vertical = AsosSpacing.xxs),
            )
        }

        CaseStatus.JARAYONDA -> Surface(
            shape = AsosShapes.R10,
            color = AsosTones.Orange.container,
        ) {
            Text(
                text = status.label,
                style = AsosTextStyles.Tag,
                color = AsosTones.Orange.content,
                modifier = Modifier.padding(horizontal = AsosSpacing.sm, vertical = AsosSpacing.xxs),
            )
        }
    }
}
