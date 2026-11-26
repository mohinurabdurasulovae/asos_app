/*
 * PostDetailScreen.kt
 * Module: :feature:community   ·   Work package: WP-09
 * Screen: Muhokama (design artboard: Post.dc.html)
 *
 * Composable UI. Discussion thread: verified lawyer answer, nested replies.
 * PostDetailRoute(viewModel) collects UiState and forwards actions; PostDetailScreen(uiState,
 * onAction) is stateless and previewable.
 *
 * To include:
 *  - PostHeader
 *  - PostActionBar
 *  - LawyerAnswerCard
 *  - ReplyThread
 *  - ReplyComposer
 *
 * PLACEHOLDER: not implemented yet.
 */

package uz.asos.feature.community.post

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uz.asos.core.designsystem.component.AsosCard
import uz.asos.core.designsystem.component.AsosTag
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.designsystem.theme.AsosTone
import uz.asos.core.designsystem.theme.AsosTones
import uz.asos.feature.community.R

@Composable
fun PostDetailRoute(
    postId: Int,
    onBack: () -> Unit,
    onOpenCommunity: () -> Unit,
    onOpenChat: (Int) -> Unit,
    onOpenArticle: (String) -> Unit,
    viewModel: PostDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PostDetailScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                PostDetailAction.Back -> onBack()
                PostDetailAction.OpenCommunity -> onOpenCommunity()
                is PostDetailAction.OpenChat -> onOpenChat(action.lawyerId)
                is PostDetailAction.OpenArticle -> onOpenArticle(action.articleKey)
                else -> viewModel.onAction(action)
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    uiState: PostDetailUiState,
    onAction: (PostDetailAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val successState = uiState as? PostDetailUiState.Success

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AsosColors.Background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AsosColors.Surface),
                navigationIcon = {
                    IconButton(
                        onClick = { onAction(PostDetailAction.Back) },
                        modifier = Modifier.size(44.dp),
                    ) {
                        Icon(
                            imageVector = AsosIcons.ChevronLeft,
                            contentDescription = stringResource(R.string.community_back_cd),
                            tint = AsosColors.Navy,
                        )
                    }
                },
                title = {
                    Column(
                        modifier = Modifier.clickable { onAction(PostDetailAction.OpenCommunity) },
                    ) {
                        Text(
                            text = successState?.post?.communityName ?: stringResource(R.string.community_title),
                            style = MaterialTheme.typography.titleMedium,
                            color = AsosColors.Navy,
                        )
                        if (successState != null) {
                            Text(
                                text = stringResource(
                                    R.string.community_members_count,
                                    successState.post.membersCountLabel,
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                color = AsosColors.Text2,
                            )
                        }
                    }
                },
                actions = {
                    if (successState != null) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (successState.post.isJoined) AsosColors.SurfaceMuted else AsosColors.Brand50,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .heightIn(min = 44.dp)
                                .clickable { onAction(PostDetailAction.ToggleJoinCommunity) },
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(horizontal = 14.dp),
                            ) {
                                Text(
                                    text = if (successState.post.isJoined) {
                                        stringResource(R.string.community_joined)
                                    } else {
                                        stringResource(R.string.community_join)
                                    },
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (successState.post.isJoined) AsosColors.Text2 else AsosColors.Brand600,
                                )
                            }
                        }
                    }
                },
            )
        },
        bottomBar = {
            if (successState != null) {
                ReplyComposer(
                    text = successState.replyDraft,
                    isAnonymous = successState.isAnonymousReply,
                    onTextChange = { onAction(PostDetailAction.UpdateReplyText(it)) },
                    onAnonymousToggle = { onAction(PostDetailAction.ToggleAnonymous(it)) },
                    onSend = { onAction(PostDetailAction.SendReply) },
                )
            }
        },
    ) { innerPadding ->
        when (uiState) {
            PostDetailUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = AsosColors.Brand600)
                }
            }

            is PostDetailUiState.Error -> {
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
                                onClick = { onAction(PostDetailAction.Retry) },
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

            is PostDetailUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    item(key = "post_card") {
                        AsosCard(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                PostHeader(
                                    authorLabel = uiState.post.authorLabel,
                                    timeAgoLabel = uiState.post.timeAgoLabel,
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = uiState.post.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = AsosColors.Navy,
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = uiState.post.body,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = AsosColors.Text2,
                                )
                                if (uiState.post.tags.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        uiState.post.tags.forEach { tag ->
                                            AsosTag(text = tag, tone = AsosTones.Brand)
                                        }
                                    }
                                }
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = AsosColors.Divider,
                                )
                                PostActionBar(
                                    votes = uiState.post.votes,
                                    isUpvoted = uiState.post.isUpvoted,
                                    commentCount = uiState.post.commentCount,
                                    isBookmarked = uiState.post.isBookmarked,
                                    onVoteClick = { onAction(PostDetailAction.UpvotePost) },
                                    onBookmarkClick = { onAction(PostDetailAction.ToggleBookmarkPost) },
                                    onShareClick = { onAction(PostDetailAction.SharePost) },
                                )
                            }
                        }
                    }

                    item(key = "replies_header") {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = stringResource(R.string.community_comments_count, uiState.post.commentCount),
                                style = MaterialTheme.typography.titleMedium,
                                color = AsosColors.Navy,
                            )
                            Text(
                                text = stringResource(R.string.community_best_answers_sort),
                                style = MaterialTheme.typography.labelLarge,
                                color = AsosColors.Brand600,
                            )
                        }
                    }

                    items(uiState.replies, key = { it.id }) { reply ->
                        if (reply.isBestAnswer && reply.isVerifiedLawyer) {
                            LawyerAnswerCard(
                                reply = reply,
                                onLikeClick = { onAction(PostDetailAction.UpvoteReply(reply.id)) },
                                onReplyClick = { onAction(PostDetailAction.ReplyToAuthor(reply.authorName)) },
                                onOpenChatClick = { chatId -> onAction(PostDetailAction.OpenChat(chatId)) },
                                onOpenArticleClick = { key -> onAction(PostDetailAction.OpenArticle(key)) },
                            )
                        } else {
                            ReplyThread(
                                reply = reply,
                                onLikeClick = { onAction(PostDetailAction.UpvoteReply(reply.id)) },
                                onReplyClick = { onAction(PostDetailAction.ReplyToAuthor(reply.authorName)) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PostHeader(
    authorLabel: String,
    timeAgoLabel: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(AsosColors.SurfaceMuted),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = AsosIcons.Person,
                contentDescription = authorLabel,
                tint = AsosColors.SoftGray,
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = authorLabel,
                style = MaterialTheme.typography.titleMedium,
                color = AsosColors.Navy,
            )
            Text(
                text = timeAgoLabel,
                style = MaterialTheme.typography.bodySmall,
                color = AsosColors.Text2,
            )
        }
    }
}

@Composable
fun PostActionBar(
    votes: Int,
    isUpvoted: Boolean,
    commentCount: Int,
    isBookmarked: Boolean,
    onVoteClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onShareClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .heightIn(min = 44.dp)
                    .clickable { onVoteClick() },
            ) {
                Icon(
                    imageVector = AsosIcons.Upvote,
                    contentDescription = stringResource(R.string.community_upvote_cd, votes),
                    tint = if (isUpvoted) AsosColors.Brand600 else AsosColors.SoftGray,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = votes.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = AsosColors.Navy,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.heightIn(min = 44.dp),
            ) {
                Icon(
                    imageVector = AsosIcons.Comment,
                    contentDescription = stringResource(R.string.community_comments_count, commentCount),
                    tint = AsosColors.SoftGray,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.community_comments_count, commentCount),
                    style = MaterialTheme.typography.bodySmall,
                    color = AsosColors.Text2,
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            IconButton(
                onClick = onBookmarkClick,
                modifier = Modifier.size(44.dp),
            ) {
                Icon(
                    imageVector = if (isBookmarked) AsosIcons.BookmarkFilled else AsosIcons.Bookmark,
                    contentDescription = stringResource(R.string.community_save),
                    tint = if (isBookmarked) AsosColors.Brand600 else AsosColors.SoftGray,
                )
            }
            IconButton(
                onClick = onShareClick,
                modifier = Modifier.size(44.dp),
            ) {
                Icon(
                    imageVector = AsosIcons.Share,
                    contentDescription = stringResource(R.string.community_share),
                    tint = AsosColors.SoftGray,
                )
            }
        }
    }
}

@Composable
fun LawyerAnswerCard(
    reply: ReplyItemUi,
    onLikeClick: () -> Unit,
    onReplyClick: () -> Unit,
    onOpenChatClick: (Int) -> Unit,
    onOpenArticleClick: (String) -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = AsosColors.Surface,
        border = BorderStroke(1.5.dp, AsosColors.Brand500),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsosTag(
                text = stringResource(R.string.community_best_answer),
                tone = AsosTone(AsosColors.SuccessText, AsosColors.SuccessBg),
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(AsosColors.Navy),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = reply.initials,
                        style = MaterialTheme.typography.labelLarge,
                        color = AsosColors.Surface,
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = reply.authorName,
                            style = MaterialTheme.typography.titleMedium,
                            color = AsosColors.Navy,
                        )
                        if (reply.roleLabel != null) {
                            Spacer(modifier = Modifier.width(6.dp))
                            AsosTag(
                                text = reply.roleLabel,
                                tone = AsosTones.Brand,
                            )
                        }
                    }
                    Text(
                        text = reply.metaLabel,
                        style = MaterialTheme.typography.bodySmall,
                        color = AsosColors.Text2,
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = reply.content,
                style = MaterialTheme.typography.bodyMedium,
                color = AsosColors.Navy,
            )

            if (reply.articleNumber != null && reply.articleTitle != null && reply.articleKey != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AsosColors.Navy,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .clickable { onOpenArticleClick(reply.articleKey) },
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AsosColors.Brand500,
                            modifier = Modifier.size(36.dp),
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = reply.articleNumber,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = AsosColors.Surface,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${reply.articleNumber}${stringResource(R.string.community_article_suffix)}",
                                style = MaterialTheme.typography.labelLarge,
                                color = AsosColors.Surface,
                            )
                            Text(
                                text = reply.articleTitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = AsosColors.TextOnNavy,
                            )
                        }
                        Icon(
                            imageVector = AsosIcons.ChevronRight,
                            contentDescription = stringResource(
                                R.string.community_article_card_cd,
                                reply.articleNumber,
                                reply.articleTitle,
                            ),
                            tint = AsosColors.Surface,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.community_legal_disclaimer),
                style = MaterialTheme.typography.bodySmall,
                color = AsosColors.Text2,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .heightIn(min = 44.dp)
                            .clickable { onLikeClick() },
                    ) {
                        Icon(
                            imageVector = AsosIcons.Upvote,
                            contentDescription = stringResource(R.string.community_upvote_cd, reply.likes),
                            tint = AsosColors.Brand600,
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = reply.likes.toString(),
                            style = MaterialTheme.typography.labelLarge,
                            color = AsosColors.Navy,
                        )
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .heightIn(min = 44.dp)
                            .clickable { onReplyClick() },
                    ) {
                        Text(
                            text = stringResource(R.string.community_reply_action),
                            style = MaterialTheme.typography.labelLarge,
                            color = AsosColors.Text2,
                        )
                    }
                }

                if (reply.lawyerChatId != null) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .heightIn(min = 44.dp)
                            .clickable { onOpenChatClick(reply.lawyerChatId) },
                    ) {
                        Text(
                            text = stringResource(R.string.community_write_lawyer),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = AsosColors.Brand600,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReplyThread(
    reply: ReplyItemUi,
    onLikeClick: () -> Unit,
    onReplyClick: () -> Unit,
) {
    AsosCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (reply.isVerifiedLawyer) AsosColors.Navy else AsosColors.Brand50),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = reply.initials,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (reply.isVerifiedLawyer) AsosColors.Surface else AsosColors.Brand600,
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = reply.authorName,
                            style = MaterialTheme.typography.titleMedium,
                            color = AsosColors.Navy,
                        )
                        if (reply.roleLabel != null) {
                            Spacer(modifier = Modifier.width(6.dp))
                            AsosTag(
                                text = reply.roleLabel,
                                tone = AsosTones.Brand,
                            )
                        }
                    }
                    Text(
                        text = reply.metaLabel,
                        style = MaterialTheme.typography.bodySmall,
                        color = AsosColors.Text2,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = reply.content,
                style = MaterialTheme.typography.bodyMedium,
                color = AsosColors.Navy,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .heightIn(min = 44.dp)
                        .clickable { onLikeClick() },
                ) {
                    Icon(
                        imageVector = AsosIcons.Upvote,
                        contentDescription = stringResource(R.string.community_upvote_cd, reply.likes),
                        tint = AsosColors.Brand600,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = reply.likes.toString(),
                        style = MaterialTheme.typography.labelLarge,
                        color = AsosColors.Navy,
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .heightIn(min = 44.dp)
                        .clickable { onReplyClick() },
                ) {
                    Text(
                        text = stringResource(R.string.community_reply_action),
                        style = MaterialTheme.typography.labelLarge,
                        color = AsosColors.Text2,
                    )
                }
            }
        }
    }
}

@Composable
fun ReplyComposer(
    text: String,
    isAnonymous: Boolean,
    onTextChange: (String) -> Unit,
    onAnonymousToggle: (Boolean) -> Unit,
    onSend: () -> Unit,
) {
    Surface(
        color = AsosColors.Surface,
        shadowElevation = 6.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = onTextChange,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.community_reply_placeholder),
                            style = MaterialTheme.typography.bodyMedium,
                            color = AsosColors.Text2,
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onSend,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(AsosColors.Brand600),
                ) {
                    Icon(
                        imageVector = AsosIcons.Send,
                        contentDescription = stringResource(R.string.community_send_reply_cd),
                        tint = AsosColors.Surface,
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.heightIn(min = 44.dp),
            ) {
                Checkbox(
                    checked = isAnonymous,
                    onCheckedChange = onAnonymousToggle,
                    colors = CheckboxDefaults.colors(checkedColor = AsosColors.Brand600),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.community_anonymous_reply),
                    style = MaterialTheme.typography.bodySmall,
                    color = AsosColors.Text2,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PostDetailScreenPreview() {
    AsosTheme {
        PostDetailScreen(
            uiState = PostDetailUiState.Success(
                post = PostDetailUi(
                    id = 1,
                    communityName = "Uy-joy",
                    membersCountLabel = "2,4 ming",
                    isJoined = false,
                    authorLabel = "Anonim foydalanuvchi",
                    timeAgoLabel = "2 soat oldin",
                    title = "Uy egasi ruxsatsiz kvartiraga kirib turibdi. Bu qonuniymi?",
                    body = "Ijaradagi kvartirada yashayman. Uy egasi kalit nusxasini saqlab qolgan va men yo'qligimda kirib, narsalarimni ko'rib ketyapti.",
                    tags = listOf("ijara", "uy-joy daxlsizligi", "31-modda"),
                    votes = 128,
                    isUpvoted = true,
                    commentCount = 34,
                    isBookmarked = false,
                ),
                replies = emptyList(),
            ),
            onAction = {},
        )
    }
}
