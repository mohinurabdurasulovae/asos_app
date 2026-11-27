package uz.asos.feature.constitution.browse.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.AsosCard
import uz.asos.core.designsystem.component.AsosCardStyle
import uz.asos.core.designsystem.component.AsosChip
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.designsystem.theme.AsosTones
import uz.asos.feature.constitution.R
import uz.asos.feature.constitution.browse.TopicUi

@Composable
internal fun TopicChipsRow(
    topics: List<TopicUi>,
    selectedSlug: String?,
    onSelect: (String?) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp),
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item(key = "all") {
            AsosChip(
                label = stringResource(R.string.constitution_topic_all),
                selected = selectedSlug == null,
                onClick = { onSelect(null) },
            )
        }
        items(topics, key = { it.slug }) { topic ->
            AsosChip(
                label = topic.title,
                selected = topic.slug == selectedSlug,
                onClick = { onSelect(topic.slug) },
            )
        }
    }
}

@Composable
internal fun TopicListItem(
    topic: TopicUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tone = AsosTones.fromName(topic.tone)
    AsosCard(
        modifier = modifier.fillMaxWidth(),
        shape = AsosShapes.R18,
        style = AsosCardStyle.Outlined,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(tone.container, AsosShapes.R14),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = topicIcon(topic.slug, topic.title),
                    contentDescription = null,
                    tint = tone.content,
                    modifier = Modifier.size(21.dp),
                )
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(text = topic.title, style = AsosTextStyles.RowTitle)
                Text(text = topic.countLabel, style = AsosTextStyles.Meta)
            }
            Icon(
                imageVector = AsosIcons.ChevronRight,
                contentDescription = null,
                tint = AsosColors.Text2,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

private fun topicIcon(slug: String, title: String): ImageVector {
    val key = "$slug $title".lowercase()
    return when {
        "ta" in key && "lim" in key -> AsosIcons.GraduationCap
        "erkin" in key || "shaxs" in key -> AsosIcons.ShieldCheck
        "siyos" in key || "saylov" in key -> AsosIcons.Ballot
        "mehnat" in key || "ijtimoiy" in key || "iqtisod" in key -> AsosIcons.Briefcase
        "burch" in key -> AsosIcons.ClipboardCheck
        "oila" in key || "yosh" in key || "bola" in key -> AsosIcons.Family
        "sud" in key || "adolat" in key -> AsosIcons.Scales
        "davlat" in key || "hokimiyat" in key -> AsosIcons.Government
        "huquq" in key -> AsosIcons.ShieldCheck
        else -> AsosIcons.Book
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC)
@Composable
private fun TopicComponentsPreview() {
    val topics = listOf(
        TopicUi("huquqlar", "Huquqlar", "24 modda", "brand"),
        TopicUi("talim", "Ta’lim", "3 modda", "teal"),
        TopicUi("burchlar", "Burchlar", "6 modda", "orange"),
    )
    AsosTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            TopicChipsRow(topics = topics, selectedSlug = null, onSelect = {})
            TopicListItem(topic = topics[1], onClick = {}, modifier = Modifier.padding(horizontal = 20.dp))
        }
    }
}
