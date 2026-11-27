package uz.asos.feature.community.post.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.feature.community.R
import uz.asos.feature.community.post.ReplyDraft

@Composable
internal fun ReplyComposer(
    draft: ReplyDraft,
    onBodyChange: (String) -> Unit,
    onAnonymousChange: (Boolean) -> Unit,
    onSend: () -> Unit,
    onCancelReply: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = AsosColors.Surface,
        shadowElevation = 8.dp,
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(horizontal = AsosSpacing.l, vertical = AsosSpacing.m),
            verticalArrangement = Arrangement.spacedBy(AsosSpacing.s),
        ) {
            draft.parentAuthor?.let { author ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.community_replying_to, author),
                        style = AsosTextStyles.Meta,
                        color = AsosColors.Brand600,
                    )
                    IconButton(onClick = onCancelReply, modifier = Modifier.size(24.dp)) {
                        Icon(
                            imageVector = AsosIcons.Close,
                            contentDescription = stringResource(R.string.community_cancel_reply),
                            tint = AsosColors.Text2,
                            modifier = Modifier.size(14.dp),
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AsosSpacing.s),
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 44.dp)
                        .background(AsosColors.Background, AsosShapes.Pill)
                        .border(BorderStroke(1.dp, AsosColors.Border), AsosShapes.Pill)
                        .padding(horizontal = AsosSpacing.l, vertical = AsosSpacing.m),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (draft.body.isEmpty()) {
                        Text(
                            text = stringResource(R.string.community_reply_hint),
                            style = AsosTextStyles.BodySmall,
                            color = AsosColors.Text2,
                        )
                    }
                    BasicTextField(
                        value = draft.body,
                        onValueChange = onBodyChange,
                        enabled = !draft.isSending,
                        textStyle = LocalTextStyle.current.merge(AsosTextStyles.BodySmall)
                            .copy(color = AsosColors.Navy),
                        cursorBrush = SolidColor(AsosColors.Brand600),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                Surface(
                    onClick = onSend,
                    enabled = draft.canSend,
                    shape = AsosShapes.Circle,
                    color = if (draft.canSend) AsosColors.Brand600 else AsosColors.Outline,
                ) {
                    Box(modifier = Modifier.size(44.dp), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = AsosIcons.Send,
                            contentDescription = stringResource(R.string.community_reply_send),
                            tint = AsosColors.Surface,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AsosSpacing.s),
            ) {
                Switch(
                    checked = draft.isAnonymous,
                    onCheckedChange = onAnonymousChange,
                    enabled = !draft.isSending,
                    colors = SwitchDefaults.colors(checkedTrackColor = AsosColors.Brand600),
                )
                Text(
                    text = stringResource(R.string.community_reply_anonymous),
                    style = AsosTextStyles.Meta,
                    color = AsosColors.Text2,
                )
            }
        }
    }
}
