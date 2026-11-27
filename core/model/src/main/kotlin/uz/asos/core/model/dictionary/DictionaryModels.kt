package uz.asos.core.model.dictionary

data class TermSummary(
    val id: Int,
    val term: String,
    val slug: String,
    val letter: String?,
    val shortDefinition: String?,
    val articleNumbers: List<Int>,
    val isSaved: Boolean,
)

data class TermDetail(
    val id: Int,
    val term: String,
    val slug: String,
    val letter: String,
    val partOfSpeech: String?,
    val etymology: String?,
    val definition: String,
    val inLife: String?,
    val shortDef: String,
    val articles: List<TermArticle>,
    val isSaved: Boolean,
)

data class TermArticle(
    val number: Int,
    val title: String,
    val slug: String,
)

data class AlphabetLetter(
    val letter: String,
    val count: Int,
) {
    val isEmpty: Boolean get() = count == 0
}

data class TermsPage(
    val terms: List<TermSummary>,
    val letters: List<AlphabetLetter>,
    val page: Int,
    val total: Int,
    val hasMore: Boolean,
)
