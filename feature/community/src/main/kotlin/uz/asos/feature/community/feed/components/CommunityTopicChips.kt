/*
 * CommunityTopicChips.kt
 * Module: :feature:community   ·   Work package: WP-09
 * Screen: Hamjamiyat (design artboard: Hamjamiyat.dc.html)
 *
 * Barchasi · Uy-joy · Ta'lim · Mehnat · Oila · Sud, each a tinted circle over its name.
 */
package uz.asos.feature.community.feed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTones
import uz.asos.core.model.community.CommunityTopic
import uz.asos.feature.community.R

@Composable
internal fun CommunityTopicChips(
    topics: List<CommunityTopic>,
    selected: String?,
    onSelect: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(AsosSpacing.m),
    ) {
        TopicChip(
            label = stringResource(R.string.community_topic_all),
            icon = AsosIcons.ListAll,
            content = AsosColors.Navy,
            container = AsosColors.SurfaceMuted,
            isSelected = selected == null,
            onClick = { onSelect(null) },
        )
        topics.forEach { topic ->
            val tone = AsosTones.fromName(topic.tone)
            TopicChip(
                label = topic.title,
                icon = topic.icon(),
                content = tone.content,
                container = tone.container,
                isSelected = selected == topic.slug,
                onClick = { onSelect(topic.slug) },
            )
        }
    }
}

@Composable
private fun TopicChip(
    label: String,
    icon: ImageVector,
    content: Color,
    container: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(68.dp)
            .clip(AsosShapes.R16)
            .clickable(onClick = onClick)
            .semantics { selected = isSelected },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AsosSpacing.xs),
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(AsosShapes.Circle)
                .then(
                    if (isSelected) {
                        Modifier.border(2.dp, content, AsosShapes.Circle)
                    } else {
                        Modifier
                    },
                )
                .background(container),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = content,
                modifier = Modifier.size(22.dp),
            )
        }
        Text(
            text = label,
            style = AsosTextStyles.Tag,
            color = if (isSelected) AsosColors.Navy else AsosColors.Text2,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}

/** Icon for a topic, picked from its slug; falls back to a speech bubble. */
private fun CommunityTopic.icon(): ImageVector = when (slug) {
    "uy-joy" -> AsosIcons.Home
    "talim" -> AsosIcons.GraduationCap
    "mehnat" -> AsosIcons.Briefcase
    "oila" -> AsosIcons.Family
    "sud" -> AsosIcons.Scales
    else -> AsosIcons.Comment
}
