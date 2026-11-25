/*
 * ArticleList.kt
 * Module: :feature:constitution   ·   Work package: WP-05
 * Screen: Konstitutsiya (design artboard: Konstitutsiya.dc.html)
 *
 * Article rows used in search results, a topic's articles, Saqlangan, and inside an opened
 * chapter. Same visual language as the chapter rows: serif number tile, title, meta, status.
 */
package uz.asos.feature.constitution.browse.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.component.ArticleNumberBadge
import uz.asos.core.designsystem.component.ArticleNumberBadgeVariant
import uz.asos.core.designsystem.component.AsosButton
import uz.asos.core.designsystem.component.AsosButtonVariant
import uz.asos.core.designsystem.component.AsosCard
import uz.asos.core.designsystem.component.AsosCardStyle
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.ui.state.EmptyState
import uz.asos.core.ui.state.InlineLoading
import uz.asos.feature.constitution.R
import uz.asos.feature.constitution.browse.ArticleListState
import uz.asos.feature.constitution.browse.ArticleRowUi
import uz.asos.feature.constitution.browse.ReadState

/** One article row. [compact] = inside an opened chapter (smaller tile, more indent). */
@Composable
internal fun ArticleListItem(
    article: ArticleRowUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    startPadding: Dp = 16.dp,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick)
            .padding(start = startPadding, end = 16.dp, top = 12.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ArticleNumberBadge(
            text = article.numberLabel,
            variant = ArticleNumberBadgeVariant.Light,
            size = if (compact) 34.dp else 44.dp,
            cornerRadius = if (compact) 10.dp else 13.dp,
            fontSize = if (compact) 14.sp else 17.sp,
        )
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = article.title,
                style = AsosTextStyles.RowTitleRegular.copy(
                    fontSize = if (compact) 14.sp else 14.5.sp,
                    fontWeight = if (compact) FontWeight.SemiBold else FontWeight.Bold,
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(text = article.meta, style = AsosTextStyles.Meta)
        }
        if (article.state != ReadState.NOT_STARTED) {
            ArticleStatusIcon(state = article.state, size = if (compact) 18.dp else 22.dp)
        }
    }
}

/**
 * A whole list in a card: loading spinner, rows with dividers, empty message or error.
 */
@Composable
internal fun ArticleListSection(
    state: ArticleListState,
    emptyTitle: String,
    onArticleClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    emptyMessage: String? = null,
    onSignIn: (() -> Unit)? = null,
) {
    when (state) {
        ArticleListState.Hidden -> Unit
        ArticleListState.Loading -> InlineLoading(modifier.padding(vertical = 24.dp))
        is ArticleListState.Failed -> EmptyState(
            title = state.message,
            icon = AsosIcons.Close,
            actionLabel = stringResource(R.string.constitution_retry),
            onAction = onRetry,
            modifier = modifier,
        )
        ArticleListState.NeedsSignIn -> EmptyState(
            title = stringResource(R.string.constitution_saved_signin_title),
            message = stringResource(R.string.constitution_saved_signin_message),
            icon = AsosIcons.Bookmark,
            actionLabel = if (onSignIn != null) stringResource(R.string.constitution_signin_action) else null,
            onAction = onSignIn,
            modifier = modifier,
        )
        is ArticleListState.Loaded -> if (state.items.isEmpty()) {
            EmptyState(title = emptyTitle, message = emptyMessage, modifier = modifier)
        } else {
            AsosCard(
                modifier = modifier.fillMaxWidth(),
                shape = AsosShapes.R20,
                style = AsosCardStyle.Outlined,
            ) {
                state.items.forEachIndexed { index, article ->
                    if (index > 0) HorizontalDivider(thickness = 1.dp, color = AsosColors.Divider)
                    ArticleListItem(article = article, onClick = { onArticleClick(article.key) })
                }
            }
        }
    }
}

/** Articles shown under an opened chapter, inside the section card. */
@Composable
internal fun ChapterArticles(
    state: ArticleListState?,
    onArticleClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        when (state) {
            null, ArticleListState.Hidden, ArticleListState.NeedsSignIn -> Unit
            ArticleListState.Loading -> InlineLoading()
            is ArticleListState.Failed -> Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(text = state.message, style = AsosTextStyles.BodySmall, color = AsosColors.Text2)
                AsosButton(
                    text = stringResource(R.string.constitution_retry),
                    onClick = onRetry,
                    variant = AsosButtonVariant.Secondary,
                    height = 44.dp,
                )
            }
            is ArticleListState.Loaded -> if (state.items.isEmpty()) {
                Text(
                    text = stringResource(R.string.constitution_chapter_articles_empty),
                    style = AsosTextStyles.BodySmall,
                    color = AsosColors.Text2,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                )
            } else {
                state.items.forEach { article ->
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = AsosColors.Divider,
                        modifier = Modifier.padding(start = 70.dp),
                    )
                    ArticleListItem(
                        article = article,
                        onClick = { onArticleClick(article.key) },
                        compact = true,
                        startPadding = 70.dp,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC)
@Composable
private fun ArticleListSectionPreview() {
    AsosTheme {
        ArticleListSection(
            state = ArticleListState.Loaded(
                listOf(
                    ArticleRowUi("50", "50", "Ta’lim olish huquqi", "IX bob · 4 daqiqa", ReadState.IN_PROGRESS),
                    ArticleRowUi("47", "47", "Har kim uy-joyli bo‘lish huquqiga ega.", "IX bob · 2 daqiqa", ReadState.READ),
                    ArticleRowUi("48", "48", "Har kim sog‘lig‘ini saqlash va malakali tibbiy xizmatdan foydalanish huquqiga ega.", "IX bob · 2 daqiqa", ReadState.NOT_STARTED),
                ),
            ),
            emptyTitle = "",
            onArticleClick = {},
            onRetry = {},
            modifier = Modifier.padding(20.dp),
        )
    }
}
