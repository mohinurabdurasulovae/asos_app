/*
 * DictionarySearchField.kt
 * Module: :feature:dictionary   ·   Work package: WP-06
 * Screen: Konstitutsiyaviy lug‘at (design artboard: Lugat.dc.html)
 *
 * "Atamani qidiring" search, on top of the shared AsosSearchField.
 */
package uz.asos.feature.dictionary.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import uz.asos.core.designsystem.component.AsosSearchField
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.dictionary.R

@Composable
internal fun DictionarySearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    AsosSearchField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = stringResource(R.string.dictionary_search_placeholder),
        accessibilityLabel = stringResource(R.string.dictionary_search_label),
        elevated = true,
        modifier = modifier,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC)
@Composable
private fun DictionarySearchFieldPreview() {
    AsosTheme { DictionarySearchField(query = "", onQueryChange = {}) }
}
