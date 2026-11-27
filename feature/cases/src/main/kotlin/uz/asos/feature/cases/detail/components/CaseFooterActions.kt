/*
 * CaseFooterActions.kt
 * Module: :feature:cases   ·   Work package: WP-08
 * Screen: Kazus — yechim (design artboard: Kazus.dc.html)
 *
 * "Qaytadan" and "Keyingi kazus", shown once the answer is checked.
 */
package uz.asos.feature.cases.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.AsosButton
import uz.asos.core.designsystem.component.AsosButtonVariant
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.feature.cases.R

@Composable
internal fun CaseFooterActions(
    hasNextCase: Boolean,
    onRetry: () -> Unit,
    onNextCase: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AsosSpacing.sm),
    ) {
        AsosButton(
            text = stringResource(R.string.cases_action_retry),
            onClick = onRetry,
            variant = AsosButtonVariant.Secondary,
            height = 48.dp,
            modifier = Modifier.weight(1f),
        )

        if (hasNextCase) {
            AsosButton(
                text = stringResource(R.string.cases_action_next),
                onClick = onNextCase,
                trailingIcon = AsosIcons.ArrowRight,
                height = 48.dp,
                modifier = Modifier.weight(1.3f),
            )
        }
    }
}
