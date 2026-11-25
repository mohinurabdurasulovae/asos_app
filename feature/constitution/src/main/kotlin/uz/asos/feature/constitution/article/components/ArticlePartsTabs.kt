/*
 * ArticlePartsTabs.kt
 * Module: :feature:constitution   ·   Work package: WP-05
 * Screen: Modda (50-modda) (design artboard: Modda.dc.html)
 *
 * "Modda bo‘limlari" tabs: Rasmiy matn · Sodda tilda · Misollar · Bog‘liq. Tapping a tab
 * scrolls to that part of the page; the active tab follows the scroll position.
 */
package uz.asos.feature.constitution.article.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.constitution.R

/** The parts of the reader the tabs jump to. */
enum class ReaderSection(@StringRes val labelRes: Int) {
    OFFICIAL(R.string.constitution_tab_official),
    PLAIN(R.string.constitution_tab_plain),
    EXAMPLES(R.string.constitution_tab_examples),
    RELATED(R.string.constitution_tab_related),
}

@Composable
internal fun ArticlePartsTabs(
    sections: List<ReaderSection>,
    selected: ReaderSection,
    onSelect: (ReaderSection) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navLabel = stringResource(R.string.constitution_parts_nav_label)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(AsosColors.Surface)
            .semantics { contentDescription = navLabel },
    ) {
        HorizontalDivider(
            thickness = 1.dp,
            color = AsosColors.Divider,
            modifier = Modifier.align(Alignment.BottomStart),
        )
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            sections.forEach { section ->
                val isSelected = section == selected
                Column(
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .clip(AsosShapes.R10)
                        .semantics { this.selected = isSelected }
                        .clickable(role = Role.Tab) { onSelect(section) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(section.labelRes),
                        style = AsosTextStyles.Segment.copy(
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                        ),
                        color = if (isSelected) AsosColors.Navy else AsosColors.Text2,
                        maxLines = 1,
                        modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 11.dp),
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.5.dp)
                            .background(if (isSelected) AsosColors.Brand500 else Color.Transparent),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ArticlePartsTabsPreview() {
    AsosTheme {
        ArticlePartsTabs(
            sections = ReaderSection.entries,
            selected = ReaderSection.OFFICIAL,
            onSelect = {},
        )
    }
}
