package uz.asos.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.component.AsosCard
import uz.asos.core.designsystem.component.ProgressBar
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.designsystem.theme.AsosTones
import uz.asos.feature.home.CaseOfWeekUi
import uz.asos.feature.home.ContinueReadingUi
import uz.asos.feature.home.R

@Composable
internal fun ContinueLearningSection(
    continueReading: ContinueReadingUi?,
    caseOfWeek: CaseOfWeekUi?,
    onArticleClick: (String) -> Unit,
    onCaseClick: (String) -> Unit,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp),
) {
    HomeSection(
        title = stringResource(R.string.home_continue_title),
        actionLabel = stringResource(R.string.home_continue_all),
        onAction = onSeeAllClick,
        headerPadding = contentPadding,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .height(IntrinsicSize.Max)
                .padding(contentPadding),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            continueReading?.let { item ->
                ContinueReadingCard(
                    item = item,
                    onClick = { onArticleClick(item.articleKey) },
                    modifier = Modifier.fillMaxHeight(),
                )
            }
            caseOfWeek?.let { item ->
                CaseCard(
                    item = item,
                    onClick = { onCaseClick(item.caseId) },
                    modifier = Modifier.fillMaxHeight(),
                )
            }
        }
    }
}

@Composable
private fun ContinueReadingCard(
    item: ContinueReadingUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tone = AsosTones.Teal
    AsosCard(modifier = modifier.width(272.dp), shape = AsosShapes.R22, onClick = onClick) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(tone.container, AsosShapes.R12),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(AsosIcons.BookOpen, contentDescription = null, tint = tone.content, modifier = Modifier.size(21.dp))
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(text = item.title, style = AsosTextStyles.RowTitle, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    if (item.meta.isNotBlank()) {
                        Text(text = item.meta, style = AsosTextStyles.Meta.copy(fontSize = 12.sp))
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = item.progressLabel, style = AsosTextStyles.Meta.copy(fontWeight = FontWeight.SemiBold))
                    Text(
                        text = item.percentLabel,
                        style = AsosTextStyles.Meta.copy(fontWeight = FontWeight.SemiBold, color = AsosColors.Navy),
                    )
                }
                ProgressBar(progress = item.progress)
            }
            Spacer(Modifier.weight(1f))
            Text(text = item.nextLabel, style = AsosTextStyles.Link.copy(fontSize = 13.sp))
        }
    }
}

@Composable
private fun CaseCard(
    item: CaseOfWeekUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .width(236.dp)
            .clip(AsosShapes.R22)
            .background(AsosColors.Navy)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(
            text = stringResource(R.string.home_case_tag).uppercase(),
            style = AsosTextStyles.Overline.copy(letterSpacing = 0.04.em),
            color = AsosColors.BrandOnNavySoft,
            modifier = Modifier
                .background(AsosColors.BrandOnNavy.copy(alpha = 0.18f), AsosShapes.Pill)
                .padding(horizontal = 10.dp, vertical = 5.dp),
        )
        Text(
            text = item.title,
            style = AsosTextStyles.RowTitle.copy(fontSize = 16.sp, lineHeight = 21.sp),
            color = AsosColors.Surface,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.weight(1f))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = item.progressLabel,
                style = AsosTextStyles.Meta.copy(fontWeight = FontWeight.SemiBold),
                color = AsosColors.TextOnNavy,
            )
            ProgressBar(
                progress = item.progress,
                color = AsosColors.BrandOnNavy,
                trackColor = AsosColors.Surface.copy(alpha = 0.14f),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC, widthDp = 390)
@Composable
private fun ContinueLearningSectionPreview() {
    AsosTheme {
        ContinueLearningSection(
            continueReading = ContinueReadingUi(
                articleKey = "50",
                title = "Ijtimoiy huquqlar",
                meta = "IX bob",
                progressLabel = "8 / 13 modda o‘qildi",
                percentLabel = "62%",
                progress = 0.62f,
                nextLabel = "Keyingi: 50-modda · Ta’lim →",
            ),
            caseOfWeek = CaseOfWeekUi("14", "Direktor va telefon: shaxsiy hayot daxlsizligi", "2 / 3 bosqich", 0.66f),
            onArticleClick = {},
            onCaseClick = {},
            onSeeAllClick = {},
            modifier = Modifier.padding(vertical = 20.dp),
        )
    }
}
