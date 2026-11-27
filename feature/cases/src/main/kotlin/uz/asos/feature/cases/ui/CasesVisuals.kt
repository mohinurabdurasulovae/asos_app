package uz.asos.feature.cases.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosTone
import uz.asos.core.designsystem.theme.AsosTones
import uz.asos.feature.cases.data.model.CaseDifficulty
import uz.asos.feature.cases.data.model.CaseTopic

internal object CasesPalette {
    val NavyCardChip = Color(0xFF233B63)
    val PersonaBg = AsosColors.WarningBg
    val PersonaText = AsosColors.WarningOnLight
}

internal fun CaseTopic.tone(): AsosTone = when (this) {
    CaseTopic.SHAXSIY_HAYOT -> AsosTones.Brand
    CaseTopic.TALIM -> AsosTones.Teal
    CaseTopic.MEHNAT -> AsosTones.Orange
    CaseTopic.SUD -> AsosTones.Brand
    CaseTopic.UY_JOY -> AsosTones.Pink
}

@Composable
internal fun CaseDifficultyBars(
    level: Int,
    onDark: Boolean,
    modifier: Modifier = Modifier,
    label: String? = null,
) {
    Row(
        modifier = modifier.clearAndSetSemantics {
            if (label != null) contentDescription = label
        },
        horizontalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        for (bar in 1..3) {
            val active = bar <= level
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(11.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        when {
                            active && onDark -> AsosColors.BrandOnNavy
                            active -> AsosColors.Brand500
                            onDark -> AsosColors.Surface.copy(alpha = 0.22f)
                            else -> AsosColors.Outline
                        },
                    ),
            )
        }
    }
}

@Composable
internal fun CaseDifficultyBars(
    difficulty: CaseDifficulty,
    onDark: Boolean,
    modifier: Modifier = Modifier,
) = CaseDifficultyBars(
    level = difficulty.level,
    onDark = onDark,
    modifier = modifier,
    label = difficulty.label,
)
