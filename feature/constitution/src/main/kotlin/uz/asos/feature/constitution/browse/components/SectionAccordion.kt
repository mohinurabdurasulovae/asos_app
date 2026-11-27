package uz.asos.feature.constitution.browse.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.component.AsosCard
import uz.asos.core.designsystem.component.AsosCardStyle
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.constitution.R
import uz.asos.feature.constitution.browse.ArticleListState
import uz.asos.feature.constitution.browse.ArticleRowUi
import uz.asos.feature.constitution.browse.ChapterUi
import uz.asos.feature.constitution.browse.ReadState
import uz.asos.feature.constitution.browse.SectionUi

@Composable
internal fun SectionAccordion(
    section: SectionUi,
    expanded: Boolean,
    expandedChapters: Set<String>,
    groupArticles: (String) -> ArticleListState?,
    directArticlesKey: String,
    onToggle: () -> Unit,
    onChapterToggle: (String) -> Unit,
    onGroupRetry: (String) -> Unit,
    onArticleClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = if (expanded) AsosShapes.R20 else AsosShapes.R18
    AsosCard(
        modifier = modifier
            .fillMaxWidth()
            .then(if (expanded) Modifier.border(1.dp, AsosColors.Brand100, shape) else Modifier),
        shape = shape,
        style = if (expanded) AsosCardStyle.Elevated else AsosCardStyle.Outlined,
        borderColor = AsosColors.BorderSoft,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            SectionHeaderRow(section = section, expanded = expanded, onClick = onToggle)
            AnimatedVisibility(visible = expanded) {
                Column(Modifier.fillMaxWidth()) {
                    if (section.chapters.isEmpty()) {
                        HorizontalDivider(thickness = 1.dp, color = AsosColors.Divider)
                        ChapterArticles(
                            state = groupArticles(directArticlesKey),
                            onArticleClick = onArticleClick,
                            onRetry = { onGroupRetry(directArticlesKey) },
                        )
                    }
                    section.chapters.forEach { chapter ->
                        val chapterOpen = chapter.roman in expandedChapters
                        HorizontalDivider(thickness = 1.dp, color = AsosColors.Divider)
                        ChapterRow(
                            chapter = chapter,
                            expanded = chapterOpen,
                            onClick = { onChapterToggle(chapter.roman) },
                        )
                        AnimatedVisibility(visible = chapterOpen) {
                            ChapterArticles(
                                state = groupArticles(chapter.roman),
                                onArticleClick = onArticleClick,
                                onRetry = { onGroupRetry(chapter.roman) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeaderRow(
    section: SectionUi,
    expanded: Boolean,
    onClick: () -> Unit,
) {
    val stateLabel = stringResource(if (expanded) R.string.constitution_collapse else R.string.constitution_expand)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClickLabel = stateLabel, onClick = onClick)
            .semantics { stateDescription = stateLabel }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = section.overline, style = AsosTextStyles.Overline, color = AsosColors.Brand600)
            Text(text = section.title, style = AsosTextStyles.RowTitle)
            Text(text = section.meta, style = AsosTextStyles.Meta)
        }
        Icon(
            imageVector = if (expanded) AsosIcons.ChevronUp else AsosIcons.ChevronDown,
            contentDescription = null,
            tint = if (expanded) AsosColors.Brand600 else AsosColors.Text2,
            modifier = Modifier.size(18.dp),
        )
    }
}

@Composable
internal fun PreambleRow(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AsosCard(
        modifier = modifier.fillMaxWidth(),
        shape = AsosShapes.R18,
        style = AsosCardStyle.Outlined,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(modifier = Modifier.width(40.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = "§",
                    style = AsosTextStyles.SerifRoman.copy(fontSize = 17.sp, fontStyle = FontStyle.Italic),
                    color = AsosColors.Brand600,
                    textAlign = TextAlign.Center,
                )
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(text = stringResource(R.string.constitution_preamble_title), style = AsosTextStyles.RowTitle)
                Text(text = stringResource(R.string.constitution_preamble_subtitle), style = AsosTextStyles.Meta)
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

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC)
@Composable
private fun SectionAccordionPreview() {
    val section = SectionUi(
        roman = "II",
        overline = "II BO‘LIM",
        title = "Inson va fuqaroning asosiy huquqlari, erkinliklari va burchlari",
        meta = "7 bob · 19–64-moddalar",
        chapters = listOf(
            ChapterUi("V", "Umumiy qoidalar", "19–21-moddalar", ReadState.READ, 100),
            ChapterUi("IX", "Iqtisodiy, ijtimoiy, madaniy va ekologik huquqlar", "41–53-moddalar · 8/13 o‘qildi", ReadState.IN_PROGRESS, 62),
            ChapterUi("XI", "Fuqarolarning burchlari", "59–64-moddalar", ReadState.NOT_STARTED, 0),
        ),
        directArticlesQuery = null,
    )
    val articles = ArticleListState.Loaded(
        listOf(ArticleRowUi("50", "50", "Ta’lim olish huquqi", "IX bob · 4 daqiqa", ReadState.IN_PROGRESS)),
    )
    AsosTheme {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            PreambleRow(onClick = {})
            SectionAccordion(
                section = section,
                expanded = true,
                expandedChapters = setOf("IX"),
                groupArticles = { if (it == "IX") articles else null },
                directArticlesKey = "section-II",
                onToggle = {},
                onChapterToggle = {},
                onGroupRetry = {},
                onArticleClick = {},
            )
        }
    }
}
