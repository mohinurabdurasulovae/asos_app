/*
 * ChapterRow.kt
 * Module: :feature:constitution   ·   Work package: WP-05
 * Screen: Konstitutsiya (design artboard: Konstitutsiya.dc.html)
 *
 * Bob row: roman numeral tile, title, article range, read status. The chapter being read is
 * highlighted (light blue row, blue tile). Tapping opens its articles underneath.
 */
package uz.asos.feature.constitution.browse.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.ArticleNumberBadge
import uz.asos.core.designsystem.component.ArticleNumberBadgeVariant
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.constitution.R
import uz.asos.feature.constitution.browse.ChapterUi
import uz.asos.feature.constitution.browse.ReadState

@Composable
internal fun ChapterRow(
    chapter: ChapterUi,
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val stateLabel = stringResource(if (expanded) R.string.constitution_collapse else R.string.constitution_expand)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(if (chapter.isHighlighted) AsosColors.Brand25 else AsosColors.Surface)
            .clickable(role = Role.Button, onClickLabel = stateLabel, onClick = onClick)
            .semantics { stateDescription = stateLabel }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        ArticleNumberBadge(
            text = chapter.roman,
            variant = if (chapter.isHighlighted) ArticleNumberBadgeVariant.Brand else ArticleNumberBadgeVariant.Light,
        )
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = chapter.title,
                style = AsosTextStyles.RowTitleRegular.copy(
                    fontWeight = if (chapter.isHighlighted) FontWeight.ExtraBold else FontWeight.Bold,
                ),
            )
            Text(
                text = chapter.meta,
                style = AsosTextStyles.Meta.copy(
                    fontWeight = if (chapter.isHighlighted) FontWeight.SemiBold else FontWeight.Medium,
                ),
                color = if (chapter.isHighlighted) AsosColors.Brand600 else AsosColors.Text2,
            )
        }
        ArticleStatusIcon(state = chapter.state, percent = chapter.percent)
    }
}

@Preview(showBackground = true)
@Composable
private fun ChapterRowPreview() {
    AsosTheme {
        Column {
            ChapterRow(ChapterUi("VII", "Shaxsiy huquq va erkinliklar", "25–35-moddalar", ReadState.READ, 100), false, {})
            ChapterRow(
                ChapterUi("IX", "Iqtisodiy, ijtimoiy, madaniy va ekologik huquqlar", "41–53-moddalar · 8/13 o‘qildi", ReadState.IN_PROGRESS, 62),
                false,
                {},
            )
            ChapterRow(ChapterUi("X", "Inson hamda fuqaroning huquq va erkinliklari kafolatlari", "54–58-moddalar", ReadState.NOT_STARTED, 0), false, {})
        }
    }
}
