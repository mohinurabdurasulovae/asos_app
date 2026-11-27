package uz.asos.core.model.community

data class Author(
    val id: Int?,
    val name: String,
    val username: String?,
    val initials: String,
    val avatarUrl: String?,
    val isAnonymous: Boolean,
    val isLawyer: Boolean,
    val lawyerId: Int?,
    val isVerified: Boolean,
)

data class CommunityTopic(
    val slug: String,
    val title: String,
    val tone: String?,
    val memberCount: Int?,
    val isMember: Boolean,
)

data class ArticleRef(
    val number: Int,
    val title: String,
    val slug: String,
)

enum class PostStatus(val apiValue: String) {
    OPEN("open"),

    AWAITING_LAWYER("awaiting_lawyer"),

    ANSWERED("answered");

    companion object {
        fun fromApi(value: String?): PostStatus =
            entries.firstOrNull { it.apiValue == value } ?: OPEN
    }
}

enum class PostSort(val apiValue: String) {
    POPULAR("popular"),
    NEW("new"),
    UNANSWERED("unanswered"),
    SUBSCRIPTIONS("subscriptions");

    companion object {
        fun fromApi(value: String?): PostSort =
            entries.firstOrNull { it.apiValue == value } ?: POPULAR
    }
}

enum class VoteState(val apiValue: Int) {
    UP(1),
    NONE(0),
    DOWN(-1);

    companion object {
        fun fromApi(value: Int?): VoteState =
            entries.firstOrNull { it.apiValue == value } ?: NONE
    }
}

data class LawyerBadge(
    val specialties: List<String>,
    val years: Int,
)

data class BestAnswerPreview(
    val id: Int,
    val excerpt: String,
    val isLawyer: Boolean,
    val authorName: String,
)

data class Post(
    val id: Int,
    val title: String,
    val excerpt: String,
    val tags: List<String>,
    val upvotes: Int,
    val commentCount: Int,
    val status: PostStatus,
    val createdAt: String,
    val author: Author,
    val community: CommunityTopic?,
    val article: ArticleRef?,
    val bestAnswer: BestAnswerPreview?,
    val myVote: VoteState,
    val isSaved: Boolean,
)

data class PostDetail(
    val post: Post,
    val body: String,
    val isMine: Boolean,
    val disclaimer: String?,
    val comments: List<Comment>,
)

data class Comment(
    val id: Int,
    val parentId: Int?,
    val body: String,
    val isDeleted: Boolean,
    val upvotes: Int,
    val isBest: Boolean,
    val isLawyer: Boolean,
    val lawyer: LawyerBadge?,
    val replyCount: Int,
    val createdAt: String,
    val author: Author,
    val article: ArticleRef?,
    val isMine: Boolean,
    val replies: List<Comment>,
)

data class PostsPage(
    val posts: List<Post>,
    val communities: List<CommunityTopic>,
    val page: Int,
    val total: Int,
    val hasMore: Boolean,
)
