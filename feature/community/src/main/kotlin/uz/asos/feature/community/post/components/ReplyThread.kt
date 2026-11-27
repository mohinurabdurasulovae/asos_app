/*
 * ReplyThread.kt
 * Module: :feature:community   ·   Work package: WP-09
 * Screen: Muhokama (design artboard: Post.dc.html)
 *
 * A comment and its one level of replies, the nested level indented behind a rule.
 */
package uz.asos.feature.community.post.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
internal fun ReplyThread(
    comment: Comment,
    isPostAuthor: Boolean,
    onVote: (commentId: Int, vote: VoteState) -> Unit,
    onReply: (commentId: Int, authorName: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AsosSpacing.sm),
    ) {
        CommentRow(
            comment = comment,
            isPostAuthor = isPostAuthor,
            onVote = { vote -> onVote(comment.id, vote) },
            onReply = { onReply(comment.id, comment.author.name) },
        )

        if (comment.replies.isNotEmpty()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .padding(start = AsosSpacing.l, end = AsosSpacing.m)
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(AsosColors.Divider),
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(AsosSpacing.sm),
                ) {
                    comment.replies.forEach { reply ->
                        CommentRow(
                            comment = reply,
                            isPostAuthor = false,
                            onVote = { vote -> onVote(reply.id, vote) },
                            onReply = { onReply(reply.id, reply.author.name) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CommentRow(
    comment: Comment,
    isPostAuthor: Boolean,
    onVote: (VoteState) -> Unit,
    onReply: () -> Unit,
) {
    if (comment.isDeleted) {
        Text(
            text = stringResource(R.string.community_deleted_comment),
            style = AsosTextStyles.Meta,
            color = AsosColors.SoftGray,
        )
        return
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AsosSpacing.sm),
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(AsosShapes.Circle)
                .background(
                    if (comment.isLawyer) AsosColors.Brand50 else AsosColors.SurfaceMuted,
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (comment.author.isAnonymous) {
                Icon(
                    imageVector = AsosIcons.EyeOff,
                    contentDescription = null,
                    tint = AsosColors.Text2,
                    modifier = Modifier.size(16.dp),
                )
            } else {
                Text(
                    text = comment.author.initials,
                    style = AsosTextStyles.Tag,
                    color = if (comment.isLawyer) AsosColors.Brand600 else AsosColors.Navy,
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(AsosSpacing.xxs),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AsosSpacing.xxs),
            ) {
                Text(text = comment.author.name, style = AsosTextStyles.Tag)
                if (comment.author.isVerified) VerifiedBadge(size = 13.dp)
                if (isPostAuthor) {
                    Surface(shape = AsosShapes.Pill, color = AsosColors.SurfaceMuted) {
                        Text(
                            text = stringResource(R.string.community_author_badge),
                            style = AsosTextStyles.Tag,
                            color = AsosColors.Text2,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp),
                        )
                    }
                }
                Text(
                    text = "· ${comment.createdAt.shortAge()}",
                    style = AsosTextStyles.Meta,
                    color = AsosColors.Text2,
                )
            }

            Text(text = comment.body, style = AsosTextStyles.BodySmall)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AsosSpacing.m),
            ) {
                VoteColumn(
                    upvotes = comment.upvotes,
                    myVote = VoteState.NONE,
                    onVote = onVote,
                    horizontal = true,
                )
                Text(
                    text = stringResource(R.string.community_reply),
                    style = AsosTextStyles.Tag,
                    color = AsosColors.Text2,
                    modifier = Modifier
                        .height(28.dp)
                        .padding(top = AsosSpacing.xs)
                        .clickable(onClick = onReply),
                )
            }
        }
    }
}
