/*
 * OfficialTextCard.kt
 * Module: :feature:constitution   ·   Work package: WP-05
 * Screen: Modda (50-modda) (design artboard: Modda.dc.html)
 *
 * The official text: numbered parts in Source Serif 4, highlighted phrases on a light blue
 * background, "To‘liq matnni ochish" after the first three parts, and the source line.
 * The text is shown exactly as the API sends it (openapi.yaml: "Matn o‘zgartirilmagan").
 */
package uz.asos.feature.constitution.article.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.model.user.ReaderTextSize
import uz.asos.feature.constitution.R
import uz.asos.feature.constitution.article.PartUi

@Composable
internal fun OfficialTextCard(
    parts: List<PartUi>,
    showAll: Boolean,
    collapsedCount: Int,
    textSize: ReaderTextSize,
    sourceLabel: String?,
    onToggleShowAll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bodyStyle = legalBodyStyle(textSize)
    val visible = if (showAll) parts else parts.take(collapsedCount)
    val hasMore = parts.size > collapsedCount

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        if (parts.isEmpty()) {
            Text(
                text = stringResource(R.string.constitution_no_official_text),
                style = AsosTextStyles.Body,
                color = AsosColors.Text2,
            )
        }
        visible.forEachIndexed { index, part ->
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = part.number.toString(),
                    style = AsosTextStyles.LegalPartNumber,
                    modifier = Modifier
                        .width(18.dp)
                        .padding(top = 4.dp),
                )
                Text(
                    text = highlighted(part.body, part.highlights),
                    style = bodyStyle,
                    // The design fades the last visible part a little when the text is collapsed.
                    color = if (!showAll && hasMore && index == visible.lastIndex) AsosColors.Text3 else AsosColors.Navy,
                    modifier = Modifier.weight(1f),
                )
            }
        }
        if (hasMore) {
            Row(
                modifier = Modifier
                    .padding(start = 32.dp)
                    .height(40.dp)
                    .border(1.dp, AsosColors.Brand100, AsosShapes.R12)
                    .clip(AsosShapes.R12)
                    .clickable(role = Role.Button, onClick = onToggleShowAll)
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = stringResource(
                        if (showAll) R.string.constitution_hide_full_text else R.string.constitution_show_full_text,
                    ),
                    style = AsosTextStyles.Chip,
                    color = AsosColors.Brand600,
                )
                Icon(
                    imageVector = if (showAll) AsosIcons.ChevronUp else AsosIcons.ChevronDown,
                    contentDescription = null,
                    tint = AsosColors.Brand600,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
        Row(
            modifier = Modifier.padding(start = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(AsosIcons.Document, contentDescription = null, tint = AsosColors.Text2, modifier = Modifier.size(16.dp))
            Text(
                text = sourceLabel ?: stringResource(R.string.constitution_official_source),
                style = AsosTextStyles.Tag.copy(fontWeight = FontWeight.SemiBold),
                color = AsosColors.Text2,
            )
        }
    }
}

/** Serif body text, sized by the reader text-size setting (line height 1.6). */
internal fun legalBodyStyle(textSize: ReaderTextSize): TextStyle {
    val size = when (textSize) {
        ReaderTextSize.SMALL -> 17f
        ReaderTextSize.MEDIUM -> 19f
        ReaderTextSize.LARGE -> 21f
        ReaderTextSize.XLARGE -> 24f
    }
    return AsosTextStyles.LegalBody.copy(fontSize = size.sp, lineHeight = (size * 1.6f).sp)
}

/** Marks every occurrence of each phrase with the light blue highlight. */
@Composable
private fun highlighted(body: String, phrases: List<String>): AnnotatedString = remember(body, phrases) {
    val ranges = phrases
        .filter { it.isNotBlank() }
        .flatMap { phrase ->
            generateSequence(body.indexOf(phrase)) { previous ->
                body.indexOf(phrase, previous + phrase.length).takeIf { it >= 0 }
            }.takeWhile { it >= 0 }.map { it until it + phrase.length }.toList()
        }
        .sortedBy { it.first }
    buildAnnotatedString {
        append(body)
        ranges.forEach { range ->
            addStyle(
                SpanStyle(background = AsosColors.Brand100),
                start = range.first,
                end = range.last + 1,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OfficialTextCardPreview() {
    AsosTheme {
        OfficialTextCard(
            parts = listOf(
                PartUi(1, "Har kim ta’lim olish huquqiga ega.", emptyList()),
                PartUi(
                    2,
                    "Davlat uzluksiz ta’lim tizimi, uning har xil turlari va shakllari, davlat va nodavlat ta’lim tashkilotlari rivojlanishini ta’minlaydi.",
                    listOf("uzluksiz ta’lim tizimi"),
                ),
                PartUi(3, "Davlat maktabgacha ta’lim va tarbiyani rivojlantirish uchun shart-sharoitlar yaratadi.", emptyList()),
                PartUi(4, "…", emptyList()),
            ),
            showAll = false,
            collapsedCount = 3,
            textSize = ReaderTextSize.MEDIUM,
            sourceLabel = null,
            onToggleShowAll = {},
            modifier = Modifier.padding(24.dp),
        )
    }
}
