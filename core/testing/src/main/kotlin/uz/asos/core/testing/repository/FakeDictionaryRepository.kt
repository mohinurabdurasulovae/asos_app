package uz.asos.core.testing.repository

import uz.asos.core.common.result.AsosResult
import uz.asos.core.data.repository.DictionaryRepository
import uz.asos.core.model.dictionary.AlphabetLetter
import uz.asos.core.model.dictionary.TermDetail
import uz.asos.core.model.dictionary.TermsPage
import uz.asos.core.testing.data.DictionarySampleData

class FakeDictionaryRepository : DictionaryRepository {

    var termsResult: AsosResult<TermsPage>? = null
    var termOfTheDayResult: AsosResult<TermDetail> =
        AsosResult.Success(DictionarySampleData.termOfTheDay)
    var termResult: AsosResult<TermDetail> = AsosResult.Success(DictionarySampleData.daxlsizlik)
    var lettersResult: AsosResult<List<AlphabetLetter>> =
        AsosResult.Success(DictionarySampleData.letters)
    var toggleResult: AsosResult<Boolean>? = null

    val queries = mutableListOf<String?>()
    val letterFilters = mutableListOf<String?>()
    val savedToggles = mutableListOf<Pair<Int, Boolean>>()
    var termsCalls = 0
        private set
    var termOfTheDayCalls = 0
        private set

    private val saved = DictionarySampleData.terms
        .filter { it.isSaved }
        .map { it.id }
        .toMutableSet()

    override suspend fun getTerms(
        letter: String?,
        query: String?,
        savedOnly: Boolean,
        page: Int,
    ): AsosResult<TermsPage> {
        termsCalls++
        queries += query
        letterFilters += letter
        termsResult?.let { return it }

        val matching = DictionarySampleData.terms
            .filter { letter == null || it.letter == letter }
            .filter { query.isNullOrBlank() || it.term.contains(query, ignoreCase = true) }
            .filter { !savedOnly || it.id in saved }
            .map { it.copy(isSaved = it.id in saved) }

        return AsosResult.Success(
            DictionarySampleData.page.copy(
                terms = matching,
                page = page,
                total = matching.size,
                hasMore = false,
            ),
        )
    }

    override suspend fun getTermOfTheDay(): AsosResult<TermDetail> {
        termOfTheDayCalls++
        return termOfTheDayResult
    }

    override suspend fun getTerm(slug: String): AsosResult<TermDetail> = termResult

    override suspend fun getLetters(): AsosResult<List<AlphabetLetter>> = lettersResult

    override suspend fun toggleSaved(termId: Int, isSaved: Boolean): AsosResult<Boolean> {
        savedToggles += termId to isSaved
        toggleResult?.let { return it }

        val nowSaved = !isSaved
        if (nowSaved) saved += termId else saved -= termId
        return AsosResult.Success(nowSaved)
    }
}
