/*
 * ArticleTopBar.kt
 * Module: :feature:constitution   ·   Work package: WP-05
 * Screen: Modda (50-modda) (design artboard: Modda.dc.html)
 *
 * Back · "II bo‘lim › IX bob" breadcrumb · Aa (text size) · save · share.
 * The save button turns light blue with a filled icon when the article is saved.
 */
package uz.asos.feature.constitution.article.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.constitution.R

@Composable
internal fun ArticleTopBar(
    sectionRoman: String?,
    chapterRoman: String?,
    isSaved: Boolean,
    onBack: () -> Unit,
    onTextSizeClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sectionLabel = sectionRoman?.let { stringResource(R.string.constitution_section_breadcrumb, it) }
    val chapterLabel = chapterRoman?.let { stringResource(R.string.constitution_chapter_breadcrumb, it) }
    val fallback = stringResource(R.string.constitution_preamble_breadcrumb)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AsosColors.Surface)
            .statusBarsPadding(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 6.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            BarButton(
                icon = AsosIcons.ChevronLeft,
                contentDescription = stringResource(R.string.constitution_back),
                onClick = onBack,
                iconSize = 22,
            )
            Text(
                text = buildAnnotatedString {
                    when {
                        sectionLabel != null && chapterLabel != null -> {
                            append("$sectionLabel › ")
                            withStyle(SpanStyle(color = AsosColors.Navy, fontWeight = FontWeight.Bold)) { append(chapterLabel) }
                        }
                        sectionLabel != null -> withStyle(SpanStyle(color = AsosColors.Navy, fontWeight = FontWeight.Bold)) {
                            append(sectionLabel)
                        }
                        else -> append(fallback)
                    }
                },
                style = AsosTextStyles.Subtitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            val textSizeLabel = stringResource(R.string.constitution_text_size)
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(AsosShapes.R14)
                    .clickable(role = Role.Button, onClick = onTextSizeClick)
                    .semantics { contentDescription = textSizeLabel },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Aa",
                    style = AsosTextStyles.SerifRoman.copy(fontSize = 17.sp),
                    color = AsosColors.Navy,
                )
            }
            BarButton(
                icon = if (isSaved) AsosIcons.BookmarkFilled else AsosIcons.Bookmark,
                contentDescription = stringResource(if (isSaved) R.string.constitution_saved else R.string.constitution_save),
                onClick = onBookmarkClick,
                tint = if (isSaved) AsosColors.Brand600 else AsosColors.Navy,
                container = if (isSaved) AsosColors.Brand50 else Color.Transparent,
            )
            BarButton(
                icon = AsosIcons.Share,
                contentDescription = stringResource(R.string.constitution_share),
                onClick = onShareClick,
            )
        }
        HorizontalDivider(thickness = 1.dp, color = AsosColors.Divider)
    }
}

@Composable
private fun BarButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    tint: Color = AsosColors.Navy,
    container: Color = Color.Transparent,
    iconSize: Int = 20,
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .background(container, AsosShapes.R14)
            .clip(AsosShapes.R14)
            .clickable(role = Role.Button, onClickLabel = contentDescription, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(iconSize.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ArticleTopBarPreview() {
    AsosTheme {
        ArticleTopBar(
            sectionRoman = "II",
            chapterRoman = "IX",
            isSaved = true,
            onBack = {},
            onTextSizeClick = {},
            onBookmarkClick = {},
            onShareClick = {},
        )
    }
}
