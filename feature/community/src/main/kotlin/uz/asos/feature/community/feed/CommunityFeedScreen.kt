/*
 * CommunityFeedScreen.kt
 * Module: :feature:community   ·   Work package: WP-09
 * Screen: Hamjamiyat (design artboard: Hamjamiyat.dc.html)
 *
 * Composable UI. Reddit-style Q&A feed: topic communities, votes, anonymous posting.
 * CommunityFeedRoute(viewModel) collects UiState and forwards actions;
 * CommunityFeedScreen(uiState, onAction) is stateless and previewable.
 *
 * To include:
 *  - CommunityTopBar
 *  - CommunityTopicChips
 *  - FeedSortTabs
 *  - PostCard
 *  - AskQuestionButton
 *  - AskQuestionSheet
 *
 * PLACEHOLDER: not implemented yet.
 */

package uz.asos.feature.community.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uz.asos.core.designsystem.component.AsosCard
import uz.asos.core.designsystem.component.AsosChip
import uz.asos.core.designsystem.component.AsosTag
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.designsystem.theme.AsosTone
import uz.asos.core.designsystem.theme.AsosTones
import uz.asos.feature.community.R

@Composable
fun CommunityRoute(
    onOpenPost: (Int) -> Unit,
    onOpenBookmarks: () -> Unit,
    viewModel: CommunityFeedViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CommunityFeedScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                is CommunityFeedAction.OpenPost -> onOpenPost(action.postId)
                CommunityFeedAction.OpenBookmarks -> onOpenBookmarks()
                else -> viewModel.onAction(action)
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityFeedScreen(
    uiState: CommunityFeedUiState,
    onAction: (CommunityFeedAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val successState = uiState as? CommunityFeedUiState.Success

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AsosColors.Background,
        topBar = {
            CommunityTopBar(
                topics = successState?.topics.orEmpty(),
                selectedTopicId = successState?.selectedTopicId ?: "all",
                onSelectTopic = { topicId -> onAction(CommunityFeedAction.SelectTopic(topicId)) },
                onSearchClick = { onAction(CommunityFeedAction.OpenSearch) },
                onBookmarksClick = { onAction(CommunityFeedAction.OpenBookmarks) },
                onNotificationsClick = { onAction(CommunityFeedAction.OpenNotifications) },
            )
        },
        floatingActionButton = {
            if (successState != null) {
                AskQuestionButton(
                    onClick = { onAction(CommunityFeedAction.OpenAskSheet) },
                )
            }
        },
    ) { innerPadding ->
        when (uiState) {
            CommunityFeedUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = AsosColors.Brand600)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = stringResource(R.string.community_loading),
                            style = MaterialTheme.typography.bodyMedium,
                            color = AsosColors.Text2,
                        )
                    }
                }
            }

            is CommunityFeedUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    AsosCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = stringResource(R.string.community_error_title),
                                style = MaterialTheme.typography.titleMedium,
                                color = AsosColors.Navy,
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = uiState.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = AsosColors.Text2,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { onAction(CommunityFeedAction.Retry) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AsosColors.Brand600,
                                    contentColor = AsosColors.Surface,
                                ),
                                modifier = Modifier.heightIn(min = 44.dp),
                            ) {
                                Text(stringResource(R.string.community_retry))
                            }
                        }
                    }
                }
            }

            is CommunityFeedUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    item(key = "composer_card") {
                        QuickPromptCard(
                            onClick = { onAction(CommunityFeedAction.OpenAskSheet) },
                        )
                    }

                    item(key = "sort_tabs") {
                        FeedSortTabs(
                            options = uiState.sortOptions,
                            selectedIndex = uiState.selectedSortIndex,
                            onSelect = { index -> onAction(CommunityFeedAction.SelectSort(index)) },
                        )
                    }

                    if (uiState.posts.isEmpty()) {
                        item(key = "empty_state") {
                            AsosCard(modifier = Modifier.fillMaxWidth()) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Text(
                                        text = stringResource(R.string.community_empty_title),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = AsosColors.Navy,
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = stringResource(R.string.community_empty_subtitle),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = AsosColors.Text2,
                                    )
                                }
                            }
                        }
                    } else {
                        items(uiState.posts, key = { it.id }) { post ->
                            PostCard(
                                post = post,
                                onCardClick = { onAction(CommunityFeedAction.OpenPost(post.id)) },
                                onVoteClick = { onAction(CommunityFeedAction.UpvotePost(post.id)) },
                                onBookmarkClick = { onAction(CommunityFeedAction.ToggleBookmark(post.id)) },
                            )
                        }
                    }

                    item(key = "bottom_spacer") {
                        Spacer(modifier = Modifier.height(72.dp))
                    }
                }

                if (uiState.askSheetVisible) {
                    ModalBottomSheet(
                        onDismissRequest = { onAction(CommunityFeedAction.DismissAskSheet) },
                        containerColor = AsosColors.Surface,
                    ) {
                        AskQuestionSheet(
                            topics = uiState.topics.filter { it.id != "all" },
                            draft = uiState.draft,
                            onTopicSelected = { id -> onAction(CommunityFeedAction.UpdateDraftTopic(id)) },
                            onTitleChange = { value -> onAction(CommunityFeedAction.UpdateDraftTitle(value)) },
                            onBodyChange = { value -> onAction(CommunityFeedAction.UpdateDraftBody(value)) },
                            onAnonymousToggle = { checked -> onAction(CommunityFeedAction.ToggleDraftAnonymous(checked)) },
                            onDismiss = { onAction(CommunityFeedAction.DismissAskSheet) },
                            onSubmit = { onAction(CommunityFeedAction.SubmitQuestion) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CommunityTopBar(
    topics: List<CommunityTopicUi>,
    selectedTopicId: String,
    onSelectTopic: (String) -> Unit,
    onSearchClick: () -> Unit,
    onBookmarksClick: () -> Unit,
    onNotificationsClick: () -> Unit,
) {
    Surface(
        color = AsosColors.Surface,
        shadowElevation = 1.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 10.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.community_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = AsosColors.Navy,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = onSearchClick,
                        modifier = Modifier.size(44.dp),
                    ) {
                        Icon(
                            imageVector = AsosIcons.Search,
                            contentDescription = stringResource(R.string.community_search_cd),
                            tint = AsosColors.Navy,
                        )
                    }
                    IconButton(
                        onClick = onBookmarksClick,
                        modifier = Modifier.size(44.dp),
                    ) {
                        Icon(
                            imageVector = AsosIcons.Bookmark,
                            contentDescription = stringResource(R.string.community_bookmarks_cd),
                            tint = AsosColors.Navy,
                        )
                    }
                    IconButton(
                        onClick = onNotificationsClick,
                        modifier = Modifier.size(44.dp),
                    ) {
                        Icon(
                            imageVector = AsosIcons.Bell,
                            contentDescription = stringResource(R.string.community_notifications_cd),
                            tint = AsosColors.Navy,
                        )
                    }
                }
            }

            if (topics.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                CommunityTopicChips(
                    topics = topics,
                    selectedTopicId = selectedTopicId,
                    onSelectTopic = onSelectTopic,
                )
            }
        }
    }
}

@Composable
fun CommunityTopicChips(
    topics: List<CommunityTopicUi>,
    selectedTopicId: String,
    onSelectTopic: (String) -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(topics, key = { it.id }) { topic ->
            val selected = topic.id == selectedTopicId
            AsosChip(
                label = topic.label,
                selected = selected,
                onClick = { onSelectTopic(topic.id) },
                modifier = Modifier.heightIn(min = 44.dp),
            )
        }
    }
}

@Composable
fun FeedSortTabs(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        options.forEachIndexed { index, label ->
            val active = index == selectedIndex
            Column(
                modifier = Modifier
                    .heightIn(min = 44.dp)
                    .clickable { onSelect(index) }
                    .padding(horizontal = 2.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
                    color = if (active) AsosColors.Brand600 else AsosColors.Text2,
                )
                if (active) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height(2.dp)
                            .background(AsosColors.Brand600),
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickPromptCard(
    onClick: () -> Unit,
) {
    AsosCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(AsosColors.Brand50),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "AK",
                    style = MaterialTheme.typography.labelLarge,
                    color = AsosColors.Brand600,
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.community_ask_placeholder),
                style = MaterialTheme.typography.bodyMedium,
                color = AsosColors.Text2,
                modifier = Modifier.weight(1f),
            )
            AsosTag(
                text = stringResource(R.string.community_anonymous),
                tone = AsosTone(AsosColors.Text2, AsosColors.SurfaceMuted),
            )
        }
    }
}

@Composable
fun PostCard(
    post: CommunityPostUi,
    onCardClick: () -> Unit,
    onVoteClick: () -> Unit,
    onBookmarkClick: () -> Unit,
) {
    AsosCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f),
                ) {
                    AsosTag(
                        text = post.categoryLabel,
                        tone = AsosTones.Brand,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${post.authorLabel} · ${post.timeAgoLabel}",
                        style = MaterialTheme.typography.bodySmall,
                        color = AsosColors.Text2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (post.isUpvoted) AsosColors.Brand50 else AsosColors.SurfaceMuted,
                    modifier = Modifier
                        .heightIn(min = 44.dp)
                        .clickable { onVoteClick() }
                        .semantics {
                            contentDescription = "${post.votes}"
                        },
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = AsosIcons.Upvote,
                            contentDescription = stringResource(R.string.community_upvote_cd, post.votes),
                            tint = AsosColors.Brand600,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = post.votes.toString(),
                            style = MaterialTheme.typography.labelLarge,
                            color = AsosColors.Navy,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium,
                color = AsosColors.Navy,
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = post.bodyPreview,
                style = MaterialTheme.typography.bodyMedium,
                color = AsosColors.Text2,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )

            if (post.lawyerName != null && post.lawyerExcerpt != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AsosColors.SurfaceMuted,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = post.lawyerName,
                                style = MaterialTheme.typography.labelLarge,
                                color = AsosColors.Navy,
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            AsosTag(
                                text = stringResource(R.string.community_lawyer_badge),
                                tone = AsosTones.Brand,
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = post.lawyerExcerpt,
                            style = MaterialTheme.typography.bodySmall,
                            color = AsosColors.Text2,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.heightIn(min = 44.dp),
                    ) {
                        Icon(
                            imageVector = AsosIcons.Comment,
                            contentDescription = stringResource(R.string.community_comments_count, post.commentCount),
                            tint = AsosColors.SoftGray,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.community_comments_count, post.commentCount),
                            style = MaterialTheme.typography.bodySmall,
                            color = AsosColors.Text2,
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .heightIn(min = 44.dp)
                            .clickable { onBookmarkClick() },
                    ) {
                        Icon(
                            imageVector = if (post.isBookmarked) AsosIcons.BookmarkFilled else AsosIcons.Bookmark,
                            contentDescription = stringResource(R.string.community_save),
                            tint = if (post.isBookmarked) AsosColors.Brand600 else AsosColors.SoftGray,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.community_save),
                            style = MaterialTheme.typography.bodySmall,
                            color = AsosColors.Text2,
                        )
                    }
                }

                if (post.statusText != null) {
                    Text(
                        text = post.statusText,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (post.isLawyerAnswered) AsosColors.Brand600 else AsosColors.Text2,
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.heightIn(min = 44.dp),
                    ) {
                        Icon(
                            imageVector = AsosIcons.Share,
                            contentDescription = stringResource(R.string.community_share),
                            tint = AsosColors.SoftGray,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.community_share),
                            style = MaterialTheme.typography.bodySmall,
                            color = AsosColors.Text2,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AskQuestionButton(
    onClick: () -> Unit,
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        containerColor = AsosColors.Brand600,
        contentColor = AsosColors.Surface,
        modifier = Modifier.heightIn(min = 48.dp),
    ) {
        Icon(
            imageVector = AsosIcons.Edit,
            contentDescription = stringResource(R.string.community_ask_question),
            modifier = Modifier.size(18.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.community_ask_question),
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
fun AskQuestionSheet(
    topics: List<CommunityTopicUi>,
    draft: AskQuestionDraftUi,
    onTopicSelected: (String) -> Unit,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onAnonymousToggle: (Boolean) -> Unit,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.community_sheet_title),
            style = MaterialTheme.typography.titleLarge,
            color = AsosColors.Navy,
        )

        Text(
            text = stringResource(R.string.community_sheet_topic_label),
            style = MaterialTheme.typography.labelLarge,
            color = AsosColors.Text2,
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(topics, key = { it.id }) { topic ->
                AsosChip(
                    label = topic.label,
                    selected = topic.id == draft.selectedTopicId,
                    onClick = { onTopicSelected(topic.id) },
                )
            }
        }

        OutlinedTextField(
            value = draft.title,
            onValueChange = onTitleChange,
            label = { Text(stringResource(R.string.community_sheet_title_label)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = draft.body,
            onValueChange = onBodyChange,
            label = { Text(stringResource(R.string.community_sheet_body_label)) },
            minLines = 3,
            modifier = Modifier.fillMaxWidth(),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 44.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.community_anonymous_reply),
                style = MaterialTheme.typography.bodyMedium,
                color = AsosColors.Navy,
            )
            Switch(
                checked = draft.isAnonymous,
                onCheckedChange = onAnonymousToggle,
                colors = SwitchDefaults.colors(checkedTrackColor = AsosColors.Brand600),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 44.dp),
            ) {
                Text(stringResource(R.string.community_sheet_cancel))
            }
            Button(
                onClick = onSubmit,
                enabled = draft.title.isNotBlank() && draft.body.isNotBlank() && !draft.isSubmitting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AsosColors.Brand600,
                    contentColor = AsosColors.Surface,
                ),
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 44.dp),
            ) {
                Text(stringResource(R.string.community_sheet_submit))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CommunityFeedScreenPreview() {
    AsosTheme {
        CommunityFeedScreen(
            uiState = CommunityFeedViewModel().apply { loadFeed() }.uiState.value,
            onAction = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CommunityFeedLoadingPreview() {
    AsosTheme {
        CommunityFeedScreen(
            uiState = CommunityFeedUiState.Loading,
            onAction = {},
        )
    }
}
