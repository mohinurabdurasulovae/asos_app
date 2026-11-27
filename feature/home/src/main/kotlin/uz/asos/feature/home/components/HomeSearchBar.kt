package uz.asos.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.designsystem.theme.softShadow
import uz.asos.feature.home.R

@Composable
internal fun HomeSearchBar(
    onSearchClick: () -> Unit,
    onVoiceClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val openLabel = stringResource(R.string.home_search_open)
    val voiceLabel = stringResource(R.string.home_voice_search)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .softShadow(AsosShapes.R18)
            .background(AsosColors.Surface, AsosShapes.R18)
            .clip(AsosShapes.R18)
            .clickable(role = Role.Button, onClickLabel = openLabel, onClick = onSearchClick)
            .padding(start = 16.dp, end = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Icon(AsosIcons.Search, contentDescription = null, tint = AsosColors.Text2, modifier = Modifier.size(20.dp))
        Text(
            text = stringResource(R.string.home_search_placeholder),
            style = AsosTextStyles.Body,
            color = AsosColors.Text2,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(AsosShapes.R12)
                .background(AsosColors.Brand50)
                .clickable(role = Role.Button, onClick = onVoiceClick)
                .semantics { contentDescription = voiceLabel },
            contentAlignment = Alignment.Center,
        ) {
            Icon(AsosIcons.Microphone, contentDescription = null, tint = AsosColors.Brand600, modifier = Modifier.size(18.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC)
@Composable
private fun HomeSearchBarPreview() {
    AsosTheme { HomeSearchBar(onSearchClick = {}, onVoiceClick = {}, modifier = Modifier.padding(20.dp)) }
}
