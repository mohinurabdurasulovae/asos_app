package uz.asos.feature.community.post.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.ArticleNumberBadge
import uz.asos.core.designsystem.component.AsosCard
import uz.asos.core.designsystem.component.AsosCardStyle
import uz.asos.core.designsystem.component.VerifiedBadge
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.model.community.Comment
import uz.asos.core.model.community.VoteState
import uz.asos.feature.community.R
import uz.asos.feature.community.feed.components.VoteColumn
import uz.asos.feature.community.ui.shortAge

@Composable
internal fun LawyerAnswerCard(
    comment: Comment,
    disclaimer: String?,
    onVote: (VoteState) -> Unit,
    onReply: () -> Unit,
    onArticleClick: (Int) -> Unit,
    onWriteToLawyer: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    AsosCard(
        modifier = modifier.fillMaxWidth(),
        shape = AsosShapes.R20,
        style = AsosCardStyle.Outlined,
        borderColor = AsosColors.SuccessBg,
    ) {
        Column(
            modifier = Modifier.padding(AsosSpacing.ll),
            verticalArrangement = Arrangement.spacedBy(AsosSpacing.m),
        ) {
            Surface(shape = AsosShapes.Pill, color = AsosColors.SuccessBg) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = AsosSpacing.sm,
                        vertical = AsosSpacing.xxs,
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AsosSpacing.xxs),
                ) {
                    Icon(
                        imageVector = AsosIcons.Check,
                        contentDescription = null,
                        tint = AsosColors.SuccessOnLight,
                        modifier = Modifier.size(14.dp),
                    )
                    Text(
                        text = stringResource(R.string.community_best_answer),
                        style = AsosTextStyles.Tag,
                        color = AsosColors.SuccessOnLight,
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AsosSpacing.sm),
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(AsosShapes.Circle)
                        .background(AsosColors.Brand50),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = comment.author.initials,
                        style = AsosTextStyles.RowTitleRegular,
                        color = AsosColors.Brand600,
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AsosSpacing.xxs),
                    ) {
                        Text(text = comment.author.name, style = AsosTextStyles.RowTitleRegular)
                        if (comment.author.isVerified) VerifiedBadge(size = 15.dp)
                    }
                    Text(
                        text = lawyerSubtitle(comment),
                        style = AsosTextStyles.Meta,
                        color = AsosColors.Text2,
                    )
                }
            }

            Text(text = comment.body, style = AsosTextStyles.Body)

            comment.article?.let { article ->
                Surface(
                    onClick = { onArticleClick(article.number) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = AsosShapes.R14,
                    color = AsosColors.Brand25,
                ) {
                    Row(
                        modifier = Modifier.padding(AsosSpacing.m),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AsosSpacing.sm),
                    ) {
                        ArticleNumberBadge(text = article.number.toString(), size = 34.dp)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(
                                    R.string.community_article_number,
                                    article.number,
                                ),
                                style = AsosTextStyles.Tag,
                                color = AsosColors.Brand600,
                            )
                            Text(
                                text = article.title,
                                style = AsosTextStyles.Meta,
                                color = AsosColors.Text2,
                            )
                        }
                        Icon(
                            imageVector = AsosIcons.ChevronRight,
                            contentDescription = null,
                            tint = AsosColors.Brand600,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
            }

            disclaimer?.let {
                Text(text = it, style = AsosTextStyles.Meta, color = AsosColors.Text2)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AsosSpacing.l),
            ) {
                VoteColumn(
                    upvotes = comment.upvotes,
                    myVote = VoteState.NONE,
                    onVote = onVote,
                    horizontal = true,
                )
                Text(
                    text = stringResource(R.string.community_reply),
                    style = AsosTextStyles.Link,
                    modifier = Modifier.clickable(onClick = onReply),
                )
                comment.author.lawyerId?.let { lawyerId ->
                    Text(
                        text = stringResource(R.string.community_write_to_lawyer),
                        style = AsosTextStyles.Link,
                        modifier = Modifier.clickable { onWriteToLawyer(lawyerId) },
                    )
                }
            }
        }
    }
}

@Composable
private fun lawyerSubtitle(comment: Comment): String {
    val parts = buildList {
        add(stringResource(R.string.community_verified_lawyer))
        comment.lawyer?.specialties?.firstOrNull()?.let { add(it) }
        add(comment.createdAt.shortAge())
    }
    return parts.joinToString(" · ")
}
