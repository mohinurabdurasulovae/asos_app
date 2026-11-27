/*
 * PostActionBar.kt
 * Module: :feature:community   ·   Work package: WP-09
 * Screen: Muhokama (design artboard: Post.dc.html)
 *
 * Vote control, comment count, save and share under the question.
 */
package uz.asos.feature.community.post.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.model.community.VoteState
import uz.asos.feature.community.R
import uz.asos.feature.community.feed.components.VoteColumn

@Composable
internal fun PostActionBar(
    upvotes: Int,
    myVote: VoteState,
    commentCount: Int,
    isSaved: Boolean,
    onVote: (VoteState) -> Unit,
    onSaveToggle: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AsosSpacing.l),
    ) {
        VoteColumn(
            upvotes = upvotes,
            myVote = myVote,
            onVote = onVote,
            horizontal = true,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AsosSpacing.xxs),
        ) {
            Icon(
                imageVector = AsosIcons.Comment,
                contentDescription = null,
                tint = AsosColors.Text2,
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = commentCount.toString(),
                style = AsosTextStyles.Tag,
                color = AsosColors.Text2,
            )
        }

        IconButton(onClick = onSaveToggle, modifier = Modifier.size(32.dp)) {
            Icon(
                imageVector = if (isSaved) AsosIcons.BookmarkFilled else AsosIcons.Bookmark,
                contentDescription = stringResource(
                    if (isSaved) R.string.community_post_saved else R.string.community_post_save,
                ),
                tint = if (isSaved) AsosColors.Brand600 else AsosColors.Text2,
                modifier = Modifier.size(18.dp),
            )
        }

        IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) {
            Icon(
                imageVector = AsosIcons.Share,
                contentDescription = stringResource(R.string.community_post_share),
                tint = AsosColors.Text2,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}
