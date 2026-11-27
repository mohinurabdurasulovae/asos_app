/*
 * SettingsSection.kt
 * Module: :feature:profile   ·   Work package: WP-11
 * Screen: Profil (design artboard: Profil.dc.html)
 *
 * "Sozlamalar": personal data, the daily-reminder switch, language, text size, privacy.
 */
package uz.asos.feature.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.AsosCard
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.model.user.ReaderTextSize
import uz.asos.feature.profile.R

@Composable
internal fun SettingsSection(
    notifyDaily: Boolean,
    locale: String?,
    textSize: ReaderTextSize,
    onPersonalData: () -> Unit,
    onNotifyDailyChange: (Boolean) -> Unit,
    onLanguage: () -> Unit,
    onTextSize: () -> Unit,
    onPrivacy: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AsosSpacing.m),
    ) {
        Text(
            text = stringResource(R.string.profile_settings_section),
            style = AsosTextStyles.CardTitle,
        )

        AsosCard(modifier = Modifier.fillMaxWidth()) {
            SettingsRow(
                icon = AsosIcons.Person,
                label = stringResource(R.string.profile_personal_data),
                onClick = onPersonalData,
            )
            HorizontalDivider(color = AsosColors.Divider)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AsosSpacing.l, vertical = AsosSpacing.s),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AsosSpacing.m),
            ) {
                Icon(
                    imageVector = AsosIcons.Bell,
                    contentDescription = null,
                    tint = AsosColors.Text2,
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    text = stringResource(R.string.profile_daily_reminders),
                    style = AsosTextStyles.RowTitleRegular,
                    modifier = Modifier.weight(1f),
                )
                Switch(
                    checked = notifyDaily,
                    onCheckedChange = onNotifyDailyChange,
                    colors = SwitchDefaults.colors(checkedTrackColor = AsosColors.Brand600),
                )
            }
            HorizontalDivider(color = AsosColors.Divider)

            SettingsRow(
                icon = AsosIcons.Globe,
                label = stringResource(R.string.profile_language),
                value = stringResource(locale.languageLabel()),
                onClick = onLanguage,
            )
            HorizontalDivider(color = AsosColors.Divider)

            SettingsRow(
                icon = AsosIcons.DocumentLines,
                label = stringResource(R.string.profile_text_size),
                value = stringResource(textSize.label()),
                onClick = onTextSize,
            )
            HorizontalDivider(color = AsosColors.Divider)

            SettingsRow(
                icon = AsosIcons.ShieldCheck,
                label = stringResource(R.string.profile_privacy),
                onClick = onPrivacy,
            )
            HorizontalDivider(color = AsosColors.Divider)

            SettingsRow(
                icon = AsosIcons.Logout,
                label = stringResource(R.string.profile_logout),
                onClick = onLogout,
                isDestructive = true,
            )
        }
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    value: String? = null,
    isDestructive: Boolean = false,
) {
    Surface(onClick = onClick, color = AsosColors.Surface) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AsosSpacing.l, vertical = AsosSpacing.mm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AsosSpacing.m),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isDestructive) AsosColors.Error else AsosColors.Text2,
                modifier = Modifier.size(20.dp),
            )
            Text(
                text = label,
                style = AsosTextStyles.RowTitleRegular,
                color = if (isDestructive) AsosColors.Error else AsosColors.Navy,
                modifier = Modifier.weight(1f),
            )
            value?.let {
                Text(text = it, style = AsosTextStyles.Meta, color = AsosColors.Text2)
            }
            if (!isDestructive) {
                Icon(
                    imageVector = AsosIcons.ChevronRight,
                    contentDescription = null,
                    tint = AsosColors.SoftGray,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
    }
}

internal fun String?.languageLabel(): Int = when (this) {
    "uz-Cyrl" -> R.string.profile_language_uz_cyrl
    "ru" -> R.string.profile_language_ru
    "en" -> R.string.profile_language_en
    else -> R.string.profile_language_uz_latn
}

internal fun ReaderTextSize.label(): Int = when (this) {
    ReaderTextSize.SMALL -> R.string.profile_text_size_small
    ReaderTextSize.MEDIUM -> R.string.profile_text_size_medium
    ReaderTextSize.LARGE -> R.string.profile_text_size_large
    ReaderTextSize.XLARGE -> R.string.profile_text_size_xlarge
}
