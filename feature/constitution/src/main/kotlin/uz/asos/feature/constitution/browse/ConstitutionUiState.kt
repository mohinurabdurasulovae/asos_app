/*
 * ConstitutionUiState.kt
 * Module: :feature:constitution   ·   Work package: WP-05
 * Screen: Konstitutsiya (design artboard: Konstitutsiya.dc.html)
 *
 * Immutable state the screen renders. Everything is already formatted ("19–21-moddalar",
 * "II BO‘LIM"), so ConstitutionScreen only lays it out.
 */
package uz.asos.feature.constitution.browse

enum class ConstitutionTab { TOPICS, STRUCTURE, SAVED }

data class ConstitutionUiState(
    /** First load (skeleton). Later refreshes keep the old content on screen. */
    val isLoading: Boolean = true,
    /** Set when the first load failed and there is nothing to show. */
    val errorMessage: String? = null,
    /** "2023-yil tahriri · 6 bo‘lim · 27 bob · 155 modda" */
    val subtitle: String = "",
    val selectedTab: ConstitutionTab = ConstitutionTab.STRUCTURE,
    val query: String = "",
    val isSignedIn: Boolean = false,
    val continueReading: ContinueReadingUi? = null,
    val topics: List<TopicUi> = emptyList(),
    /** null = "Barchasi" (show the table of contents). */
    val selectedTopicSlug: String? = null,
    val sections: List<SectionUi> = emptyList(),
    val expandedSections: Set<String> = emptySet(),
    val expandedChapters: Set<String> = emptySet(),
    /** Articles of an opened chapter, by chapter roman numeral. */
    val chapterArticles: Map<String, ArticleListState> = emptyMap(),
    /** List shown for a search, a selected topic or the Saqlangan tab. */
    val articleList: ArticleListState = ArticleListState.Hidden,
) {
    val isSearching: Boolean get() = query.isNotBlank()
}

/** "Oxirgi o‘qilgan" card. */
data class ContinueReadingUi(
    val articleKey: String,
    /** "50" */
    val numberLabel: String,
    /** "Ta’lim olish huquqi" */
    val title: String,
    /** "IX bob · 3 daqiqa qoldi" */
    val meta: String,
)

data class TopicUi(
    val slug: String,
    val title: String,
    /** "13 modda" */
    val countLabel: String,
    /** Design tone name from the API (brand, teal, orange, purple, pink, navy). */
    val tone: String?,
)

data class SectionUi(
    val roman: String,
    /** "II BO‘LIM" */
    val overline: String,
    val title: String,
    /** "7 bob · 19–64-moddalar" or "154–155-moddalar" for section VI. */
    val meta: String,
    val chapters: List<ChapterUi>,
    /** Section VI has no chapters: it opens its articles directly. */
    val directArticlesQuery: String?,
)

enum class ReadState { READ, IN_PROGRESS, NOT_STARTED }

data class ChapterUi(
    val roman: String,
    val title: String,
    /** "41–53-moddalar" or "41–53-moddalar · 8/13 o‘qildi" */
    val meta: String,
    val state: ReadState,
    /** 0..100, used by the progress ring. */
    val percent: Int,
) {
    /** In-progress chapters are highlighted, as in the design. */
    val isHighlighted: Boolean get() = state == ReadState.IN_PROGRESS
}

data class ArticleRowUi(
    /** Route key: "50" or "muqaddima". */
    val key: String,
    /** "50", or "§" for the preamble. */
    val numberLabel: String,
    /** Editorial title, or the start of the official text when there is no title. */
    val title: String,
    /** "IX bob · 4 daqiqa" */
    val meta: String,
    val state: ReadState,
)

sealed interface ArticleListState {
    data object Hidden : ArticleListState
    data object Loading : ArticleListState
    data class Loaded(val items: List<ArticleRowUi>) : ArticleListState
    data class Failed(val message: String) : ArticleListState
    /** Saqlangan tab while signed out. */
    data object NeedsSignIn : ArticleListState
}
