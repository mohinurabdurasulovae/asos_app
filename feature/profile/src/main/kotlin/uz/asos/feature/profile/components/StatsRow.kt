package uz.asos.feature.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.feature.profile.R

@Composable
internal fun StatsRow(
    pointsLabel: String,
    quizzesDone: Int,
    casesSolved: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AsosSpacing.xxl),
    ) {
        Stat(value = pointsLabel, label = stringResource(R.string.profile_stat_points))
        Stat(value = quizzesDone.toString(), label = stringResource(R.string.profile_stat_quizzes))
        Stat(value = casesSolved.toString(), label = stringResource(R.string.profile_stat_cases))
    }
}

@Composable
private fun Stat(value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = value,
            style = AsosTextStyles.SectionTitle.copy(fontSize = 20.sp),
            color = AsosColors.Surface,
        )
        Text(
            text = label,
            style = AsosTextStyles.Meta,
            color = AsosColors.TextOnNavy,
        )
    }
}
