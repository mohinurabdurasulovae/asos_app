/*
 * TermOfTheDayCard.kt
 * Module: :feature:dictionary   ·   Work package: WP-06
 * Screen: Konstitutsiyaviy lug‘at (design artboard: Lugat.dc.html)
 *
 * Full term: serif title, part of speech · etymology, definition, "Hayotda" box and the
 * article pills, with listen (TTS) and save buttons. Used for "Kun atamasi" (with the tag)
 * and, without the tag and card chrome, inside the opened-term sheet.
 */
package uz.asos.feature.dictionary.components

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.component.AsosCard
import uz.asos.core.designsystem.component.AsosTag
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.designsystem.theme.AsosTones
import uz.asos.feature.dictionary.ArticleChipUi
import uz.asos.feature.dictionary.R
import uz.asos.feature.dictionary.TermCardUi

/** "Kun atamasi" card on the list screen. */
@Composable
internal fun TermOfTheDayCard(
    term: TermCardUi,
    onListen: () -> Unit,
    onBookmark: () -> Unit,
    onArticleClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    AsosCard(modifier = modifier.fillMaxWidth(), shape = AsosShapes.R24) {
        Column(modifier = Modifier.padding(20.dp)) {
            TermDetails(
                term = term,
                overline = stringResource(R.string.dictionary_term_of_the_day).uppercase(),
                onListen = onListen,
                onBookmark = onBookmark,
                onArticleClick = onArticleClick,
            )
        }
    }
}

/** Term body without card chrome. [overline] = the purple tag above the title (null hides it). */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ColumnScope.TermDetails(
    term: TermCardUi,
    overline: String?,
    onListen: () -> Unit,
    onBookmark: () -> Unit,
    onArticleClick: (Int) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (overline != null) AsosTag(text = overline, tone = AsosTones.Purple)
        Spacer(Modifier.weight(1f))
        IconAction(
            icon = AsosIcons.VolumeUp,
            label = stringResource(R.string.dictionary_listen),
            onClick = onListen,
        )
        IconAction(
            icon = if (term.isSaved) AsosIcons.BookmarkFilled else AsosIcons.Bookmark,
            label = stringResource(if (term.isSaved) R.string.dictionary_unsave else R.string.dictionary_save),
            onClick = onBookmark,
            tint = if (term.isSaved) AsosColors.Surface else AsosColors.Navy,
            container = if (term.isSaved) AsosColors.Brand600 else Color.Transparent,
        )
    }

    Spacer(Modifier.height(10.dp))
    Text(
        text = term.term,
        style = AsosTextStyles.SerifNumber.copy(fontSize = 34.sp, lineHeight = 40.sp),
        color = AsosColors.Navy,
        modifier = Modifier.semantics { heading() },
    )
    term.meta?.let {
        Spacer(Modifier.height(4.dp))
        Text(text = it, style = AsosTextStyles.Meta)
    }
    Spacer(Modifier.height(14.dp))
    Text(text = term.definition, style = AsosTextStyles.Body, color = AsosColors.Text3)

    term.inLife?.let { inLife ->
        Spacer(Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AsosColors.Background, AsosShapes.R16)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = stringResource(R.string.dictionary_in_life).uppercase(),
                style = AsosTextStyles.Overline,
                color = AsosColors.Text2,
            )
            Text(text = inLife, style = AsosTextStyles.SerifQuote, color = AsosColors.Navy)
        }
    }

    if (term.articles.isNotEmpty()) {
        Spacer(Modifier.height(16.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.dictionary_articles),
                style = AsosTextStyles.Chip,
                color = AsosColors.Navy,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 2.dp),
            )
            term.articles.forEach { article ->
                ArticlePill(article = article, onClick = { onArticleClick(article.number) })
            }
        }
    }
}

@Composable
private fun ArticlePill(article: ArticleChipUi, onClick: () -> Unit) {
    val label = stringResource(R.string.dictionary_open_article, article.label)
    // 44 dp touch target around a 32 dp pill.
    Box(
        modifier = Modifier
            .sizeIn(minHeight = 44.dp)
            .clip(AsosShapes.R10)
            .clickable(role = Role.Button, onClickLabel = label, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .height(32.dp)
                .background(AsosColors.Navy, AsosShapes.R10)
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = article.label,
                style = AsosTextStyles.Tag.copy(fontSize = 13.sp, fontWeight = FontWeight.ExtraBold),
                color = AsosColors.Surface,
            )
        }
    }
}

@Composable
private fun IconAction(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    tint: Color = AsosColors.Navy,
    container: Color = Color.Transparent,
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(AsosShapes.R14)
            .background(container, AsosShapes.R14)
            .clickable(role = Role.Button, onClickLabel = label, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = tint, modifier = Modifier.size(20.dp))
    }
}

internal val previewTerm = TermCardUi(
    id = 1,
    slug = "daxlsizlik",
    term = "Daxlsizlik",
    meta = "ot · arabcha “daxl” — tegish, aralashish",
    definition = "Shaxs, uy-joy yoki yozishmalarga ruxsatsiz tegib, aralashib bo‘lmasligi — ularning qonun bilan himoyalanganligi.",
    inLife = "Qonunda belgilangan holatlardan tashqari, telefoningizdagi xabarlarni sizning roziligingizsiz hech kim o‘qiy olmaydi.",
    articles = listOf(ArticleChipUi(27, "27-modda"), ArticleChipUi(31, "31-modda")),
    isSaved = true,
)

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC, widthDp = 360)
@Composable
private fun TermOfTheDayCardPreview() {
    AsosTheme {
        TermOfTheDayCard(
            term = previewTerm,
            onListen = {},
            onBookmark = {},
            onArticleClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
