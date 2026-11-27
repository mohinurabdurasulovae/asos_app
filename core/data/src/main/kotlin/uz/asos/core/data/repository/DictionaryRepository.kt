package uz.asos.core.data.repository

import uz.asos.core.common.result.AsosResult
import uz.asos.core.model.dictionary.AlphabetLetter
import uz.asos.core.model.dictionary.TermDetail
import uz.asos.core.model.dictionary.TermsPage

interface DictionaryRepository {

    suspend fun getTerms(
        letter: String? = null,
        query: String? = null,
        savedOnly: Boolean = false,
        page: Int = 1,
    ): AsosResult<TermsPage>

    suspend fun getTermOfTheDay(): AsosResult<TermDetail>

    suspend fun getTerm(slug: String): AsosResult<TermDetail>

    suspend fun getLetters(): AsosResult<List<AlphabetLetter>>

    suspend fun toggleSaved(termId: Int, isSaved: Boolean): AsosResult<Boolean>
}
