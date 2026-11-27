package uz.asos.feature.auth.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.auth.R

private val HeaderShape = RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp)

private val BrandNameStyle = AsosTextStyles.ScreenTitle.copy(fontSize = 21.sp, lineHeight = 26.sp, letterSpacing = (-0.02).em)

private val TaglineStyle = AsosTextStyles.ArticleNumberDisplay.copy(
    fontSize = 34.sp,
    lineHeight = 39.5.sp,
    fontWeight = FontWeight.SemiBold,
    letterSpacing = (-0.01).em,
)

private val CaptionStyle = AsosTextStyles.Subtitle.copy(fontWeight = FontWeight.Medium, lineHeight = 19.5.sp)

@Composable
internal fun AuthHeader(
    titleStart: String,
    titleAccent: String,
    modifier: Modifier = Modifier,
    caption: String? = null,
    showSteps: Boolean = false,
    onBack: (() -> Unit)? = null,
) {
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(HeaderShape)
            .background(AsosColors.Navy),
    ) {
        Icon(
            imageVector = AsosIcons.LogoPillars,
            contentDescription = null,
            tint = AsosColors.Surface.copy(alpha = 0.07f),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 70.dp, y = topInset + 70.dp)
                .size(300.dp),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = AuthGutter, end = AuthGutter, top = topInset + 16.dp, bottom = 30.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                if (onBack != null) BackButton(onClick = onBack)
                BrandRow()
            }
            Tagline(start = titleStart, accent = titleAccent)
            if (showSteps) StepChips()
            if (caption != null) {
                Text(text = caption, style = CaptionStyle, color = AsosColors.TextOnNavy)
            }
        }
    }
}

@Composable
private fun BackButton(onClick: () -> Unit) {
    val label = stringResource(R.string.auth_back)
    Box(
        modifier = Modifier
            .size(AsosSpacing.minTouch)
            .clip(AsosShapes.R14)
            .background(AsosColors.Surface.copy(alpha = 0.08f))
            .border(1.dp, AsosColors.Surface.copy(alpha = 0.14f), AsosShapes.R14)
            .clickable(role = Role.Button, onClick = onClick)
            .semantics { contentDescription = label },
        contentAlignment = Alignment.Center,
    ) {
        Icon(AsosIcons.ChevronLeft, contentDescription = null, tint = AsosColors.Surface, modifier = Modifier.size(22.dp))
    }
}

@Composable
private fun BrandRow() {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Icon(
            imageVector = AsosIcons.LogoMark,
            contentDescription = stringResource(R.string.auth_logo_description),
            tint = Color.Unspecified,
            modifier = Modifier.size(36.dp),
        )
        Text(text = stringResource(R.string.auth_app_name), style = BrandNameStyle, color = AsosColors.Surface)
    }
}

@Composable
private fun Tagline(start: String, accent: String) {
    Text(
        text = buildAnnotatedString {
            append(start)
            withStyle(SpanStyle(color = AsosColors.BrandOnNavy)) { append(accent) }
        },
        style = TaglineStyle,
        color = AsosColors.Surface,
        modifier = Modifier.semantics { heading() },
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StepChips() {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        StepChip(number = 1, label = stringResource(R.string.auth_step_read))
        StepChip(number = 2, label = stringResource(R.string.auth_step_understand))
        StepChip(number = 3, label = stringResource(R.string.auth_step_apply))
    }
}

@Composable
private fun StepChip(number: Int, label: String) {
    Row(
        modifier = Modifier
            .background(AsosColors.Surface.copy(alpha = 0.08f), AsosShapes.Pill)
            .border(1.dp, AsosColors.Surface.copy(alpha = 0.14f), AsosShapes.Pill)
            .padding(start = 7.dp, end = 12.dp, top = 7.dp, bottom = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp),
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(AsosColors.Brand500, AsosShapes.Circle),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = number.toString(),
                style = AsosTextStyles.Tag.copy(fontSize = 11.sp, lineHeight = 14.sp, fontWeight = FontWeight.ExtraBold),
                color = AsosColors.Surface,
            )
        }
        Text(text = label, style = AsosTextStyles.Chip.copy(fontSize = 13.sp), color = AsosColors.Surface)
    }
}

@Preview(widthDp = 390)
@Composable
private fun AuthHeaderLoginPreview() {
    AsosTheme {
        AuthHeader(
            titleStart = "Konstitutsiyani o‘qing, tushuning va hayotda ",
            titleAccent = "qo‘llang.",
            caption = "O‘zbekiston Respublikasi Konstitutsiyasi · 2023-yil tahriri · 6 bo‘lim, 27 bob, 155 modda",
            showSteps = true,
        )
    }
}

@Preview(widthDp = 390)
@Composable
private fun AuthHeaderRegisterPreview() {
    AsosTheme {
        AuthHeader(
            titleStart = "Bir daqiqada ",
            titleAccent = "akkaunt yarating.",
            caption = "Ism, telefon raqam va SMS kod — boshqa hech narsa kerak emas.",
            onBack = {},
        )
    }
}
