/*
 * LawyerExplanationCard.kt
 * Module: :feature:cases   ·   Work package: WP-08
 * Screen: Kazus — yechim (design artboard: Kazus.dc.html)
 *
 * Verified lawyer comment, "Moddani o'qish" chips for the correct articles, and the
 * numbered "AMALIY QADAMLAR" list.
 */
package uz.asos.feature.cases.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.ArticleNumberBadge
import uz.asos.core.designsystem.component.AsosCard
import uz.asos.core.designsystem.component.AsosCardStyle
import uz.asos.core.designsystem.component.VerifiedBadge
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.feature.cases.R
import uz.asos.feature.cases.data.model.ArticleOption
import uz.asos.feature.cases.data.model.LawyerComment

@Composable
internal fun LawyerExplanationCard(
    comment: LawyerComment,
    correctArticles: List<ArticleOption>,
    onReadArticleClick: (ArticleOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    AsosCard(
        modifier = modifier.fillMaxWidth(),
        style = AsosCardStyle.Outlined,
    ) {
        Column(
            modifier = Modifier.padding(AsosSpacing.ll),
            verticalArrangement = Arrangement.spacedBy(AsosSpacing.mm),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AsosSpacing.m),
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(AsosShapes.Circle)
                        .background(AsosColors.Brand50),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = comment.initials,
                        style = AsosTextStyles.RowTitleRegular,
                        color = AsosColors.Brand600,
                    )
                }
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.cases_lawyer_heading),
                            style = AsosTextStyles.RowTitle,
                        )
                        VerifiedBadge()
                    }
                    Text(
                        text = "${comment.fullName} · ${comment.role}",
                        style = AsosTextStyles.Meta,
                        color = AsosColors.Text2,
                    )
                }
            }

            comment.paragraphs.forEach { paragraph ->
                Text(
                    text = paragraph,
                    style = AsosTextStyles.Body,
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(AsosSpacing.s)) {
                correctArticles.forEach { article ->
                    Surface(
                        onClick = { onReadArticleClick(article) },
                        shape = AsosShapes.R10,
                        color = AsosColors.Brand50,
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                horizontal = AsosSpacing.m,
                                vertical = AsosSpacing.s,
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(AsosSpacing.xs),
                        ) {
                            ArticleNumberBadge(
                                text = article.number.toString(),
                                size = 22.dp,
                                cornerRadius = 6.dp,
                            )
                            Text(
                                text = stringResource(R.string.cases_lawyer_read_article),
                                style = AsosTextStyles.Link,
                            )
                        }
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = AsosShapes.R14,
                color = AsosColors.Background,
            ) {
                Column(
                    modifier = Modifier.padding(AsosSpacing.mm),
                    verticalArrangement = Arrangement.spacedBy(AsosSpacing.sm),
                ) {
                    Text(
                        text = stringResource(R.string.cases_lawyer_steps),
                        style = AsosTextStyles.Overline,
                        color = AsosColors.Text2,
                    )
                    comment.practicalSteps.forEachIndexed { index, step ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(AsosSpacing.sm),
                            verticalAlignment = Alignment.Top,
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(AsosShapes.Circle)
                                    .background(AsosColors.Navy),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = (index + 1).toString(),
                                    style = AsosTextStyles.Tag,
                                    color = AsosColors.Surface,
                                )
                            }
                            Text(
                                text = step,
                                style = AsosTextStyles.BodySmall,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }
        }
    }
}
