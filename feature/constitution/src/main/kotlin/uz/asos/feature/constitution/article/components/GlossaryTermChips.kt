/*
 * GlossaryTermChips.kt
 * Module: :feature:constitution   ·   Work package: WP-05
 * Screen: Modda (50-modda) (design artboard: Modda.dc.html)
 *
 * "Lug‘atda": purple chips with the dictionary terms used in this article. Tapping one opens
 * the term in the dictionary.
 */
package uz.asos.feature.constitution.article.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.constitution.R
import uz.asos.feature.constitution.article.TermChipUi

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun GlossaryTermChips(
    terms: List<TermChipUi>,
    onTermClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = stringResource(R.string.constitution_terms_title),
            style = AsosTextStyles.Subtitle.copy(fontWeight = FontWeight.ExtraBold),
            color = AsosColors.Text2,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            terms.forEach { term ->
                Box(
                    modifier = Modifier
                        .height(38.dp)
                        .border(1.dp, AsosColors.PurpleBorder, AsosShapes.Pill)
                        .background(AsosColors.PurpleBgSoft, AsosShapes.Pill)
                        .clip(AsosShapes.Pill)
                        .clickable(role = Role.Button) { onTermClick(term.slug) }
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = term.term,
                        style = AsosTextStyles.Chip.copy(fontSize = 13.5.sp),
                        color = AsosColors.PurpleText,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GlossaryTermChipsPreview() {
    AsosTheme {
        GlossaryTermChips(
            terms = listOf(
                TermChipUi("inklyuziv-talim", "Inklyuziv ta’lim"),
                TermChipUi("uzluksiz-talim", "Uzluksiz ta’lim"),
                TermChipUi("davlat-granti", "Davlat granti"),
            ),
            onTermClick = {},
            modifier = Modifier.padding(24.dp),
        )
    }
}
