package uz.asos.feature.dictionary.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.component.AsosCard
import uz.asos.core.designsystem.component.AsosCardStyle
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.model.dictionary.TermSummary
import uz.asos.feature.dictionary.R

@Composable
internal fun TermListItem(
    term: TermSummary,
    onClick: () -> Unit,
    onSaveToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AsosCard(
        modifier = modifier.fillMaxWidth(),
        shape = AsosShapes.R18,
        style = AsosCardStyle.Outlined,
        borderColor = AsosColors.BorderSoft,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AsosSpacing.l, vertical = AsosSpacing.mm),
            horizontalArrangement = Arrangement.spacedBy(AsosSpacing.m),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(AsosSpacing.xxs),
            ) {
                Text(
                    text = term.term,
                    style = AsosTextStyles.RowTitle.copy(fontSize = 15.5.sp),
                )
                term.shortDefinition?.takeIf { it.isNotBlank() }?.let { definition ->
                    Text(
                        text = definition,
                        style = AsosTextStyles.Meta.copy(fontSize = 13.sp, lineHeight = 18.9.sp),
                        color = AsosColors.Text2,
                    )
                }
                if (term.articleNumbers.isNotEmpty()) {
                    Text(
                        text = pluralStringResource(
                            R.plurals.dictionary_article_list,
                            term.articleNumbers.size,
                            term.articleNumbers.joinToString(" · "),
                        ),
                        style = AsosTextStyles.Tag,
                        color = AsosColors.Brand600,
                    )
                }
            }

            IconButton(onClick = onSaveToggle, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = if (term.isSaved) AsosIcons.BookmarkFilled else AsosIcons.Bookmark,
                    contentDescription = stringResource(
                        if (term.isSaved) R.string.dictionary_saved else R.string.dictionary_save,
                    ),
                    tint = if (term.isSaved) AsosColors.Brand500 else AsosColors.SoftGray,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
    }
}
