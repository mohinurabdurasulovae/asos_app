/*
 * DictionaryRepository.kt
 * Module: :core:data   ·   Work package: WP-02 (filled in for WP-06)
 *
 * Interface for the legal dictionary. Feature ViewModels depend on this interface only;
 * tests use FakeDictionaryRepository from :core:testing. Saving a term goes through
 * BookmarkRepository.
 */
package uz.asos.core.data.repository

import uz.asos.core.common.result.AsosResult
import uz.asos.core.model.dictionary.Term
import uz.asos.core.model.dictionary.TermList
import uz.asos.core.model.dictionary.TermQuery

interface DictionaryRepository {

    /** GET /v1/terms with filters. Saved-only for a guest answers 401 ("Avval tizimga kiring"). */
    suspend fun getTerms(query: TermQuery = TermQuery()): AsosResult<TermList>

    /** GET /v1/terms/of-the-day. */
    suspend fun getTermOfTheDay(): AsosResult<Term>

    /** GET /v1/terms/{slug}. */
    suspend fun getTerm(slug: String): AsosResult<Term>
}
