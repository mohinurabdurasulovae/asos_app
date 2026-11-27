/*
 * AlphabetIndex.kt
 * Module: :feature:dictionary   ·   Work package: WP-06
 * Screen: Konstitutsiyaviy lug'at (design artboard: Lugat.dc.html)
 *
 * The rail down the right edge: every Uzbek letter, the selected one in a brand circle.
 * Letters with no terms are dimmed and not clickable.
 */
package uz.asos.feature.dictionary.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.model.dictionary.AlphabetLetter

@Composable
internal fun AlphabetIndex(
    letters: List<AlphabetLetter>,
    selectedLetter: String?,
    onLetterSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.width(26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(1.dp),
    ) {
        letters.forEach { entry ->
            val isSelected = entry.letter == selectedLetter
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .background(AsosColors.Brand500, AsosShapes.Circle)
                        .clickable { onLetterSelect(entry.letter) }
                        .semantics { selected = true },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = entry.letter,
                        style = AlphabetStyle,
                        color = AsosColors.Surface,
                    )
                }
            } else {
                Text(
                    text = entry.letter,
                    style = AlphabetStyle,
                    color = if (entry.isEmpty) AsosColors.Outline else AsosColors.Text2,
                    modifier = Modifier
                        .padding(vertical = 1.dp)
                        .then(
                            if (entry.isEmpty) {
                                Modifier
                            } else {
                                Modifier.clickable { onLetterSelect(entry.letter) }
                            },
                        ),
                )
            }
        }
    }
}

private val AlphabetStyle = AsosTextStyles.Tag.copy(
    fontSize = 10.5.sp,
    lineHeight = 13.sp,
    fontWeight = FontWeight.Bold,
)
