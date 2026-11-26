/*
 * CommunityFeedViewModel.kt
 * Module: :feature:community   ·   Work package: WP-09
 * Screen: Hamjamiyat (design artboard: Hamjamiyat.dc.html)
 *
 * @HiltViewModel. Holds StateFlow<CommunityFeedUiState>, handles CommunityFeedAction, calls
 * use cases / repositories from core:domain and core:data. No Android UI types here.
 *
 * To include:
 *  - API used (through repositories): GET /communities, GET /posts, POST /posts, POST
 *    /posts/{postId}/vote
 *
 * PLACEHOLDER: not implemented yet.
 */

package uz.asos.feature.community.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CommunityFeedViewModel @Inject constructor() : ViewModel() {

    private val allTopics = listOf(
        CommunityTopicUi(id = "all", label = "Barchasi"),
        CommunityTopicUi(id = "housing", label = "Uy-joy"),
        CommunityTopicUi(id = "education", label = "Ta'lim"),
        CommunityTopicUi(id = "labor", label = "Mehnat"),
        CommunityTopicUi(id = "family", label = "Oila"),
    )

    private val sortLabels = listOf("Ommabop", "Yangi", "Javobsiz", "Obunalar")

    private var masterPosts: List<CommunityPostUi> = initialSamplePosts()
    private var currentTopicId: String = "all"
    private var currentSortIndex: Int = 0
    private var askSheetVisible: Boolean = false
    private var draft: AskQuestionDraftUi = AskQuestionDraftUi()

    private val _uiState = MutableStateFlow<CommunityFeedUiState>(CommunityFeedUiState.Loading)
    val uiState: StateFlow<CommunityFeedUiState> = _uiState.asStateFlow()

    init {
        loadFeed()
    }

    fun onAction(action: CommunityFeedAction) {
        when (action) {
            is CommunityFeedAction.SelectTopic -> {
                currentTopicId = action.topicId
                publishSuccess()
            }
            is CommunityFeedAction.SelectSort -> {
                currentSortIndex = action.sortIndex
                publishSuccess()
            }
            is CommunityFeedAction.UpvotePost -> {
                masterPosts = masterPosts.map { post ->
                    if (post.id == action.postId) {
                        val nextVoted = !post.isUpvoted
                        post.copy(
                            isUpvoted = nextVoted,
                            votes = if (nextVoted) post.votes + 1 else (post.votes - 1).coerceAtLeast(0),
                        )
                    } else {
                        post
                    }
                }
                publishSuccess()
            }
            is CommunityFeedAction.ToggleBookmark -> {
                masterPosts = masterPosts.map { post ->
                    if (post.id == action.postId) {
                        post.copy(isBookmarked = !post.isBookmarked)
                    } else {
                        post
                    }
                }
                publishSuccess()
            }
            CommunityFeedAction.OpenAskSheet -> {
                askSheetVisible = true
                draft = draft.copy(
                    selectedTopicId = if (currentTopicId == "all") "housing" else currentTopicId,
                )
                publishSuccess()
            }
            CommunityFeedAction.DismissAskSheet -> {
                askSheetVisible = false
                publishSuccess()
            }
            is CommunityFeedAction.UpdateDraftTitle -> {
                draft = draft.copy(title = action.value)
                publishSuccess()
            }
            is CommunityFeedAction.UpdateDraftBody -> {
                draft = draft.copy(body = action.value)
                publishSuccess()
            }
            is CommunityFeedAction.UpdateDraftTopic -> {
                draft = draft.copy(selectedTopicId = action.topicId)
                publishSuccess()
            }
            is CommunityFeedAction.ToggleDraftAnonymous -> {
                draft = draft.copy(isAnonymous = action.anonymous)
                publishSuccess()
            }
            CommunityFeedAction.SubmitQuestion -> submitQuestion()
            CommunityFeedAction.Retry -> loadFeed()
            is CommunityFeedAction.OpenPost,
            CommunityFeedAction.OpenBookmarks,
            CommunityFeedAction.OpenSearch,
            CommunityFeedAction.OpenNotifications -> Unit
        }
    }

    fun loadFeed() {
        viewModelScope.launch {
            _uiState.value = CommunityFeedUiState.Loading
            publishSuccess()
        }
    }

    fun showErrorForTest(message: String) {
        _uiState.value = CommunityFeedUiState.Error(message)
    }

    private fun submitQuestion() {
        val cleanTitle = draft.title.trim()
        val cleanBody = draft.body.trim()
        if (cleanTitle.isEmpty() || cleanBody.isEmpty()) return

        val topicLabel = allTopics.firstOrNull { it.id == draft.selectedTopicId }?.label ?: "Uy-joy"
        val newPost = CommunityPostUi(
            id = masterPosts.size + 1,
            categoryLabel = topicLabel,
            authorLabel = if (draft.isAnonymous) "Anonim" else "Aziza K.",
            timeAgoLabel = "hozirgina",
            title = cleanTitle,
            bodyPreview = cleanBody,
            votes = 1,
            isUpvoted = true,
            commentCount = 0,
            isBookmarked = false,
            statusText = "Yurist javobi kutilmoqda",
            isLawyerAnswered = false,
        )
        masterPosts = listOf(newPost) + masterPosts
        draft = AskQuestionDraftUi(selectedTopicId = draft.selectedTopicId)
        askSheetVisible = false
        publishSuccess()
    }

    private fun publishSuccess() {
        val selectedLabel = allTopics.firstOrNull { it.id == currentTopicId }?.label
        val filtered = masterPosts.filter { post ->
            val matchesTopic = selectedLabel == null || currentTopicId == "all" || post.categoryLabel == selectedLabel
            val matchesSort = when (currentSortIndex) {
                2 -> !post.isLawyerAnswered && post.commentCount == 0 || post.statusText?.contains("kutilmoqda") == true
                else -> true
            }
            matchesTopic && matchesSort
        }

        val ordered = when (currentSortIndex) {
            1 -> filtered.reversed()
            else -> filtered.sortedByDescending { it.votes }
        }

        _uiState.value = CommunityFeedUiState.Success(
            topics = allTopics,
            selectedTopicId = currentTopicId,
            sortOptions = sortLabels,
            selectedSortIndex = currentSortIndex,
            posts = ordered,
            askSheetVisible = askSheetVisible,
            draft = draft,
        )
    }

    private fun initialSamplePosts(): List<CommunityPostUi> = listOf(
        CommunityPostUi(
            id = 1,
            categoryLabel = "Uy-joy",
            authorLabel = "Anonim",
            timeAgoLabel = "2 soat",
            title = "Uy egasi ruxsatsiz kvartiraga kirib turibdi. Bu qonuniymi?",
            bodyPreview = "Ijarada yashayman. Uy egasi kalit nusxasini saqlab qolgan va men yo'qligimda kirib, narsalarimni ko'rib ketyapti. Shartnomada bu haqda hech narsa yo'q...",
            votes = 128,
            isUpvoted = false,
            commentCount = 34,
            isBookmarked = false,
            lawyerName = "Sardor Rahimov",
            lawyerExcerpt = "31-modda uy-joy daxlsizligini kafolatlaydi. Avval uy egasiga yozma ogohlantirish yuboring...",
            isLawyerAnswered = true,
        ),
        CommunityPostUi(
            id = 2,
            categoryLabel = "Ta'lim",
            authorLabel = "Jasur T.",
            timeAgoLabel = "5 soat",
            title = "Kontrakt to'lovi kechiksa, talabani darsga kiritmaslik mumkinmi?",
            bodyPreview = "To'lovning ikkinchi qismini ikki haftaga kechiktirdim. Dekanat imtihonlarga kiritmasligini aytdi. Bu to'g'rimi?",
            votes = 76,
            isUpvoted = false,
            commentCount = 19,
            isBookmarked = false,
            statusText = "Yurist javobi kutilmoqda",
            isLawyerAnswered = false,
        ),
        CommunityPostUi(
            id = 3,
            categoryLabel = "Mehnat",
            authorLabel = "Nilufar K.",
            timeAgoLabel = "kecha",
            title = "16 yoshda yozgi ishga rasmiy kirish mumkinmi?",
            bodyPreview = "Yozda kafeda ishlamoqchiman. Ish beruvchi \"shartnoma shart emas\" deyapti. Qanday huquqlarim bor?",
            votes = 54,
            isUpvoted = false,
            commentCount = 11,
            isBookmarked = false,
            statusText = "Advokat javob berdi",
            isLawyerAnswered = true,
        ),
        CommunityPostUi(
            id = 4,
            categoryLabel = "Oila",
            authorLabel = "Anonim",
            timeAgoLabel = "2 kun",
            title = "19 yoshdaman, ota-onam pasportimni berishmayapti",
            bodyPreview = "Boshqa shaharda o'qishga kirmoqchiman, lekin hujjatlarimni \"xavfsizlik uchun\" olib qo'yishgan...",
            votes = 41,
            isUpvoted = false,
            commentCount = 23,
            isBookmarked = false,
        ),
    )
}
