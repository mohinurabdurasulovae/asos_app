/*
 * DictionaryRepository.kt
 * Module: :core:data   ·   Work package: WP-02
 *
 * Interface. Terms, the alphabet rail, the term of the day and saving a term.
 * Feature ViewModels depend on this interface only.
 */
package uz.asos.core.data.repository

import uz.asos.core.common.result.AsosResult
import uz.asos.core.model.dictionary.AlphabetLetter
import uz.asos.core.model.dictionary.TermDetail
import uz.asos.core.model.dictionary.TermsPage

interface DictionaryRepository {

    /**
     * GET /v1/terms
     *
     * @param letter one Uzbek letter, or null for every term.
     * @param query search text, or null.
     * @param savedOnly only the terms the user saved (needs a signed-in user).
     */
    suspend fun getTerms(
        letter: String? = null,
        query: String? = null,
        savedOnly: Boolean = false,
        page: Int = 1,
    ): AsosResult<TermsPage>

    /** GET /v1/terms/of-the-day — the «Kun atamasi» card. */
    suspend fun getTermOfTheDay(): AsosResult<TermDetail>

    /** GET /v1/terms/{slug} */
    suspend fun getTerm(slug: String): AsosResult<TermDetail>

    /** The alphabet rail on its own, when the list is not reloaded. */
    suspend fun getLetters(): AsosResult<List<AlphabetLetter>>

    /** POST/DELETE /v1/me/bookmarks — returns the new saved state. */
    suspend fun toggleSaved(termId: Int, isSaved: Boolean): AsosResult<Boolean>
}
