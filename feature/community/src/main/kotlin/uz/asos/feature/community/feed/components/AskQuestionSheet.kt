/*
 * AskQuestionSheet.kt
 * Module: :feature:community   ·   Work package: WP-09
 * Screen: Hamjamiyat (design artboard: Hamjamiyat.dc.html)
 *
 * "Savol berish": title, body, topic chips and the anonymous switch.
 */
package uz.asos.feature.community.feed.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.AsosButton
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.model.community.CommunityTopic
import uz.asos.feature.community.R
import uz.asos.feature.community.feed.AskDraft

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AskQuestionSheet(
    draft: AskDraft,
    topics: List<CommunityTopic>,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onTopicChange: (String?) -> Unit,
    onAnonymousChange: (Boolean) -> Unit,
    onSend: () -> Unit,
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
            verticalArrangement = Arrangement.spacedBy(AsosSpacing.m),
        ) {
            Text(
                text = stringResource(R.string.community_ask_title),
                style = AsosTextStyles.SectionTitle,
            )

            OutlinedTextField(
                value = draft.title,
                onValueChange = onTitleChange,
                enabled = !draft.isSending,
                textStyle = AsosTextStyles.Body,
                placeholder = {
                    Text(
                        text = stringResource(R.string.community_ask_title_hint),
                        style = AsosTextStyles.BodySmall,
                        color = AsosColors.SoftGray,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = AsosShapes.R14,
                colors = sheetFieldColors(),
            )

            OutlinedTextField(
                value = draft.body,
                onValueChange = onBodyChange,
                enabled = !draft.isSending,
                textStyle = AsosTextStyles.BodySmall,
                placeholder = {
                    Text(
                        text = stringResource(R.string.community_ask_body_hint),
                        style = AsosTextStyles.BodySmall,
                        color = AsosColors.SoftGray,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                shape = AsosShapes.R14,
                colors = sheetFieldColors(),
            )

            Text(
                text = stringResource(R.string.community_ask_topic),
                style = AsosTextStyles.Subtitle,
                color = AsosColors.Text2,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(AsosSpacing.s),
            ) {
                topics.forEach { topic ->
                    val isSelected = draft.topicSlug == topic.slug
                    Surface(
                        onClick = { onTopicChange(if (isSelected) null else topic.slug) },
                        shape = AsosShapes.Pill,
                        color = if (isSelected) AsosColors.Navy else AsosColors.SurfaceMuted,
                    ) {
                        Text(
                            text = topic.title,
                            style = AsosTextStyles.Chip,
                            color = if (isSelected) AsosColors.Surface else AsosColors.Navy,
                            modifier = Modifier.padding(
                                horizontal = AsosSpacing.m,
                                vertical = AsosSpacing.s,
                            ),
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.community_ask_anonymous),
                    style = AsosTextStyles.RowTitleRegular,
                )
                Switch(
                    checked = draft.isAnonymous,
                    onCheckedChange = onAnonymousChange,
                    enabled = !draft.isSending,
                    colors = SwitchDefaults.colors(checkedTrackColor = AsosColors.Brand600),
                )
            }

            AsosButton(
                text = stringResource(R.string.community_ask_send),
                onClick = onSend,
                enabled = draft.canSend,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun sheetFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = AsosColors.Surface,
    unfocusedContainerColor = AsosColors.Surface,
    disabledContainerColor = AsosColors.Surface,
    focusedBorderColor = AsosColors.Brand600,
    unfocusedBorderColor = AsosColors.Border,
    disabledBorderColor = AsosColors.Border,
    disabledTextColor = AsosColors.Navy,
)
