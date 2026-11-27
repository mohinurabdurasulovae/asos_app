package uz.asos.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme

enum class ArticleNumberBadgeVariant(val container: Color, val content: Color) {
    Dark(AsosColors.Navy, AsosColors.Surface),
    Brand(AsosColors.Brand500, AsosColors.Surface),
    Light(AsosColors.Background, AsosColors.Navy),
}

@Composable
fun ArticleNumberBadge(
    text: String,
    modifier: Modifier = Modifier,
    variant: ArticleNumberBadgeVariant = ArticleNumberBadgeVariant.Dark,
    size: Dp = 40.dp,
    cornerRadius: Dp = 12.dp,
    fontSize: TextUnit = 15.sp,
) {
    Box(
        modifier = modifier
            .size(size)
            .background(variant.container, RoundedCornerShape(cornerRadius)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = AsosTextStyles.SerifRoman.copy(fontSize = fontSize, lineHeight = fontSize),
            color = variant.content,
            maxLines = 1,
        )
    }
}

@Preview
@Composable
private fun ArticleNumberBadgePreview() {
    AsosTheme {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ArticleNumberBadge("50", size = 52.dp, cornerRadius = 15.dp, fontSize = 24.sp)
            ArticleNumberBadge("IX", variant = ArticleNumberBadgeVariant.Brand)
            ArticleNumberBadge("VII", variant = ArticleNumberBadgeVariant.Light)
        }
    }
}
