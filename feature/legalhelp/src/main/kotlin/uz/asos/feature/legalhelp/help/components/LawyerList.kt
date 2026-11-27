/*
 * LawyerList.kt
 * Module: :feature:legalhelp   ·   Work package: WP-10
 *
 * "Advokatlar" header with the online count, the category filter chips and the filtered
 * LawyerCards (or an empty message).
 */
package uz.asos.feature.legalhelp.help.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.AsosChip
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.legalhelp.R
import uz.asos.feature.legalhelp.help.LawyerUi

@Composable
internal fun LawyersHeader(
    onlineCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.legalhelp_lawyers_title),
            style = AsosTextStyles.SectionTitle,
            modifier = Modifier.weight(1f),
        )
        if (onlineCount > 0) {
            Box(
                Modifier
                    .size(7.dp)
                    .background(AsosColors.Success, AsosShapes.Circle),
            )
            Text(
                text = stringResource(R.string.legalhelp_lawyers_online, onlineCount),
                style = AsosTextStyles.Tag,
                color = AsosColors.SuccessOnLight,
                modifier = Modifier.padding(start = AsosSpacing.xs),
            )
        }
    }
}

@Composable
internal fun LawyerFilterChips(
    specialties: List<String>,
    selected: String?,
    onSelect: (String?) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val options: List<String?> = listOf<String?>(null) + specialties
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(contentPadding),
        horizontalArrangement = Arrangement.spacedBy(AsosSpacing.s),
    ) {
        options.forEach { category ->
            AsosChip(
                label = category ?: stringResource(R.string.legalhelp_filter_all),
                selected = category == selected,
                onClick = { onSelect(category) },
            )
        }
    }
}

/** Cards for the lawyers that match the selected chip. */
@Composable
internal fun LawyerList(
    lawyers: List<LawyerUi>,
    onWrite: (Int) -> Unit,
    onCall: (Int) -> Unit,
    onVideoCall: (Int) -> Unit,
    onBook: (Int) -> Unit,
    modifier: Modifier = Modifier,
    openingChatForLawyerId: Int? = null,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(AsosSpacing.m)) {
        if (lawyers.isEmpty()) {
            Text(
                text = stringResource(R.string.legalhelp_lawyers_empty),
                style = AsosTextStyles.BodySmall.copy(color = AsosColors.Text2),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AsosSpacing.xxl),
            )
        }
        lawyers.forEach { lawyer ->
            LawyerCard(
                lawyer = lawyer,
                onWrite = { onWrite(lawyer.id) },
                onCall = { onCall(lawyer.id) },
                onVideoCall = { onVideoCall(lawyer.id) },
                onBook = { onBook(lawyer.id) },
                isOpeningChat = openingChatForLawyerId == lawyer.id,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC, widthDp = 390)
@Composable
private fun LawyerListPreview() {
    AsosTheme {
        Column(Modifier.padding(vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            LawyersHeader(onlineCount = 2, modifier = Modifier.padding(horizontal = 20.dp))
            LawyerFilterChips(
                specialties = listOf("Uy-joy", "Oila", "Mehnat", "Ta’lim"),
                selected = null,
                onSelect = {},
                contentPadding = PaddingValues(horizontal = 20.dp),
            )
            LawyerList(
                lawyers = PreviewLawyers,
                onWrite = {},
                onCall = {},
                onVideoCall = {},
                onBook = {},
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }
    }
}
