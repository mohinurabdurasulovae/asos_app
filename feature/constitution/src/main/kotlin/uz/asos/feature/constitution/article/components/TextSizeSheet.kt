package uz.asos.feature.constitution.article.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.AsosSegmentedControl
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.model.user.ReaderTextSize
import uz.asos.feature.constitution.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TextSizeSheet(
    current: ReaderTextSize,
    onSelect: (ReaderTextSize) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = AsosColors.Surface,
        shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
    ) {
        TextSizeSheetContent(current = current, onSelect = onSelect)
    }
}

@Composable
private fun TextSizeSheetContent(
    current: ReaderTextSize,
    onSelect: (ReaderTextSize) -> Unit,
) {
    val sizes = ReaderTextSize.entries
    val labels = listOf(
        stringResource(R.string.constitution_text_size_small),
        stringResource(R.string.constitution_text_size_medium),
        stringResource(R.string.constitution_text_size_large),
        stringResource(R.string.constitution_text_size_xlarge),
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Text(
            text = stringResource(R.string.constitution_text_size_title),
            style = AsosTextStyles.CardTitle,
            modifier = Modifier.semantics { heading() },
        )
        Text(
            text = stringResource(R.string.constitution_text_size_sample),
            style = legalBodyStyle(current),
            modifier = Modifier
                .fillMaxWidth()
                .background(AsosColors.Background, AsosShapes.R16)
                .padding(16.dp),
        )
        AsosSegmentedControl(
            options = labels,
            selectedIndex = sizes.indexOf(current),
            onSelect = { onSelect(sizes[it]) },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TextSizeSheetContentPreview() {
    AsosTheme { TextSizeSheetContent(current = ReaderTextSize.LARGE, onSelect = {}) }
}
