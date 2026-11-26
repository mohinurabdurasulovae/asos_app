/*
 * DictionaryModels.kt
 * Module: :core:model   ·   Work package: WP-02 (dictionary models added for WP-06)
 *
 * Domain models shared by every layer.
 */
package uz.asos.core.model.dictionary

import uz.asos.core.model.constitution.ArticleRef

/** A dictionary term in a list or as a chip ("Lug‘atda" on the article reader). */
data class TermSummary(
    val id: Int,
    val term: String,
    val slug: String,
    val letter: String?,
    val shortDefinition: String?,
    val articleNumbers: List<Int>,
    val isSaved: Boolean,
)

/** Full dictionary entry (GET /v1/terms/{slug}, GET /v1/terms/of-the-day). */
data class Term(
    val id: Int,
    val term: String,
    val slug: String,
    val letter: String?,
    /** "ot", "sifat"… */
    val partOfSpeech: String?,
    /** "arabcha “daxl” — tegish, aralashish" */
    val etymology: String?,
    val definition: String,
    /** "Hayotda" example. */
    val inLife: String?,
    val shortDefinition: String?,
    val articles: List<ArticleRef>,
    val isSaved: Boolean,
)

/** One letter of the A–Z rail with the number of terms under it. */
data class TermLetter(
    val letter: String,
    val count: Int,
)

/** Filters for GET /v1/terms. Empty query = every term. */
data class TermQuery(
    val text: String? = null,
    val letter: String? = null,
    /** Only the user's saved terms (needs a token). */
    val savedOnly: Boolean = false,
)

/** GET /v1/terms result: the terms plus the alphabet rail. */
data class TermList(
    val items: List<TermSummary>,
    val letters: List<TermLetter>,
    val total: Int,
)
