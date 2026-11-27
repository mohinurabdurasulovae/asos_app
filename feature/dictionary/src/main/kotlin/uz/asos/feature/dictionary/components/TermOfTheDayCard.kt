package uz.asos.feature.dictionary.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.component.AsosCard
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.model.dictionary.TermDetail
import uz.asos.feature.dictionary.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun TermOfTheDayCard(
    term: TermDetail,
    onSaveToggle: () -> Unit,
    onArticleClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isTermOfTheDay: Boolean = true,
) {
    AsosCard(
        modifier = modifier.fillMaxWidth(),
        shape = AsosShapes.R26,
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(AsosSpacing.l),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                if (isTermOfTheDay) {
                    Surface(shape = AsosShapes.Pill, color = AsosColors.PurpleBg) {
                        Text(
                            text = stringResource(R.string.dictionary_term_of_the_day),
                            style = AsosTextStyles.Overline,
                            color = AsosColors.Purple,
                            modifier = Modifier.padding(
                                horizontal = AsosSpacing.sm,
                                vertical = AsosSpacing.xs,
                            ),
                        )
                    }
                } else {
                    Box(modifier = Modifier.size(0.dp))
                }

                Surface(
                    onClick = onSaveToggle,
                    shape = AsosShapes.R12,
                    color = if (term.isSaved) AsosColors.Brand50 else AsosColors.Surface,
                    border = if (term.isSaved) {
                        null
                    } else {
                        androidx.compose.foundation.BorderStroke(1.dp, AsosColors.Border)
                    },
                ) {
                    Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (term.isSaved) AsosIcons.BookmarkFilled else AsosIcons.Bookmark,
                            contentDescription = stringResource(
                                if (term.isSaved) R.string.dictionary_saved else R.string.dictionary_save,
                            ),
                            tint = if (term.isSaved) AsosColors.Brand600 else AsosColors.Navy,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(AsosSpacing.xxs)) {
                Text(
                    text = term.term,
                    style = AsosTextStyles.ArticleNumberDisplay.copy(fontSize = 34.sp, lineHeight = 40.sp),
                )
                term.grammarLine()?.let { line ->
                    Text(text = line, style = AsosTextStyles.Subtitle, color = AsosColors.Text2)
                }
            }

            Text(
                text = term.definition,
                style = AsosTextStyles.Body.copy(fontSize = 16.sp, lineHeight = 24.8.sp),
            )

            term.inLife?.let { inLife ->
                Surface(shape = AsosShapes.R16, color = AsosColors.Background) {
                    Column(
                        modifier = Modifier.padding(horizontal = AsosSpacing.l, vertical = AsosSpacing.mm),
                        verticalArrangement = Arrangement.spacedBy(AsosSpacing.xs),
                    ) {
                        Text(
                            text = stringResource(R.string.dictionary_in_life),
                            style = AsosTextStyles.Overline,
                            color = AsosColors.Text2,
                        )
                        Text(
                            text = inLife,
                            style = AsosTextStyles.SerifQuote,
                            color = AsosColors.Text3,
                        )
                    }
                }
            }

            if (term.articles.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(AsosSpacing.s),
                    verticalArrangement = Arrangement.spacedBy(AsosSpacing.s),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(R.string.dictionary_articles_label),
                        style = AsosTextStyles.Subtitle,
                        color = AsosColors.Text2,
                        modifier = Modifier.padding(top = AsosSpacing.s),
                    )
                    term.articles.forEach { article ->
                        ArticleChip(
                            number = article.number,
                            onClick = { onArticleClick(article.number) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ArticleChip(number: Int, onClick: () -> Unit) {
    Surface(onClick = onClick, shape = AsosShapes.R10, color = AsosColors.Navy) {
        Text(
            text = stringResource(R.string.dictionary_article_number, number),
            style = AsosTextStyles.SerifRoman,
            color = AsosColors.Surface,
            modifier = Modifier.padding(horizontal = AsosSpacing.m, vertical = AsosSpacing.s),
        )
    }
}

internal fun TermDetail.grammarLine(): String? =
    listOfNotNull(partOfSpeech, etymology)
        .filter { it.isNotBlank() }
        .takeIf { it.isNotEmpty() }
        ?.joinToString(" · ")
