package uz.asos.feature.auth.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.auth.common.overflowVertically

internal val AuthSheetGap = 18.dp
internal val AuthGutter = 24.dp

internal val AuthTitleStyle = AsosTextStyles.ScreenTitle.copy(fontSize = 23.sp, lineHeight = 28.sp, letterSpacing = (-0.02).em)
internal val AuthMessageStyle = AsosTextStyles.BodySmall.copy(color = AsosColors.Text2)
internal val AuthSmallPrintStyle = AsosTextStyles.Meta.copy(fontSize = 12.sp, lineHeight = 18.sp)
internal val AuthLabelStyle = AsosTextStyles.Subtitle.copy(fontWeight = FontWeight.Bold, color = AsosColors.Navy)
internal val AuthErrorStyle = AsosTextStyles.Meta.copy(fontSize = 13.sp, color = AsosColors.ErrorOnLight)

private val LinkStyle = AsosTextStyles.Link.copy(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.ExtraBold)
private val LinkTouchPadding = 12.dp

@Composable
internal fun SheetTitle(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(text = title, style = AuthTitleStyle, modifier = Modifier.semantics { heading() })
        Text(text = message, style = AuthMessageStyle)
    }
}

@Composable
internal fun LinkText(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Text(
        text = text,
        style = LinkStyle,
        color = if (enabled) AsosColors.Brand600 else AsosColors.Text2,
        modifier = modifier
            .overflowVertically(LinkTouchPadding)
            .clip(AsosShapes.R10)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .padding(vertical = LinkTouchPadding),
    )
}

@Composable
internal fun LinkPrompt(
    question: String,
    link: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
    ) {
        Text(text = question, style = AuthMessageStyle)
        LinkText(text = link, onClick = onClick, enabled = enabled)
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthTextsPreview() {
    AsosTheme {
        Column(Modifier.padding(AuthGutter), verticalArrangement = Arrangement.spacedBy(AuthSheetGap)) {
            SheetTitle(title = "Xush kelibsiz", message = "Telefon raqamingizga tasdiqlash kodini yuboramiz.")
            LinkText(text = "Raqamni o‘zgartirish", onClick = {})
            LinkPrompt(question = "Akkauntingiz yo‘qmi?", link = "Ro‘yxatdan o‘tish", onClick = {})
        }
    }
}
