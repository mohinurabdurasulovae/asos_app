/*
 * TermListItem.kt
 * Module: :feature:dictionary   ·   Work package: WP-06
 * Screen: Konstitutsiyaviy lug‘at (design artboard: Lugat.dc.html)
 *
 * One term card in the A–Z list: term, short definition, related articles, save toggle.
 * Plus the serif letter header ("D") above each group.
 */
package uz.asos.feature.dictionary.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.component.AsosCard
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.dictionary.R
import uz.asos.feature.dictionary.TermRowUi

@Composable
internal fun TermListItem(
    term: TermRowUi,
    onClick: () -> Unit,
    onBookmark: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AsosCard(
        modifier = modifier.fillMaxWidth(),
        shape = AsosShapes.R18,
        onClick = onClick,
        onClickLabel = term.term,
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 4.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 2.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(text = term.term, style = AsosTextStyles.RowTitle, color = AsosColors.Navy)
                if (term.shortDefinition.isNotBlank()) {
                    Text(
                        text = term.shortDefinition,
                        style = AsosTextStyles.BodySmall.copy(fontSize = 13.5.sp, lineHeight = 19.sp),
                        color = AsosColors.Text2,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                term.articlesLabel?.let {
                    Text(text = it, style = AsosTextStyles.Link.copy(fontSize = 12.5.sp))
                }
            }
            val label = stringResource(if (term.isSaved) R.string.dictionary_unsave else R.string.dictionary_save)
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(AsosShapes.R14)
                    .clickable(role = Role.Button, onClickLabel = label, onClick = onBookmark),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = if (term.isSaved) AsosIcons.BookmarkFilled else AsosIcons.Bookmark,
                    contentDescription = label,
                    tint = if (term.isSaved) AsosColors.Brand600 else AsosColors.SoftGray,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

/** Serif brand-blue letter above a group, as in the design. */
@Composable
internal fun LetterHeader(letter: String, modifier: Modifier = Modifier) {
    Text(
        text = letter,
        style = AsosTextStyles.SerifNumber,
        color = AsosColors.Brand600,
        modifier = modifier
            .padding(top = 8.dp, bottom = 2.dp)
            .semantics { heading() },
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC, widthDp = 360)
@Composable
private fun TermListItemPreview() {
    AsosTheme {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            LetterHeader("D")
            TermListItem(
                term = TermRowUi(1, "daxlsizlik", "Daxlsizlik", "Ruxsatsiz tegib, aralashib bo‘lmaslik.", "27 · 31-moddalar", isSaved = true),
                onClick = {},
                onBookmark = {},
            )
            TermListItem(
                term = TermRowUi(4, "demokratiya", "Demokratiya", "Hokimiyat manbai xalq bo‘lgan boshqaruv tartibi.", null, isSaved = false),
                onClick = {},
                onBookmark = {},
            )
        }
    }
}
