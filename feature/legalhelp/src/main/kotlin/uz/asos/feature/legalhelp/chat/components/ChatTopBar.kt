/*
 * ChatTopBar.kt
 * Module: :feature:legalhelp   ·   Work package: WP-10
 *
 * Back, lawyer avatar with online dot, name + verified badge, "Onlayn · advokat",
 * and call / video buttons. White bar with a bottom divider.
 */
package uz.asos.feature.legalhelp.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.component.VerifiedBadge
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.legalhelp.R
import uz.asos.feature.legalhelp.chat.ChatLawyerUi
import uz.asos.feature.legalhelp.ui.AvatarTone
import uz.asos.feature.legalhelp.ui.LawyerAvatar

@Composable
internal fun ChatTopBar(
    lawyer: ChatLawyerUi?,
    onBack: () -> Unit,
    onCall: () -> Unit,
    onVideoCall: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AsosColors.Surface)
            .statusBarsPadding(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AsosSpacing.s, vertical = AsosSpacing.s),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(AsosSpacing.minTouch)
                    .clip(AsosShapes.Circle)
                    .clickable(role = Role.Button, onClick = onBack),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    AsosIcons.ChevronLeft,
                    contentDescription = stringResource(R.string.legalhelp_back),
                    tint = AsosColors.Navy,
                    modifier = Modifier.size(22.dp),
                )
            }
            if (lawyer != null) {
                LawyerAvatar(
                    initials = lawyer.initials,
                    tone = lawyer.avatarTone,
                    isOnline = lawyer.isOnline,
                    size = 40.dp,
                    fontSize = 14.sp,
                )
                Column(
                    Modifier
                        .weight(1f)
                        .padding(horizontal = AsosSpacing.m),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = lawyer.name,
                            style = AsosTextStyles.RowTitle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false),
                        )
                        if (lawyer.isVerified) {
                            VerifiedBadge(modifier = Modifier.padding(start = AsosSpacing.xxs), size = 14.dp)
                        }
                    }
                    Text(
                        text = stringResource(
                            if (lawyer.isOnline) R.string.legalhelp_chat_status_online else R.string.legalhelp_chat_status_offline,
                        ),
                        style = AsosTextStyles.Tag,
                        color = if (lawyer.isOnline) AsosColors.SuccessOnLight else AsosColors.Text2,
                    )
                }
                TopBarIconButton(AsosIcons.Phone, stringResource(R.string.legalhelp_call), onCall)
                Spacer(Modifier.width(AsosSpacing.s))
                TopBarIconButton(AsosIcons.Video, stringResource(R.string.legalhelp_video_call), onVideoCall)
                Spacer(Modifier.width(AsosSpacing.xxs))
            } else {
                Spacer(Modifier.weight(1f))
            }
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(AsosColors.Divider),
        )
    }
}

@Composable
private fun TopBarIconButton(icon: ImageVector, contentDescription: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(AsosSpacing.minTouch)
            .clip(AsosShapes.R14)
            .background(AsosColors.Brand50)
            .clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = contentDescription, tint = AsosColors.Brand600, modifier = Modifier.size(20.dp))
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun ChatTopBarPreview() {
    AsosTheme {
        ChatTopBar(
            lawyer = ChatLawyerUi(
                name = "Sardor Rahimov",
                initials = "SR",
                isOnline = true,
                isVerified = true,
                avatarTone = AvatarTone.NAVY,
            ),
            onBack = {},
            onCall = {},
            onVideoCall = {},
        )
    }
}
