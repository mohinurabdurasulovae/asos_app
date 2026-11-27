/*
 * PostDetailScreen.kt
 * Module: :feature:community   ·   Work package: WP-09
 * Screen: Muhokama (design artboard: Post.dc.html)
 *
 * Layout (top to bottom, 16 dp gutters):
 *   PostTopBar (back · topic · Qo'shilish)
 *   PostHeader (author · title · body · tags)
 *   PostActionBar (votes · comments · save · share)
 *   "N izoh"
 *   LawyerAnswerCard (the best answer, lifted out of the thread)
 *   ReplyThread per remaining comment
 *   ReplyComposer pinned to the bottom
 *
 * PostDetailRoute collects the ViewModel; PostDetailScreen(uiState, onAction) is stateless.
 */
package uz.asos.feature.community.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.ui.preview.AsosPreviews
import uz.asos.core.ui.state.ErrorState
import uz.asos.core.ui.state.LoadingState
import uz.asos.feature.community.R
import uz.asos.feature.community.post.components.LawyerAnswerCard
import uz.asos.feature.community.post.components.PostActionBar
import uz.asos.feature.community.post.components.PostHeader
import uz.asos.feature.community.post.components.PostTopBar
import uz.asos.feature.community.post.components.ReplyComposer
import uz.asos.feature.community.post.components.ReplyThread

@Composable
internal fun PostDetailRoute(
    onBack: () -> Unit,
    onOpenArticle: (number: Int) -> Unit,
    onOpenChat: (lawyerId: Int) -> Unit,
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PostDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PostDetailScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                PostDetailAction.OnBackClick -> onBack()
                is PostDetailAction.OnArticleClick -> onOpenArticle(action.number)
                is PostDetailAction.OnWriteToLawyer -> onOpenChat(action.lawyerId)
                PostDetailAction.OnSignInClick -> onSignIn()
                else -> viewModel.onAction(action)
            }
        },
        modifier = modifier,
    )
}

@Composable
internal fun PostDetailScreen(
    uiState: PostDetailUiState,
    onAction: (PostDetailAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AsosColors.Background),
    ) {
        val detail = uiState.post
        when {
            uiState.isLoading && detail == null ->
                LoadingState(
                    modifier = Modifier.statusBarsPadding(),
                    blocks = listOf(40.dp, 150.dp, 40.dp, 200.dp, 120.dp),
                )

            uiState.errorMessage != null && detail == null ->
                ErrorState(
                    title = stringResource(R.string.community_post_error_title),
                    message = uiState.errorMessage,
                    onRetry = { onAction(PostDetailAction.OnRetry) },
                    modifier = Modifier.statusBarsPadding(),
                )

            detail != null -> Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .statusBarsPadding(),
                    contentPadding = PaddingValues(
                        start = AsosSpacing.l,
                        end = AsosSpacing.l,
                        bottom = AsosSpacing.l,
                    ),
                    verticalArrangement = Arrangement.spacedBy(AsosSpacing.m),
                ) {
                    item {
                        PostTopBar(
                            post = detail.post,
                            onBack = { onAction(PostDetailAction.OnBackClick) },
                            onMembershipToggle = {
                                onAction(PostDetailAction.OnMembershipToggle)
                            },
                        )
                    }

                    item {
                        PostHeader(
                            post = detail.post,
                            body = detail.body,
                            onArticleClick = { onAction(PostDetailAction.OnArticleClick(it)) },
                        )
                    }

                    item {
                        PostActionBar(
                            upvotes = detail.post.upvotes,
                            myVote = detail.post.myVote,
                            commentCount = uiState.commentCount,
                            isSaved = detail.post.isSaved,
                            onVote = { onAction(PostDetailAction.OnPostVote(it)) },
                            onSaveToggle = { onAction(PostDetailAction.OnSaveToggle) },
                            onShare = { onAction(PostDetailAction.OnShareClick) },
                        )
                    }

                    item { HorizontalDivider(color = AsosColors.Divider) }

                    item {
                        Text(
                            text = stringResource(
                                R.string.community_comments_count,
                                uiState.commentCount,
                            ),
                            style = AsosTextStyles.RowTitle,
                        )
                    }

                    uiState.bestAnswer?.let { best ->
                        item(key = "best-${best.id}") {
                            LawyerAnswerCard(
                                comment = best,
                                disclaimer = detail.disclaimer,
                                onVote = { vote ->
                                    onAction(PostDetailAction.OnCommentVote(best.id, vote))
                                },
                                onReply = {
                                    onAction(
                                        PostDetailAction.OnReplyTo(best.id, best.author.name),
                                    )
                                },
                                onArticleClick = {
                                    onAction(PostDetailAction.OnArticleClick(it))
                                },
                                onWriteToLawyer = {
                                    onAction(PostDetailAction.OnWriteToLawyer(it))
                                },
                            )
                        }
                    }

                    items(uiState.comments, key = { it.id }) { comment ->
                        ReplyThread(
                            comment = comment,
                            isPostAuthor = comment.author.id != null &&
                                comment.author.id == detail.post.author.id,
                            onVote = { id, vote ->
                                onAction(PostDetailAction.OnCommentVote(id, vote))
                            },
                            onReply = { id, name ->
                                onAction(PostDetailAction.OnReplyTo(id, name))
                            },
                        )
                    }
                }

                ReplyComposer(
                    draft = uiState.reply,
                    onBodyChange = { onAction(PostDetailAction.OnReplyChange(it)) },
                    onAnonymousChange = {
                        onAction(PostDetailAction.OnReplyAnonymousChange(it))
                    },
                    onSend = { onAction(PostDetailAction.OnReplySend) },
                    onCancelReply = { onAction(PostDetailAction.OnReplyTo(null, null)) },
                )
            }
        }

        uiState.notice?.let { notice ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 96.dp, start = AsosSpacing.l, end = AsosSpacing.l),
                containerColor = AsosColors.Navy,
            ) {
                Text(text = notice, style = AsosTextStyles.BodySmall, color = AsosColors.Surface)
            }
        }
    }
}

@AsosPreviews
@Composable
private fun PostDetailScreenPreview() {
    AsosTheme {
        PostDetailScreen(
            uiState = PostDetailUiState(isLoading = true),
            onAction = {},
        )
    }
}
