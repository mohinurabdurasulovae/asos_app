package uz.asos.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosElevation
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.designsystem.theme.softShadow
import uz.asos.feature.home.R

@Composable
internal fun HomeTopBar(
    greeting: String,
    userName: String?,
    initials: String?,
    hasUnreadNotifications: Boolean,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Avatar(initials = initials, onClick = onProfileClick)
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            if (greeting.isNotBlank()) {
                Text(
                    text = stringResource(R.string.home_greeting_format, greeting),
                    style = AsosTextStyles.Meta.copy(fontSize = 13.sp),
                )
            }
            Text(
                text = userName ?: stringResource(R.string.home_guest_name),
                style = AsosTextStyles.CardTitle,
                maxLines = 1,
            )
        }
        NotificationsButton(hasUnread = hasUnreadNotifications, onClick = onNotificationsClick)
    }
}

@Composable
private fun Avatar(initials: String?, onClick: () -> Unit) {
    val label = stringResource(R.string.home_profile)
    Box(
        modifier = Modifier
            .size(46.dp)
            .softShadow(AsosShapes.Circle, AsosElevation.card)
            .background(AsosColors.Brand100, AsosShapes.Circle)
            .border(2.dp, AsosColors.Surface, AsosShapes.Circle)
            .clip(AsosShapes.Circle)
            .clickable(role = Role.Button, onClick = onClick)
            .semantics { contentDescription = label },
        contentAlignment = Alignment.Center,
    ) {
        if (initials != null) {
            Text(
                text = initials,
                style = AsosTextStyles.RowTitle.copy(fontWeight = FontWeight.ExtraBold),
                color = AsosColors.Brand600,
            )
        } else {
            Icon(AsosIcons.Person, contentDescription = null, tint = AsosColors.Brand600, modifier = Modifier.size(22.dp))
        }
    }
}

@Composable
private fun NotificationsButton(hasUnread: Boolean, onClick: () -> Unit) {
    val label = stringResource(if (hasUnread) R.string.home_notifications_unread else R.string.home_notifications)
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(AsosShapes.R16)
            .background(AsosColors.Surface)
            .border(1.dp, AsosColors.Border, AsosShapes.R16)
            .clickable(role = Role.Button, onClick = onClick)
            .semantics { contentDescription = label },
        contentAlignment = Alignment.Center,
    ) {
        Icon(AsosIcons.Bell, contentDescription = null, tint = AsosColors.Navy, modifier = Modifier.size(22.dp))
        if (hasUnread) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 11.dp, end = 12.dp)
                    .size(8.dp)
                    .background(AsosColors.Error, AsosShapes.Circle)
                    .border(2.dp, AsosColors.Surface, AsosShapes.Circle),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC)
@Composable
private fun HomeTopBarPreview() {
    AsosTheme {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            HomeTopBar("Xayrli kech", "Aziza", "AK", hasUnreadNotifications = true, onProfileClick = {}, onNotificationsClick = {})
            HomeTopBar("Xayrli tong", null, null, hasUnreadNotifications = false, onProfileClick = {}, onNotificationsClick = {})
        }
    }
}
