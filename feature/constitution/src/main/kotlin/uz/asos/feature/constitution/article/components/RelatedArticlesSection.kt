package uz.asos.feature.constitution.article.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.constitution.R
import uz.asos.feature.constitution.article.RelatedUi

private const val COLUMNS = 3

@Composable
internal fun RelatedArticlesSection(
    related: List<RelatedUi>,
    onArticleClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(
            text = stringResource(R.string.constitution_related_title),
            style = AsosTextStyles.SectionTitle,
            modifier = Modifier.semantics { heading() },
        )
        related.chunked(COLUMNS).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { item ->
                    RelatedTile(
                        item = item,
                        onClick = { onArticleClick(item.number) },
                        modifier = Modifier.weight(1f),
                    )
                }
                repeat(COLUMNS - row.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
private fun RelatedTile(
    item: RelatedUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(AsosColors.Background, AsosShapes.R18)
            .clip(AsosShapes.R18)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = item.number.toString(),
            style = AsosTextStyles.SerifNumber.copy(fontSize = 26.sp, lineHeight = 26.sp),
        )
        Text(
            text = item.title,
            style = AsosTextStyles.Meta.copy(fontWeight = FontWeight.Bold, lineHeight = 16.sp),
            color = AsosColors.Navy,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RelatedArticlesSectionPreview() {
    AsosTheme {
        RelatedArticlesSection(
            related = listOf(
                RelatedUi(19, "Qonun oldida tenglik"),
                RelatedUi(42, "Mehnat va kasb tanlash"),
                RelatedUi(55, "Huquqlarni himoya qilish"),
            ),
            onArticleClick = {},
            modifier = Modifier.padding(24.dp),
        )
    }
}
