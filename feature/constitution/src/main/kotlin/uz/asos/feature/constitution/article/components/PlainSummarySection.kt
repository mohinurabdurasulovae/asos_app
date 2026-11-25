/*
 * PlainSummarySection.kt
 * Module: :feature:constitution   ·   Work package: WP-05
 * Screen: Modda (50-modda) (design artboard: Modda.dc.html)
 *
 * "Sodda qilib aytganda": light blue card with a lightbulb, bullet list and the reviewer /
 * disclaimer line. Editorial content, not the official text.
 */
package uz.asos.feature.constitution.article.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.VerifiedBadge
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.constitution.R
import uz.asos.feature.constitution.article.SummaryUi

@Composable
internal fun PlainSummarySection(
    summary: SummaryUi,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AsosColors.Brand50, AsosShapes.R24)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(AsosColors.Surface, AsosShapes.R11),
                contentAlignment = Alignment.Center,
            ) {
                Icon(AsosIcons.Lightbulb, contentDescription = null, tint = AsosColors.Brand600, modifier = Modifier.size(19.dp))
            }
            Text(
                text = stringResource(R.string.constitution_plain_title),
                style = AsosTextStyles.CardTitle,
                modifier = Modifier.semantics { heading() },
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            summary.bullets.forEach { bullet ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(
                        modifier = Modifier
                            .padding(top = 9.dp)
                            .size(6.dp)
                            .background(AsosColors.Brand500, AsosShapes.Circle),
                    )
                    Text(text = bullet, style = AsosTextStyles.Body, modifier = Modifier.weight(1f))
                }
            }
        }
        HorizontalDivider(thickness = 1.dp, color = AsosColors.Brand200)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            VerifiedBadge(contentDescription = null)
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                if (summary.reviewedBy != null) {
                    Text(
                        text = stringResource(R.string.constitution_plain_reviewed_by, summary.reviewedBy),
                        style = AsosTextStyles.Meta.copy(fontWeight = FontWeight.Bold),
                        color = AsosColors.Text3,
                    )
                }
                Text(
                    text = summary.disclaimer ?: stringResource(R.string.constitution_plain_default_disclaimer),
                    style = AsosTextStyles.Meta.copy(fontWeight = FontWeight.SemiBold),
                    color = AsosColors.Text3,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlainSummarySectionPreview() {
    AsosTheme {
        PlainSummarySection(
            summary = SummaryUi(
                bullets = listOf(
                    "O‘qish — har bir insonning huquqi. Yoshingiz, millatingiz yoki oilangizning moddiy ahvoli bunga to‘siq bo‘lmasligi kerak.",
                    "Umumiy o‘rta ta’lim bepul — maktabda o‘qish uchun pul talab qilinmaydi.",
                ),
                reviewedBy = null,
                disclaimer = null,
            ),
            modifier = Modifier.padding(24.dp),
        )
    }
}
