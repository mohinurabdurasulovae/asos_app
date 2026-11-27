package uz.asos.feature.legalhelp.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.component.ArticleNumberBadge
import uz.asos.core.designsystem.component.ArticleNumberBadgeVariant
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.legalhelp.R
import uz.asos.feature.legalhelp.chat.ChatArticleUi

@Composable
internal fun ArticleLinkMessage(
    article: ChatArticleUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(AsosShapes.R14)
            .background(AsosColors.Navy)
            .clickable(
                role = Role.Button,
                onClickLabel = stringResource(R.string.legalhelp_chat_open_article),
                onClick = onClick,
            )
            .padding(AsosSpacing.m),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ArticleNumberBadge(
            text = article.number.toString(),
            variant = ArticleNumberBadgeVariant.Brand,
            size = 36.dp,
            cornerRadius = 10.dp,
            fontSize = 14.sp,
        )
        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = AsosSpacing.m),
        ) {
            Text(
                text = stringResource(R.string.legalhelp_article_label, article.number),
                style = AsosTextStyles.RowTitle,
                color = AsosColors.Surface,
            )
            Text(text = article.title, style = AsosTextStyles.Meta, color = AsosColors.TextOnNavy)
        }
        Icon(AsosIcons.ChevronRight, contentDescription = null, tint = AsosColors.Surface, modifier = Modifier.size(18.dp))
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
private fun ArticleLinkMessagePreview() {
    AsosTheme {
        ArticleLinkMessage(
            article = ChatArticleUi(number = 31, title = "Shaxsiy hayot va uy-joy daxlsizligi"),
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
