package uz.asos.feature.cases.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import uz.asos.core.designsystem.component.AsosButton
import uz.asos.core.designsystem.component.AsosButtonVariant
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.feature.cases.R
import uz.asos.feature.cases.data.model.ArticleOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ArticlePreviewSheet(
    article: ArticleOption,
    onOpenReader: (number: Int) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = AsosColors.Surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AsosSpacing.xl)
                .padding(bottom = AsosSpacing.xxl),
            verticalArrangement = Arrangement.spacedBy(AsosSpacing.mm),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AsosSpacing.sm),
            ) {
                Surface(
                    shape = AsosShapes.R10,
                    color = AsosColors.Navy,
                ) {
                    Text(
                        text = stringResource(R.string.cases_article_number, article.number),
                        style = AsosTextStyles.SerifRoman,
                        color = AsosColors.Surface,
                        modifier = Modifier.padding(
                            horizontal = AsosSpacing.m,
                            vertical = AsosSpacing.xs,
                        ),
                    )
                }
                Text(
                    text = article.title,
                    style = AsosTextStyles.CardTitle,
                )
            }

            Text(
                text = article.officialText,
                style = AsosTextStyles.LegalBody,
            )

            Surface(
                shape = AsosShapes.R14,
                color = AsosColors.Brand50,
            ) {
                Column(
                    modifier = Modifier.padding(AsosSpacing.mm),
                    verticalArrangement = Arrangement.spacedBy(AsosSpacing.xxs),
                ) {
                    Text(
                        text = stringResource(R.string.cases_article_plain),
                        style = AsosTextStyles.Tag,
                        color = AsosColors.Brand600,
                    )
                    Text(
                        text = article.simpleExplanation,
                        style = AsosTextStyles.BodySmall,
                    )
                }
            }

            AsosButton(
                text = stringResource(R.string.cases_article_open_reader),
                onClick = { onOpenReader(article.number) },
                variant = AsosButtonVariant.Secondary,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
