package uz.asos.feature.cases.detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.feature.cases.R
import uz.asos.feature.cases.detail.ArticleChoiceUi

@Composable
internal fun ArticleChoiceList(
    options: List<ArticleChoiceUi>,
    isSubmitted: Boolean,
    onToggle: (number: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AsosSpacing.sm),
    ) {
        Text(
            text = stringResource(R.string.cases_articles_question),
            style = AsosTextStyles.CardTitle,
        )
        options.forEach { choice ->
            ArticleChoiceCard(
                choice = choice,
                isSubmitted = isSubmitted,
                onClick = { onToggle(choice.option.number) },
            )
        }
    }
}

@Composable
private fun ArticleChoiceCard(
    choice: ArticleChoiceUi,
    isSubmitted: Boolean,
    onClick: () -> Unit,
) {
    val revealedCorrect = isSubmitted && choice.isCorrectOption
    val revealedWrong = isSubmitted && choice.isSelected && !choice.isCorrectOption

    val containerColor = when {
        revealedCorrect -> AsosColors.SuccessBg
        revealedWrong -> AsosColors.ErrorBg
        choice.isSelected -> AsosColors.Brand50
        else -> AsosColors.Surface
    }
    val borderColor = when {
        revealedCorrect -> AsosColors.Success
        revealedWrong -> AsosColors.Error
        choice.isSelected -> AsosColors.Brand600
        else -> AsosColors.Border
    }
    val badgeColor = when {
        revealedWrong -> AsosColors.Error
        revealedCorrect || choice.isSelected -> AsosColors.Navy
        else -> AsosColors.SurfaceMuted
    }
    val badgeTextColor = if (revealedCorrect || revealedWrong || choice.isSelected) {
        AsosColors.Surface
    } else {
        AsosColors.Navy
    }

    Surface(
        onClick = onClick,
        enabled = !isSubmitted,
        modifier = Modifier
            .fillMaxWidth()
            .semantics { selected = choice.isSelected },
        shape = AsosShapes.R16,
        color = containerColor,
        border = BorderStroke(
            width = if (choice.isSelected || revealedCorrect) 1.5.dp else 1.dp,
            color = borderColor,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AsosSpacing.mm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AsosSpacing.m),
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(AsosShapes.R10)
                    .background(badgeColor),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = choice.option.number.toString(),
                    style = AsosTextStyles.RowTitleRegular,
                    color = badgeTextColor,
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = choice.option.title,
                    style = AsosTextStyles.RowTitleRegular,
                )
                when {
                    revealedCorrect -> Text(
                        text = stringResource(R.string.cases_choice_correct),
                        style = AsosTextStyles.Tag,
                        color = AsosColors.SuccessOnLight,
                    )
                    revealedWrong -> Text(
                        text = stringResource(R.string.cases_choice_wrong),
                        style = AsosTextStyles.Tag,
                        color = AsosColors.ErrorOnLight,
                    )
                }
            }

            ChoiceCheckbox(
                isSelected = choice.isSelected,
                revealedCorrect = revealedCorrect,
                revealedWrong = revealedWrong,
            )
        }
    }
}

@Composable
private fun ChoiceCheckbox(
    isSelected: Boolean,
    revealedCorrect: Boolean,
    revealedWrong: Boolean,
) {
    val fill = when {
        revealedCorrect -> AsosColors.Success
        revealedWrong -> AsosColors.Error
        isSelected -> AsosColors.Brand600
        else -> Color.Transparent
    }
    val outline = when {
        revealedCorrect -> AsosColors.Success
        revealedWrong -> AsosColors.Error
        isSelected -> AsosColors.Brand600
        else -> AsosColors.Outline
    }

    Box(
        modifier = Modifier
            .size(26.dp)
            .clip(AsosShapes.R10)
            .background(fill)
            .border(width = 1.5.dp, color = outline, shape = AsosShapes.R10),
        contentAlignment = Alignment.Center,
    ) {
        when {
            revealedWrong -> Icon(
                imageVector = AsosIcons.Close,
                contentDescription = null,
                tint = AsosColors.Surface,
                modifier = Modifier.size(16.dp),
            )
            revealedCorrect || isSelected -> Icon(
                imageVector = AsosIcons.Check,
                contentDescription = null,
                tint = AsosColors.Surface,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}
