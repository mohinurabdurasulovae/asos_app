/*
 * PostDetailViewModel.kt
 * Module: :feature:community   ·   Work package: WP-09
 * Screen: Muhokama (design artboard: Post.dc.html)
 *
 * @HiltViewModel. Holds StateFlow<PostDetailUiState>, handles PostDetailAction, calls use
 * cases / repositories from core:domain and core:data. No Android UI types here.
 *
 * To include:
 *  - API used (through repositories): GET /posts/{postId}, GET /posts/{postId}/replies, POST
 *    /posts/{postId}/replies, POST /posts/{postId}/vote, POST /replies/{replyId}/vote
 *
 * PLACEHOLDER: not implemented yet.
 */

package uz.asos.feature.community.post

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.asos.core.navigation.PostRoute

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val postId: Int = runCatching {
        savedStateHandle.toRoute<PostRoute>().postId
    }.getOrDefault(1)

    private var currentPost: PostDetailUi = samplePost(postId)
    private var currentReplies: List<ReplyItemUi> = sampleReplies()
    private var replyDraft: String = ""
    private var isAnonymousReply: Boolean = false

    private val _uiState = MutableStateFlow<PostDetailUiState>(PostDetailUiState.Loading)
    val uiState: StateFlow<PostDetailUiState> = _uiState.asStateFlow()

    init {
        loadDetail()
    }

    fun onAction(action: PostDetailAction) {
        when (action) {
            PostDetailAction.ToggleJoinCommunity -> {
                currentPost = currentPost.copy(isJoined = !currentPost.isJoined)
                publishSuccess()
            }
            PostDetailAction.UpvotePost -> {
                val nextVoted = !currentPost.isUpvoted
                currentPost = currentPost.copy(
                    isUpvoted = nextVoted,
                    votes = if (nextVoted) currentPost.votes + 1 else (currentPost.votes - 1).coerceAtLeast(0),
                )
                publishSuccess()
            }
            PostDetailAction.ToggleBookmarkPost -> {
                currentPost = currentPost.copy(isBookmarked = !currentPost.isBookmarked)
                publishSuccess()
            }
            is PostDetailAction.UpvoteReply -> {
                currentReplies = currentReplies.map { reply ->
                    if (reply.id == action.replyId) {
                        val nextLiked = !reply.isLiked
                        reply.copy(
                            isLiked = nextLiked,
                            likes = if (nextLiked) reply.likes + 1 else (reply.likes - 1).coerceAtLeast(0),
                        )
                    } else {
                        reply
                    }
                }
                publishSuccess()
            }
            is PostDetailAction.ReplyToAuthor -> {
                replyDraft = "@${action.authorName} "
                publishSuccess()
            }
            is PostDetailAction.UpdateReplyText -> {
                replyDraft = action.text
                publishSuccess()
            }
            is PostDetailAction.ToggleAnonymous -> {
                isAnonymousReply = action.anonymous
                publishSuccess()
            }
            PostDetailAction.SendReply -> {
                val text = replyDraft.trim()
                if (text.isEmpty()) return
                val author = if (isAnonymousReply) "Anonim" else "Aziza K."
                val initials = if (isAnonymousReply) "AN" else "AK"
                val newReply = ReplyItemUi(
                    id = "r_${currentReplies.size + 1}",
                    authorName = author,
                    initials = initials,
                    metaLabel = "hozirgina",
                    content = text,
                    likes = 1,
                    isLiked = true,
                )
                currentReplies = currentReplies + newReply
                currentPost = currentPost.copy(commentCount = currentPost.commentCount + 1)
                replyDraft = ""
                publishSuccess()
            }
            PostDetailAction.Retry -> loadDetail()
            PostDetailAction.Back,
            PostDetailAction.OpenCommunity,
            PostDetailAction.SharePost,
            is PostDetailAction.OpenChat,
            is PostDetailAction.OpenArticle -> Unit
        }
    }

    fun loadDetail() {
        viewModelScope.launch {
            _uiState.value = PostDetailUiState.Loading
            publishSuccess()
        }
    }

    fun showErrorForTest(message: String) {
        _uiState.value = PostDetailUiState.Error(message)
    }

    private fun publishSuccess() {
        _uiState.value = PostDetailUiState.Success(
            post = currentPost,
            replies = currentReplies,
            replyDraft = replyDraft,
            isAnonymousReply = isAnonymousReply,
        )
    }

    private fun samplePost(id: Int): PostDetailUi = PostDetailUi(
        id = id,
        communityName = "Uy-joy",
        membersCountLabel = "2,4 ming",
        isJoined = false,
        authorLabel = "Anonim foydalanuvchi",
        timeAgoLabel = "2 soat oldin",
        title = "Uy egasi ruxsatsiz kvartiraga kirib turibdi. Bu qonuniymi?",
        body = "Ijaradagi kvartirada yashayman. Uy egasi kalit nusxasini saqlab qolgan va men yo'qligimda kirib, narsalarimni ko'rib ketyapti. Shartnomada bu haqda hech narsa yozilmagan. Nima qilsam bo'ladi?",
        tags = listOf("ijara", "uy-joy daxlsizligi", "31-modda"),
        votes = 128,
        isUpvoted = true,
        commentCount = 34,
        isBookmarked = false,
    )

    private fun sampleReplies(): List<ReplyItemUi> = listOf(
        ReplyItemUi(
            id = "r1",
            authorName = "Sardor Rahimov",
            initials = "SR",
            metaLabel = "Uy-joy ishlari · 1 soat",
            content = "Konstitutsiyaning 31-moddasi uy-joy daxlsizligini kafolatlaydi: qonunda belgilangan holatlardan tashqari, unda yashovchilarning roziligisiz uyga kirish mumkin emas. Ijara davomida kvartiradan qonuniy foydalanuvchi — siz.\n\nAvval uy egasiga yozma ogohlantirish yuboring va shartnomaga tashrif tartibini qo'shishni taklif qiling. Takrorlansa — sudga murojaat qilish huquqingiz bor (55-modda).",
            likes = 96,
            isLiked = false,
            isBestAnswer = true,
            isVerifiedLawyer = true,
            roleLabel = "Tasdiqlangan advokat",
            articleNumber = "31",
            articleTitle = "Shaxsiy hayot va uy-joy daxlsizligi",
            articleKey = "31",
            lawyerChatId = 123,
        ),
        ReplyItemUi(
            id = "r2",
            authorName = "kamola_88",
            initials = "K8",
            metaLabel = "1 soat",
            content = "Menda ham shunday bo'lgan. Qulfni almashtirib, uy egasiga xabar berdim — muammo hal bo'ldi.",
            likes = 24,
            isLiked = false,
        ),
        ReplyItemUi(
            id = "r3",
            authorName = "Sardor Rahimov",
            initials = "SR",
            metaLabel = "50 daq",
            content = "Qulfni almashtirishdan oldin shartnomani tekshiring — ba'zi shartnomalarda bu uy egasining roziligi bilan qilinadi.",
            likes = 31,
            isLiked = false,
            isVerifiedLawyer = true,
            roleLabel = "Advokat",
            lawyerChatId = 123,
        ),
    )
}
