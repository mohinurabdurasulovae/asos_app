/*
 * AlphabetIndex.kt
 * Module: :feature:dictionary   ·   Work package: WP-06
 * Screen: Konstitutsiyaviy lug‘at (design artboard: Lugat.dc.html)
 *
 * "Alifbo" A–Z letter rail (Uzbek Latin alphabet incl. O‘, G‘, Sh, Ch, Ng). The current letter
 * sits in a brand circle; letters with no terms are dimmed and not clickable. The letters are
 * small to fit 29 on a phone, so each one takes the full rail width as its touch area.
 */
package uz.asos.feature.dictionary.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.dictionary.LetterUi
import uz.asos.feature.dictionary.R

@Composable
internal fun AlphabetIndex(
    letters: List<LetterUi>,
    currentLetter: String?,
    onLetterClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val railLabel = stringResource(R.string.dictionary_alphabet)
    Column(
        modifier = modifier
            .width(RailWidth)
            .semantics { contentDescription = railLabel },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        letters.forEach { item ->
            val isCurrent = item.letter == currentLetter
            val jumpLabel = stringResource(R.string.dictionary_letter_jump, item.letter)
            Box(
                modifier = Modifier
                    .width(RailWidth)
                    .then(
                        if (item.enabled) {
                            Modifier.clickable(role = Role.Button, onClickLabel = jumpLabel) { onLetterClick(item.letter) }
                        } else {
                            Modifier
                        },
                    )
                    .semantics { selected = isCurrent },
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .then(if (isCurrent) Modifier.background(AsosColors.Brand600, AsosShapes.Circle) else Modifier),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = item.letter,
                        style = AsosTextStyles.TabLabel.copy(
                            fontSize = if (item.letter.length > 1) 8.5.sp else 10.sp,
                            lineHeight = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                        ),
                        color = when {
                            isCurrent -> AsosColors.Surface
                            item.enabled -> AsosColors.Brand600
                            else -> AsosColors.SoftGray
                        },
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

private val RailWidth = 28.dp

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC, heightDp = 600)
@Composable
private fun AlphabetIndexPreview() {
    AsosTheme {
        AlphabetIndex(
            letters = listOf("A", "B", "D", "E", "F", "G", "H", "I", "J", "K", "O‘", "G‘", "Sh", "Ch", "Ng")
                .map { LetterUi(it, enabled = it in setOf("A", "B", "D", "E", "F", "I", "K", "Sh", "Ch")) },
            currentLetter = "D",
            onLetterClick = {},
        )
    }
}
