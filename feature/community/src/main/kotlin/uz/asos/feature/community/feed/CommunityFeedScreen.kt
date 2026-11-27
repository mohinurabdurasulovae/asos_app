/*
 * CommunityFeedScreen.kt
 * Module: :feature:community   ·   Work package: WP-09
 * Screen: Hamjamiyat (design artboard: Hamjamiyat.dc.html)
 *
 * Layout (top to bottom, 16 dp gutters):
 *   CommunityTopBar ("Hamjamiyat" · search · saved)
 *   CommunityTopicChips
 *   AskQuestionComposer
 *   FeedSortTabs
 *   PostCard per discussion
 *   AskQuestionButton floating bottom-right, AskQuestionSheet behind both entry points
 *
 * Hamjamiyat is a tab, so there is no back button and the app module draws the bottom bar.
 */
package uz.asos.feature.community.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.ui.preview.AsosPreviews
import uz.asos.core.ui.state.EmptyState
import uz.asos.core.ui.state.ErrorState
import uz.asos.core.ui.state.LoadingState
import uz.asos.core.model.community.CommunityTopic
import uz.asos.core.model.community.Post
import uz.asos.core.model.community.PostStatus
import uz.asos.core.model.community.VoteState
import uz.asos.core.model.community.Author
import uz.asos.feature.community.R
import uz.asos.feature.community.feed.components.AskQuestionButton
import uz.asos.feature.community.feed.components.AskQuestionComposer
import uz.asos.feature.community.feed.components.AskQuestionSheet
import uz.asos.feature.community.feed.components.CommunityTopBar
import uz.asos.feature.community.feed.components.CommunityTopicChips
import uz.asos.feature.community.feed.components.FeedSortTabs
import uz.asos.feature.community.feed.components.PostCard

@Composable
internal fun CommunityFeedRoute(
    onOpenPost: (postId: Int) -> Unit,
    onOpenSearch: () -> Unit,
    onOpenSaved: () -> Unit,
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CommunityFeedViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Back from a discussion: pick up new comments and vote counts.
    LifecycleResumeEffect(Unit) {
        viewModel.onAction(CommunityFeedAction.OnResume)
        onPauseOrDispose { }
    }

    CommunityFeedScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                is CommunityFeedAction.OnPostClick -> onOpenPost(action.postId)
                CommunityFeedAction.OnSearchClick -> onOpenSearch()
                CommunityFeedAction.OnSavedClick -> onOpenSaved()
                CommunityFeedAction.OnSignInClick -> onSignIn()
                else -> viewModel.onAction(action)
            }
        },
        modifier = modifier,
    )
}

@Composable
internal fun CommunityFeedScreen(
    uiState: CommunityFeedUiState,
    onAction: (CommunityFeedAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AsosColors.Background),
    ) {
        when {
            uiState.isLoading && uiState.isEmpty ->
                LoadingState(
                    modifier = Modifier.statusBarsPadding(),
                    blocks = listOf(34.dp, 76.dp, 56.dp, 40.dp, 180.dp, 150.dp),
                )

            uiState.errorMessage != null && uiState.isEmpty ->
                ErrorState(
                    title = stringResource(R.string.community_load_error_title),
                    message = uiState.errorMessage,
                    onRetry = { onAction(CommunityFeedAction.OnRetry) },
                    modifier = Modifier.statusBarsPadding(),
                )

            else -> FeedContent(uiState = uiState, onAction = onAction)
        }

        if (!uiState.isLoading && uiState.errorMessage == null) {
            AskQuestionButton(
                onClick = { onAction(CommunityFeedAction.OnAskOpen) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(AsosSpacing.l),
            )
        }

        uiState.notice?.let { notice ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(AsosSpacing.l),
                containerColor = AsosColors.Navy,
                action = {
                    Text(
                        text = stringResource(R.string.community_notice_dismiss),
                        style = AsosTextStyles.Chip,
                        color = AsosColors.BrandOnNavy,
                        modifier = Modifier
                            .padding(AsosSpacing.s)
                            .clickable { onAction(CommunityFeedAction.OnNoticeDismiss) },
                    )
                },
            ) {
                Text(text = notice, style = AsosTextStyles.BodySmall, color = AsosColors.Surface)
            }
        }
    }

    if (uiState.isAskSheetOpen) {
        AskQuestionSheet(
            draft = uiState.ask,
            topics = uiState.topics,
            onTitleChange = { onAction(CommunityFeedAction.OnAskTitleChange(it)) },
            onBodyChange = { onAction(CommunityFeedAction.OnAskBodyChange(it)) },
            onTopicChange = { onAction(CommunityFeedAction.OnAskTopicChange(it)) },
            onAnonymousChange = { onAction(CommunityFeedAction.OnAskAnonymousChange(it)) },
            onSend = { onAction(CommunityFeedAction.OnAskSend) },
            onDismiss = { onAction(CommunityFeedAction.OnAskDismiss) },
        )
    }
}

@Composable
private fun FeedContent(
    uiState: CommunityFeedUiState,
    onAction: (CommunityFeedAction) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentPadding = PaddingValues(
            start = AsosSpacing.l,
            end = AsosSpacing.l,
            top = AsosSpacing.s,
            bottom = 96.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(AsosSpacing.m),
    ) {
        item {
            CommunityTopBar(
                onSearchClick = { onAction(CommunityFeedAction.OnSearchClick) },
                onSavedClick = { onAction(CommunityFeedAction.OnSavedClick) },
            )
        }

        if (uiState.topics.isNotEmpty()) {
            item {
                CommunityTopicChips(
                    topics = uiState.topics,
                    selected = uiState.selectedTopic,
                    onSelect = { onAction(CommunityFeedAction.OnTopicSelect(it)) },
                )
            }
        }

        item {
            AskQuestionComposer(
                initials = uiState.myInitials,
                onClick = {
                    if (uiState.isSignedIn) {
                        onAction(CommunityFeedAction.OnAskOpen)
                    } else {
                        onAction(CommunityFeedAction.OnSignInClick)
                    }
                },
            )
        }

        item {
            FeedSortTabs(
                selectedSort = uiState.sort,
                onSelect = { onAction(CommunityFeedAction.OnSortSelect(it)) },
            )
        }

        if (uiState.isEmpty) {
            item {
                EmptyState(
                    title = stringResource(R.string.community_empty_title),
                    message = stringResource(R.string.community_empty_message),
                    icon = AsosIcons.Comment,
                )
            }
        } else {
            items(uiState.posts, key = { it.id }) { post ->
                PostCard(
                    post = post,
                    onClick = { onAction(CommunityFeedAction.OnPostClick(post.id)) },
                    onVote = { vote -> onAction(CommunityFeedAction.OnVote(post.id, vote)) },
                )
            }
        }
    }
}

private val previewTopics = listOf(
    CommunityTopic("uy-joy", "Uy-joy", "pink", 2400, false),
    CommunityTopic("talim", "Ta’lim", "teal", 3120, false),
    CommunityTopic("mehnat", "Mehnat", "orange", 1870, false),
)

private val previewState = CommunityFeedUiState(
    isLoading = false,
    isSignedIn = true,
    myInitials = "AK",
    topics = previewTopics,
    posts = listOf(
        Post(
            id = 1,
            title = "Uy egasi ruxsatsiz kvartiraga kirib turibdi. Bu qonuniymi?",
            excerpt = "Ijarada yashayman. Uy egasi kalit nusxasini saqlab qolgan va men " +
                "yo‘qligimda kirib, narsalarimni ko‘rib ketyapti.",
            tags = listOf("ijara"),
            upvotes = 128,
            commentCount = 34,
            status = PostStatus.ANSWERED,
            createdAt = "2026-11-26 18:00:00",
            author = Author(null, "Anonim", null, "A", null, true, false, null, false),
            community = previewTopics.first(),
            article = null,
            bestAnswer = null,
            myVote = VoteState.UP,
            isSaved = false,
        ),
        Post(
            id = 2,
            title = "Kontrakt to‘lovi kechiksa, talabani darsga kiritmaslik mumkinmi?",
            excerpt = "To‘lovning ikkinchi qismini ikki haftaga kechiktirdim.",
            tags = listOf("talim"),
            upvotes = 76,
            commentCount = 19,
            status = PostStatus.AWAITING_LAWYER,
            createdAt = "2026-11-26 15:00:00",
            author = Author(9, "Jasur T.", "jasur_t", "JT", null, false, false, null, false),
            community = previewTopics[1],
            article = null,
            bestAnswer = null,
            myVote = VoteState.NONE,
            isSaved = false,
        ),
    ),
)

@AsosPreviews
@Composable
private fun CommunityFeedScreenPreview() {
    AsosTheme { CommunityFeedScreen(uiState = previewState, onAction = {}) }
}

@AsosPreviews
@Composable
private fun CommunityFeedScreenEmptyPreview() {
    AsosTheme {
        CommunityFeedScreen(
            uiState = previewState.copy(posts = emptyList(), isSignedIn = false, myInitials = null),
            onAction = {},
        )
    }
}
