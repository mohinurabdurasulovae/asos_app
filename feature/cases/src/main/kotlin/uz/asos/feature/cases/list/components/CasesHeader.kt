/*
 * CasesHeader.kt
 * Module: :feature:cases   ·   Work package: WP-08
 * Screen: Kazuslar (design artboard: Kazuslar.dc.html)
 *
 * Back button, "Kazuslar" title, sort button, intro text.
 */
package uz.asos.feature.cases.list.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.feature.cases.R

@Composable
internal fun CasesHeader(
    hasFilter: Boolean,
    onBackClick: () -> Unit,
    onSortClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = AsosIcons.ChevronLeft,
                        contentDescription = stringResource(R.string.cases_back),
                        tint = AsosColors.Navy,
                    )
                }
                Spacer(modifier = Modifier.width(AsosSpacing.xxs))
                Text(
                    text = stringResource(R.string.cases_title),
                    style = AsosTextStyles.ScreenTitle,
                    modifier = Modifier.semantics { heading() },
                )
            }

            Surface(
                onClick = onSortClick,
                shape = AsosShapes.R12,
                color = if (hasFilter) AsosColors.Brand50 else AsosColors.Surface,
                border = BorderStroke(
                    width = 1.dp,
                    color = if (hasFilter) AsosColors.Brand600 else AsosColors.Border,
                ),
            ) {
                Box(
                    modifier = Modifier.size(AsosSpacing.minTouch),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = AsosIcons.Sliders,
                        contentDescription = stringResource(R.string.cases_sort),
                        tint = if (hasFilter) AsosColors.Brand600 else AsosColors.Navy,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }

        Text(
            text = stringResource(R.string.cases_intro),
            style = AsosTextStyles.BodySmall,
            color = AsosColors.Text2,
            modifier = Modifier.padding(horizontal = AsosSpacing.xxs),
        )
    }
}
