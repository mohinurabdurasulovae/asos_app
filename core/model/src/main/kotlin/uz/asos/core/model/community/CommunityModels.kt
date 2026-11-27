/*
 * CommunityModels.kt
 * Module: :core:model   ·   Work package: WP-02
 *
 * Domain models of Hamjamiyat and Muhokama, shared by every layer.
 */
package uz.asos.core.model.community

/** Who wrote a post or a comment. Anonymous authors hide their id and name. */
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

/** Topic a post belongs to: Uy-joy, Ta’lim, Mehnat, Oila, Sud. */
data class CommunityTopic(
    val slug: String,
    val title: String,
    /** Design-system tone name: brand, teal, orange, purple, pink, navy. */
    val tone: String?,
    val memberCount: Int?,
    val isMember: Boolean,
)

/** Article a post or comment cites. */
data class ArticleRef(
    val number: Int,
    val title: String,
    val slug: String,
)

/** Whether a question has been answered, shown as the footer badge. */
enum class PostStatus(val apiValue: String) {
    /** No lawyer involved yet. */
    OPEN("open"),

    /** "Yurist javobi kutilmoqda". */
    AWAITING_LAWYER("awaiting_lawyer"),

    /** "Advokat javob berdi". */
    ANSWERED("answered");

    companion object {
        fun fromApi(value: String?): PostStatus =
            entries.firstOrNull { it.apiValue == value } ?: OPEN
    }
}

/** Feed order. The values match `postSorts` in GET /v1/config. */
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

/** How the signed-in user voted: +1, 0 or -1. */
enum class VoteState(val apiValue: Int) {
    UP(1),
    NONE(0),
    DOWN(-1);

    companion object {
        fun fromApi(value: Int?): VoteState =
            entries.firstOrNull { it.apiValue == value } ?: NONE
    }
}

/** The lawyer's credentials shown on a verified answer. */
data class LawyerBadge(
    val specialties: List<String>,
    val years: Int,
)

/** Short form of the best answer, shown inside a feed card. */
data class BestAnswerPreview(
    val id: Int,
    val excerpt: String,
    val isLawyer: Boolean,
    val authorName: String,
)

/** A post as the Hamjamiyat feed shows it: GET /v1/posts. */
data class Post(
    val id: Int,
    val title: String,
    val excerpt: String,
    val tags: List<String>,
    val upvotes: Int,
    val commentCount: Int,
    val status: PostStatus,
    /** UTC, "YYYY-MM-DD HH:MM:SS". */
    val createdAt: String,
    val author: Author,
    val community: CommunityTopic?,
    val article: ArticleRef?,
    val bestAnswer: BestAnswerPreview?,
    val myVote: VoteState,
    val isSaved: Boolean,
)

/** The whole discussion: GET /v1/posts/{id}. */
data class PostDetail(
    val post: Post,
    val body: String,
    val isMine: Boolean,
    /** Shown under the lawyer's answer. */
    val disclaimer: String?,
    /** Top-level comments; each carries one level of [Comment.replies]. */
    val comments: List<Comment>,
)

/** A comment, with at most one nested level of replies. */
data class Comment(
    val id: Int,
    val parentId: Int?,
    val body: String,
    val isDeleted: Boolean,
    val upvotes: Int,
    /** "Eng yaxshi javob". */
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

/** One page of GET /v1/posts, plus the topic chips above the feed. */
data class PostsPage(
    val posts: List<Post>,
    val communities: List<CommunityTopic>,
    val page: Int,
    val total: Int,
    val hasMore: Boolean,
)
