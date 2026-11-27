package uz.asos.feature.constitution.article.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.AsosButton
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosElevation
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.designsystem.theme.softShadow
import uz.asos.feature.constitution.R

@Composable
internal fun ArticleActionsBar(
    previousNumber: Int?,
    nextNumber: Int?,
    quizId: String?,
    onSiblingClick: (Int) -> Unit,
    onQuizClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .softShadow(RectangleShape, AsosElevation.bar)
            .background(AsosColors.Surface),
    ) {
        HorizontalDivider(thickness = 1.dp, color = AsosColors.Divider)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (previousNumber != null) {
                SiblingButton(
                    number = previousNumber,
                    icon = AsosIcons.ChevronLeft,
                    iconFirst = true,
                    description = stringResource(R.string.constitution_prev_article, previousNumber),
                    onClick = { onSiblingClick(previousNumber) },
                )
            }
            AsosButton(
                text = stringResource(if (quizId != null) R.string.constitution_quiz_button else R.string.constitution_quiz_soon),
                onClick = { quizId?.let(onQuizClick) },
                enabled = quizId != null,
                height = 50.dp,
                leadingIcon = AsosIcons.GraduationCap,
                modifier = Modifier.weight(1f),
            )
            if (nextNumber != null) {
                SiblingButton(
                    number = nextNumber,
                    icon = AsosIcons.ChevronRight,
                    iconFirst = false,
                    description = stringResource(R.string.constitution_next_article, nextNumber),
                    onClick = { onSiblingClick(nextNumber) },
                )
            }
        }
    }
}

@Composable
private fun SiblingButton(
    number: Int,
    icon: ImageVector,
    iconFirst: Boolean,
    description: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .height(50.dp)
            .border(1.dp, AsosColors.Border, AsosShapes.R15)
            .clip(AsosShapes.R15)
            .clickable(role = Role.Button, onClick = onClick)
            .semantics { contentDescription = description }
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (iconFirst) Icon(icon, contentDescription = null, tint = AsosColors.Navy, modifier = Modifier.size(18.dp))
        Text(
            text = number.toString(),
            style = AsosTextStyles.BodySmall.copy(fontWeight = FontWeight.Bold),
            color = AsosColors.Navy,
        )
        if (!iconFirst) Icon(icon, contentDescription = null, tint = AsosColors.Navy, modifier = Modifier.size(18.dp))
    }
}

@Preview
@Composable
private fun ArticleActionsBarPreview() {
    AsosTheme {
        ArticleActionsBar(
            previousNumber = 49,
            nextNumber = 51,
            quizId = "ijtimoiy-huquqlar",
            onSiblingClick = {},
            onQuizClick = {},
        )
    }
}
