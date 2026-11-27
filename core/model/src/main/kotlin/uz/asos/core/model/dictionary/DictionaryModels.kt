/*
 * DictionaryModels.kt
 * Module: :core:model   ·   Work package: WP-02
 *
 * Domain models of Konstitutsiyaviy lug‘at, shared by every layer.
 */
package uz.asos.core.model.dictionary

/**
 * A term in a list or as a chip ("Lug‘atda" on the article reader): GET /v1/terms.
 *
 * The list endpoint sends article numbers only; [TermDetail] carries the full references.
 */
data class TermSummary(
    val id: Int,
    val term: String,
    val slug: String,
    /** Uzbek letter the term is filed under: "A", "O‘", "Sh", "Ch", "Ng". */
    val letter: String?,
    val shortDefinition: String?,
    val articleNumbers: List<Int>,
    val isSaved: Boolean,
)

/** The whole term: GET /v1/terms/{slug} and GET /v1/terms/of-the-day. */
data class TermDetail(
    val id: Int,
    val term: String,
    val slug: String,
    val letter: String,
    /** "ot", "sifat"… null when the editors left it out. */
    val partOfSpeech: String?,
    /** "arabcha “daxl” — tegish, aralashish". */
    val etymology: String?,
    val definition: String,
    /** The «Hayotda» box: a plain-language example. */
    val inLife: String?,
    val shortDef: String,
    val articles: List<TermArticle>,
    val isSaved: Boolean,
)

/** Article a term points at. */
data class TermArticle(
    val number: Int,
    val title: String,
    val slug: String,
)

/** One letter of the Uzbek alphabet rail, with how many terms sit under it. */
data class AlphabetLetter(
    val letter: String,
    val count: Int,
) {
    val isEmpty: Boolean get() = count == 0
}

/** One page of GET /v1/terms, plus the alphabet rail the screen draws beside it. */
data class TermsPage(
    val terms: List<TermSummary>,
    val letters: List<AlphabetLetter>,
    val page: Int,
    val total: Int,
    val hasMore: Boolean,
)
