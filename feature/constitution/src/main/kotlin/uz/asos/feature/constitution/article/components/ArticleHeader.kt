/*
 * ArticleHeader.kt
 * Module: :feature:constitution   ·   Work package: WP-05
 * Screen: Modda (50-modda) (design artboard: Modda.dc.html)
 *
 * Chapter title (blue), "50-modda" in large serif, editorial title, and tags:
 * topics · reading time · "01.05.2023 dan kuchda".
 */
package uz.asos.feature.constitution.article.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.component.AsosTag
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.designsystem.theme.AsosTones
import uz.asos.feature.constitution.R
import uz.asos.feature.constitution.article.TagUi

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ArticleHeader(
    chapterTitle: String?,
    heading: String,
    title: String?,
    topics: List<TagUi>,
    readingTime: String,
    effectiveFrom: String?,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (chapterTitle != null) {
            Text(
                text = chapterTitle,
                style = AsosTextStyles.Meta.copy(fontWeight = FontWeight.Bold, lineHeight = 17.5.sp),
                color = AsosColors.Brand600,
            )
        }
        Text(
            text = heading,
            style = AsosTextStyles.ArticleNumberDisplay,
            modifier = Modifier.semantics { heading() },
        )
        if (title != null) {
            Text(
                text = title,
                style = AsosTextStyles.RowTitle.copy(fontSize = 18.sp, lineHeight = 24.sp, fontWeight = FontWeight.Bold),
                color = AsosColors.Text3,
            )
        }
        FlowRow(
            modifier = Modifier.padding(top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            topics.forEach { AsosTag(text = it.text, tone = AsosTones.fromName(it.tone ?: "teal")) }
            AsosTag(text = readingTime)
            if (effectiveFrom != null) {
                AsosTag(text = stringResource(R.string.constitution_in_force_since, effectiveFrom))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ArticleHeaderPreview() {
    AsosTheme {
        ArticleHeader(
            chapterTitle = "Iqtisodiy, ijtimoiy, madaniy va ekologik huquqlar",
            heading = "50-modda",
            title = "Ta’lim olish huquqi",
            topics = listOf(TagUi("Ta’lim", "teal")),
            readingTime = "4 daqiqa",
            effectiveFrom = "01.05.2023",
            modifier = Modifier.padding(24.dp),
        )
    }
}
