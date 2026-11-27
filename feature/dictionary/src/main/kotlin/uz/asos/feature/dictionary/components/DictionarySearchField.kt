/*
 * DictionarySearchField.kt
 * Module: :feature:dictionary   ·   Work package: WP-06
 * Screen: Konstitutsiyaviy lug'at (design artboard: Lugat.dc.html)
 *
 * 52 dp white field, R16, search icon, placeholder "Atamani qidiring".
 */
package uz.asos.feature.dictionary.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.feature.dictionary.R

@Composable
internal fun DictionarySearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(AsosColors.Surface, AsosShapes.R16)
            .border(BorderStroke(1.dp, AsosColors.Border), AsosShapes.R16)
            .padding(horizontal = AsosSpacing.l),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AsosSpacing.sm),
    ) {
        Icon(
            imageVector = AsosIcons.Search,
            contentDescription = null,
            tint = AsosColors.Text2,
            modifier = Modifier.size(20.dp),
        )

        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
            if (query.isEmpty()) {
                Text(
                    text = stringResource(R.string.dictionary_search_placeholder),
                    style = AsosTextStyles.Body,
                    color = AsosColors.Text2,
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = LocalTextStyle.current.merge(AsosTextStyles.Body)
                    .copy(color = AsosColors.Navy),
                cursorBrush = SolidColor(AsosColors.Brand600),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        if (query.isNotEmpty()) {
            IconButton(
                onClick = onClear,
                modifier = Modifier.size(28.dp),
            ) {
                Icon(
                    imageVector = AsosIcons.Close,
                    contentDescription = stringResource(R.string.dictionary_search_clear),
                    tint = AsosColors.Text2,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
    }
}
