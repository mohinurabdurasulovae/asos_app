package uz.asos.feature.dictionary.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.model.dictionary.TermDetail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TermDetailSheet(
    term: TermDetail,
    onSaveToggle: () -> Unit,
    onArticleClick: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = AsosColors.Surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AsosSpacing.l)
                .padding(bottom = AsosSpacing.xxl),
        ) {
            TermOfTheDayCard(
                term = term,
                onSaveToggle = onSaveToggle,
                onArticleClick = onArticleClick,
                isTermOfTheDay = false,
            )
        }
    }
}
