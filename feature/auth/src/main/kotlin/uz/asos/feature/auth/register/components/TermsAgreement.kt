package uz.asos.feature.auth.register.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.auth.R

private val LinkSpan = SpanStyle(color = AsosColors.Brand600, fontWeight = FontWeight.Bold)

@Composable
internal fun TermsAgreement(
    checked: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val terms = stringResource(R.string.auth_terms_link)
    val middle = stringResource(R.string.auth_terms_middle)
    val privacy = stringResource(R.string.auth_privacy_link)
    val suffix = stringResource(R.string.auth_register_terms_suffix)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = AsosSpacing.minTouch)
            .clip(AsosShapes.R12)
            .toggleable(value = checked, enabled = enabled, role = Role.Checkbox, onValueChange = { onToggle() }),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        CheckMark(checked = checked)
        Text(
            text = buildAnnotatedString {
                withStyle(LinkSpan) { append(terms) }
                append(middle)
                withStyle(LinkSpan) { append(privacy) }
                append(suffix)
            },
            style = AsosTextStyles.BodySmall.copy(color = AsosColors.Text2),
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun CheckMark(checked: Boolean) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .background(if (checked) AsosColors.Brand600 else AsosColors.Surface, AsosShapes.R7)
            .border(1.5.dp, if (checked) AsosColors.Brand600 else AsosColors.Outline, AsosShapes.R7),
        contentAlignment = Alignment.Center,
    ) {
        if (checked) {
            Icon(AsosIcons.Check, contentDescription = null, tint = AsosColors.Surface, modifier = Modifier.size(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TermsAgreementPreview() {
    AsosTheme {
        TermsAgreement(checked = true, onToggle = {}, modifier = Modifier.padding(24.dp))
    }
}
