/*
 * PostCard.kt
 * Module: :feature:community   ·   Work package: WP-09
 * Screen: Hamjamiyat (design artboard: Hamjamiyat.dc.html)
 *
 * Vote column on the left; topic dot, author and age; title; excerpt; the lawyer's answer
 * preview when there is one; comment count and the status line.
 */
package uz.asos.feature.community.feed.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.AsosCard
import uz.asos.core.designsystem.component.AsosCardStyle
import uz.asos.core.designsystem.component.VerifiedBadge
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTones
import uz.asos.core.model.community.Post
import uz.asos.core.model.community.PostStatus
import uz.asos.core.model.community.VoteState
import uz.asos.feature.community.R
import uz.asos.feature.community.ui.shortAge

@Composable
internal fun PostCard(
    post: Post,
    onClick: () -> Unit,
    onVote: (VoteState) -> Unit,
    modifier: Modifier = Modifier,
) {
    AsosCard(
        modifier = modifier.fillMaxWidth(),
        shape = AsosShapes.R18,
        style = AsosCardStyle.Outlined,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AsosSpacing.mm),
            horizontalArrangement = Arrangement.spacedBy(AsosSpacing.m),
        ) {
            VoteColumn(
                upvotes = post.upvotes,
                myVote = post.myVote,
                onVote = onVote,
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(AsosSpacing.xs),
            ) {
                MetaLine(post = post)

                Text(
                    text = post.title,
                    style = AsosTextStyles.RowTitle,
                )

                Text(
                    text = post.excerpt,
                    style = AsosTextStyles.BodySmall,
                    color = AsosColors.Text2,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )

                post.bestAnswer?.let { answer ->
                    Surface(shape = AsosShapes.R14, color = AsosColors.SuccessBgSoft) {
                        Column(
                            modifier = Modifier.padding(AsosSpacing.m),
                            verticalArrangement = Arrangement.spacedBy(AsosSpacing.xxs),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(AsosSpacing.xxs),
                            ) {
                                if (answer.isLawyer) VerifiedBadge(size = 14.dp)
                                Text(
                                    text = stringResource(
                                        R.string.community_lawyer_line,
                                        answer.authorName,
                                    ),
                                    style = AsosTextStyles.Tag,
                                    color = AsosColors.SuccessOnLight,
                                )
                            }
                            Text(
                                text = answer.excerpt,
                                style = AsosTextStyles.Meta,
                                color = AsosColors.Text3,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }

                FooterLine(post = post)
            }
        }
    }
}

@Composable
private fun MetaLine(post: Post) {
    val tone = AsosTones.fromName(post.community?.tone)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AsosSpacing.xs),
    ) {
        post.community?.let { community ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(AsosShapes.Circle)
                    .background(tone.content),
            )
            Text(text = community.title, style = AsosTextStyles.Tag, color = tone.content)
        }
        Text(
            text = stringResource(
                R.string.community_author_age,
                post.author.name,
                post.createdAt.shortAge(),
            ),
            style = AsosTextStyles.Meta,
            color = AsosColors.Text2,
        )
    }
}

@Composable
private fun FooterLine(post: Post) {
    Row(
        modifier = Modifier.padding(top = AsosSpacing.xxs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AsosSpacing.m),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AsosSpacing.xxs),
        ) {
            Icon(
                imageVector = AsosIcons.Comment,
                contentDescription = null,
                tint = AsosColors.Text2,
                modifier = Modifier.size(15.dp),
            )
            Text(
                text = stringResource(R.string.community_comment_count, post.commentCount),
                style = AsosTextStyles.Meta,
                color = AsosColors.Text2,
            )
        }

        when (post.status) {
            PostStatus.ANSWERED -> if (post.bestAnswer == null) {
                Text(
                    text = stringResource(R.string.community_status_answered),
                    style = AsosTextStyles.Tag,
                    color = AsosColors.SuccessOnLight,
                )
            }
            PostStatus.AWAITING_LAWYER -> Text(
                text = stringResource(R.string.community_status_awaiting),
                style = AsosTextStyles.Tag,
                color = AsosColors.WarningOnLight,
            )
            PostStatus.OPEN -> Unit
        }
    }
}
