package uz.asos.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.home.ArticleOfDayUi
import uz.asos.feature.home.R

private val WatermarkStyle = AsosTextStyles.ArticleNumberDisplay.copy(fontSize = 176.sp, lineHeight = 176.sp)
private val QuoteStyle = AsosTextStyles.SerifQuote.copy(
    fontSize = 23.sp,
    lineHeight = 30.sp,
    fontStyle = FontStyle.Normal,
    fontWeight = FontWeight.SemiBold,
)

@Composable
internal fun ArticleOfTheDayCard(
    article: ArticleOfDayUi,
    isSaved: Boolean,
    onReadClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(AsosShapes.R26)
            .background(AsosColors.Brand600),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 70.dp, y = 90.dp)
                .size(230.dp)
                .background(AsosColors.Brand500, AsosShapes.Circle),
        )
        Text(
            text = article.watermark,
            style = WatermarkStyle,
            color = AsosColors.Surface.copy(alpha = 0.10f),
            maxLines = 1,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-14).dp, y = (-28).dp),
        )
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = stringResource(R.string.home_article_of_day),
                    style = AsosTextStyles.Tag.copy(fontWeight = FontWeight.ExtraBold),
                    color = AsosColors.Brand600,
                    modifier = Modifier
                        .background(AsosColors.Surface, AsosShapes.Pill)
                        .padding(horizontal = 11.dp, vertical = 6.dp),
                )
                Text(
                    text = article.meta,
                    style = AsosTextStyles.Meta.copy(fontWeight = FontWeight.SemiBold),
                    color = AsosColors.Surface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                text = article.quote,
                style = QuoteStyle,
                color = AsosColors.Surface,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.widthIn(max = 300.dp),
            )
            Text(
                text = article.reference,
                style = AsosTextStyles.Subtitle.copy(fontWeight = FontWeight.Bold),
                color = AsosColors.Surface,
            )
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ReadButton(onClick = onReadClick, modifier = Modifier.weight(1f))
                SaveButton(isSaved = isSaved, onClick = onSaveClick)
            }
        }
    }
}

@Composable
private fun ReadButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(48.dp)
            .clip(AsosShapes.R15)
            .background(AsosColors.Surface)
            .clickable(role = Role.Button, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    ) {
        Text(
            text = stringResource(R.string.home_read_plain),
            style = AsosTextStyles.Button.copy(fontSize = 14.5.sp),
            color = AsosColors.Brand600,
        )
        Icon(AsosIcons.ArrowRight, contentDescription = null, tint = AsosColors.Brand600, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun SaveButton(isSaved: Boolean, onClick: () -> Unit) {
    val label = stringResource(if (isSaved) R.string.home_saved else R.string.home_save)
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(AsosShapes.R15)
            .border(1.dp, AsosColors.Surface.copy(alpha = 0.35f), AsosShapes.R15)
            .clickable(role = Role.Button, onClick = onClick)
            .semantics { contentDescription = label },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = if (isSaved) AsosIcons.BookmarkFilled else AsosIcons.Bookmark,
            contentDescription = null,
            tint = AsosColors.Surface,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Preview(widthDp = 390)
@Composable
private fun ArticleOfTheDayCardPreview() {
    AsosTheme {
        ArticleOfTheDayCard(
            article = ArticleOfDayUi(
                articleKey = "33",
                watermark = "33",
                meta = "VII bob · Shaxsiy huquqlar",
                quote = "“Har kim fikrlash, so‘z va e’tiqod erkinligi huquqiga ega.”",
                reference = "33-modda, 1-qism",
            ),
            isSaved = false,
            onReadClick = {},
            onSaveClick = {},
            modifier = Modifier.padding(20.dp),
        )
    }
}
