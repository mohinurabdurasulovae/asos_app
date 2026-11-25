/*
 * DictionaryModels.kt
 * Module: :core:model   ·   Work package: WP-02
 *
 * Domain models shared by every layer.
 *
 * Still to add (WP-06 / WP-02):
 *  - Term (full entry: partOfSpeech, etymology, definition, inLife, articles)
 */
package uz.asos.core.model.dictionary

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
