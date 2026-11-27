package uz.asos.feature.community.feed.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.model.community.VoteState
import uz.asos.feature.community.R

@Composable
internal fun VoteColumn(
    upvotes: Int,
    myVote: VoteState,
    onVote: (VoteState) -> Unit,
    modifier: Modifier = Modifier,
    horizontal: Boolean = false,
) {
    val up = @Composable {
        IconButton(onClick = { onVote(VoteState.UP) }, modifier = Modifier.size(28.dp)) {
            Icon(
                imageVector = AsosIcons.Upvote,
                contentDescription = stringResource(R.string.community_vote_up),
                tint = if (myVote == VoteState.UP) AsosColors.Brand600 else AsosColors.SoftGray,
                modifier = Modifier.size(18.dp),
            )
        }
    }
    val count = @Composable {
        Text(
            text = upvotes.toString(),
            style = AsosTextStyles.Tag,
            color = if (myVote == VoteState.NONE) AsosColors.Text2 else AsosColors.Brand600,
        )
    }
    val down = @Composable {
        IconButton(onClick = { onVote(VoteState.DOWN) }, modifier = Modifier.size(28.dp)) {
            Icon(
                imageVector = AsosIcons.Downvote,
                contentDescription = stringResource(R.string.community_vote_down),
                tint = if (myVote == VoteState.DOWN) AsosColors.Error else AsosColors.SoftGray,
                modifier = Modifier.size(18.dp),
            )
        }
    }

    if (horizontal) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            up()
            count()
            down()
        }
    } else {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            up()
            count()
            down()
        }
    }
}
