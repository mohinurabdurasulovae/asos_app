package uz.asos.feature.legalhelp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme

enum class AvatarTone {
    NAVY, BRAND, ORANGE;

    companion object {
        fun forId(id: Int): AvatarTone = AvatarTone.entries[Math.floorMod(id - 1, AvatarTone.entries.size)]
    }
}

private fun AvatarTone.colors(): Pair<Color, Color> = when (this) {
    AvatarTone.NAVY -> AsosColors.Navy to AsosColors.Surface
    AvatarTone.BRAND -> AsosColors.Brand50 to AsosColors.Brand600
    AvatarTone.ORANGE -> AsosColors.WarningBg to AsosColors.WarningOnLight
}

@Composable
internal fun LawyerAvatar(
    initials: String,
    tone: AvatarTone,
    isOnline: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 52.dp,
    fontSize: TextUnit = 17.sp,
    contentDescription: String? = null,
) {
    val (container, content) = tone.colors()
    val shape = RoundedCornerShape(size * 0.3f)
    Box(
        modifier = modifier
            .size(size)
            .then(
                if (contentDescription != null) {
                    val description: String = contentDescription
                    Modifier.clearAndSetSemantics { this.contentDescription = description }
                } else {
                    Modifier
                },
            ),
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .background(container, shape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = initials,
                style = AsosTextStyles.RowTitle.copy(fontSize = fontSize, lineHeight = fontSize),
                color = content,
            )
        }
        if (isOnline) {
            val dot = size * 0.3f
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = dot * 0.2f, y = dot * 0.2f)
                    .size(dot)
                    .background(AsosColors.Success, AsosShapes.Circle)
                    .border(2.dp, AsosColors.Surface, AsosShapes.Circle),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC)
@Composable
private fun LawyerAvatarPreview() {
    AsosTheme {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            LawyerAvatar("SR", AvatarTone.NAVY, isOnline = true)
            LawyerAvatar("MY", AvatarTone.BRAND, isOnline = true)
            LawyerAvatar("BK", AvatarTone.ORANGE, isOnline = false)
            LawyerAvatar("SR", AvatarTone.NAVY, isOnline = true, size = 40.dp, fontSize = 14.sp)
        }
    }
}
