/*
 * FakeDictionaryRepository.kt
 * Module: :core:testing   ·   Work package: WP-02 (filled in for WP-06)
 *
 * In-memory fake of DictionaryRepository with SampleTerms. Set the public vars to change what
 * it returns, and read the recorded calls to check what a ViewModel did. getTerms() filters
 * SampleTerms by the query unless [termsResult] is set.
 */
package uz.asos.core.testing.repository

import uz.asos.core.common.result.AsosResult
import uz.asos.core.data.repository.DictionaryRepository
import uz.asos.core.model.dictionary.Term
import uz.asos.core.model.dictionary.TermList
import uz.asos.core.model.dictionary.TermQuery
import uz.asos.core.testing.data.SampleTerms

class FakeDictionaryRepository : DictionaryRepository {

    /** null = filter SampleTerms.list by the query (search, letter, saved). */
    var termsResult: AsosResult<TermList>? = null
    var termOfTheDayResult: AsosResult<Term> = AsosResult.Success(SampleTerms.daxlsizlik)
    var termResult: AsosResult<Term>? = null

    val termQueries = mutableListOf<TermQuery>()
    val requestedSlugs = mutableListOf<String>()

    override suspend fun getTerms(query: TermQuery): AsosResult<TermList> {
        termQueries += query
        termsResult?.let { return it }
        val text = query.text?.trim()?.lowercase().orEmpty()
        val items = SampleTerms.list.items.filter { term ->
            (text.isEmpty() || term.term.lowercase().contains(text)) &&
                (query.letter == null || term.letter == query.letter) &&
                (!query.savedOnly || term.isSaved)
        }
        return AsosResult.Success(SampleTerms.list.copy(items = items, total = items.size))
    }

    override suspend fun getTermOfTheDay(): AsosResult<Term> = termOfTheDayResult

    override suspend fun getTerm(slug: String): AsosResult<Term> {
        requestedSlugs += slug
        termResult?.let { return it }
        return SampleTerms.detail(slug)?.let { AsosResult.Success(it) }
            ?: AsosResult.Error(message = "Bunday atama lug‘atda yo‘q.", code = "term_not_found", httpStatus = 404)
    }
}
