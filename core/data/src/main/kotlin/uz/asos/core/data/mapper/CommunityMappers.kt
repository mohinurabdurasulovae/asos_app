package uz.asos.core.data.mapper

import uz.asos.core.model.community.ArticleRef
import uz.asos.core.model.community.Author
import uz.asos.core.model.community.BestAnswerPreview
import uz.asos.core.model.community.Comment
import uz.asos.core.model.community.CommunityTopic
import uz.asos.core.model.community.LawyerBadge
import uz.asos.core.model.community.Post
import uz.asos.core.model.community.PostDetail
import uz.asos.core.model.community.PostStatus
import uz.asos.core.model.community.PostsPage
import uz.asos.core.model.community.VoteState
import uz.asos.core.network.dto.ArticleRefDto
import uz.asos.core.network.dto.AuthorDto
import uz.asos.core.network.dto.BestAnswerDto
import uz.asos.core.network.dto.CommentDto
import uz.asos.core.network.dto.CommunityDto
import uz.asos.core.network.dto.LawyerBadgeDto
import uz.asos.core.network.dto.PostBriefDto
import uz.asos.core.network.dto.PostDto
import uz.asos.core.network.dto.PostsResponseDto

internal fun PostsResponseDto.asModel(communities: List<CommunityTopic> = emptyList()) = PostsPage(
    posts = items.map { it.asModel() },
    communities = communities,
    page = meta?.page ?: 1,
    total = meta?.total ?: items.size,
    hasMore = meta?.hasMore ?: false,
)

internal fun PostBriefDto.asModel() = Post(
    id = id,
    title = title,
    excerpt = excerpt,
    tags = tags,
    upvotes = upvotes,
    commentCount = commentCount,
    status = PostStatus.fromApi(status),
    createdAt = createdAt,
    author = author.asModel(),
    community = community?.asModel(),
    article = article?.asCommunityArticle(),
    bestAnswer = bestAnswer?.asModel(),
    myVote = VoteState.fromApi(myVote),
    isSaved = isSaved,
)

internal fun PostDto.asModel() = PostDetail(
    post = Post(
        id = id,
        title = title,
        excerpt = excerpt,
        tags = tags,
        upvotes = upvotes,
        commentCount = commentCount,
        status = PostStatus.fromApi(status),
        createdAt = createdAt,
        author = author.asModel(),
        community = community?.asModel(),
        article = article?.asCommunityArticle(),
        bestAnswer = bestAnswer?.asModel(),
        myVote = VoteState.fromApi(myVote),
        isSaved = isSaved,
    ),
    body = body,
    isMine = isMine,
    disclaimer = disclaimer?.takeIf { it.isNotBlank() },
    comments = comments.map { it.asModel() },
)

internal fun CommentDto.asModel(): Comment = Comment(
    id = id,
    parentId = parentId,
    body = body,
    isDeleted = isDeleted,
    upvotes = upvotes,
    isBest = isBest,
    isLawyer = isLawyer,
    lawyer = lawyer?.asModel(),
    replyCount = replyCount,
    createdAt = createdAt,
    author = author.asModel(),
    article = article?.asCommunityArticle(),
    isMine = isMine,
    replies = replies.map { it.asModel() },
)

internal fun AuthorDto.asModel() = Author(
    id = id,
    name = name,
    username = username?.takeIf { it.isNotBlank() },
    initials = initials,
    avatarUrl = avatarUrl?.takeIf { it.isNotBlank() },
    isAnonymous = isAnonymous,
    isLawyer = isLawyer,
    lawyerId = lawyerId,
    isVerified = isVerified,
)

internal fun CommunityDto.asModel() = CommunityTopic(
    slug = slug,
    title = title,
    tone = tone?.takeIf { it.isNotBlank() },
    memberCount = memberCount,
    isMember = isMember,
)

internal fun BestAnswerDto.asModel() = BestAnswerPreview(
    id = id,
    excerpt = excerpt,
    isLawyer = isLawyer,
    authorName = authorName,
)

internal fun LawyerBadgeDto.asModel() = LawyerBadge(
    specialties = specialties,
    years = years,
)

internal fun ArticleRefDto.asCommunityArticle() = ArticleRef(
    number = number,
    title = title,
    slug = slug,
)
