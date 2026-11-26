/*
 * DifficultySelector.kt
 * Module: :feature:learn   ·   Work package: WP-07
 *
 * Boshlang‘ich / O‘rta / Murakkab tabs above the test list (the shared segmented control).
 */
package uz.asos.feature.learn.hub.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.AsosSegmentedControl
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.model.learning.QuizDifficulty
import uz.asos.feature.learn.R

@Composable
internal fun DifficultySelector(
    selected: QuizDifficulty,
    onSelect: (QuizDifficulty) -> Unit,
    modifier: Modifier = Modifier,
) {
    val levels = QuizDifficulty.entries
    AsosSegmentedControl(
        options = listOf(
            stringResource(R.string.learn_difficulty_easy),
            stringResource(R.string.learn_difficulty_medium),
            stringResource(R.string.learn_difficulty_hard),
        ),
        selectedIndex = levels.indexOf(selected),
        onSelect = { onSelect(levels[it]) },
        modifier = modifier,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC)
@Composable
private fun DifficultySelectorPreview() {
    AsosTheme {
        DifficultySelector(selected = QuizDifficulty.MEDIUM, onSelect = {}, modifier = Modifier.padding(20.dp))
    }
}
