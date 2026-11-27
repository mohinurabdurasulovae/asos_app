package uz.asos.feature.constitution.browse

enum class ConstitutionTab { TOPICS, STRUCTURE, SAVED }

data class ConstitutionUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val subtitle: String = "",
    val selectedTab: ConstitutionTab = ConstitutionTab.STRUCTURE,
    val query: String = "",
    val isSignedIn: Boolean = false,
    val continueReading: ContinueReadingUi? = null,
    val topics: List<TopicUi> = emptyList(),
    val selectedTopicSlug: String? = null,
    val sections: List<SectionUi> = emptyList(),
    val expandedSections: Set<String> = emptySet(),
    val expandedChapters: Set<String> = emptySet(),
    val chapterArticles: Map<String, ArticleListState> = emptyMap(),
    val articleList: ArticleListState = ArticleListState.Hidden,
) {
    val isSearching: Boolean get() = query.isNotBlank()
}

data class ContinueReadingUi(
    val articleKey: String,
    val numberLabel: String,
    val title: String,
    val meta: String,
)

data class TopicUi(
    val slug: String,
    val title: String,
    val countLabel: String,
    val tone: String?,
)

data class SectionUi(
    val roman: String,
    val overline: String,
    val title: String,
    val meta: String,
    val chapters: List<ChapterUi>,
    val directArticlesQuery: String?,
)

enum class ReadState { READ, IN_PROGRESS, NOT_STARTED }

data class ChapterUi(
    val roman: String,
    val title: String,
    val meta: String,
    val state: ReadState,
    val percent: Int,
) {
    val isHighlighted: Boolean get() = state == ReadState.IN_PROGRESS
}

data class ArticleRowUi(
    val key: String,
    val numberLabel: String,
    val title: String,
    val meta: String,
    val state: ReadState,
)

sealed interface ArticleListState {
    data object Hidden : ArticleListState
    data object Loading : ArticleListState
    data class Loaded(val items: List<ArticleRowUi>) : ArticleListState
    data class Failed(val message: String) : ArticleListState
    data object NeedsSignIn : ArticleListState
}
