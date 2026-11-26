/*
 * DefaultDictionaryRepository.kt
 * Module: :core:data   ·   Work package: WP-02 (filled in for WP-06)
 *
 * Network implementation of DictionaryRepository (Room cache via TermDao comes later).
 */
package uz.asos.core.data.repository.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import uz.asos.core.common.dispatcher.AsosDispatchers
import uz.asos.core.common.dispatcher.Dispatcher
import uz.asos.core.common.result.AsosResult
import uz.asos.core.common.result.mapSuccess
import uz.asos.core.data.mapper.asModel
import uz.asos.core.data.repository.DictionaryRepository
import uz.asos.core.model.dictionary.Term
import uz.asos.core.model.dictionary.TermList
import uz.asos.core.model.dictionary.TermQuery
import uz.asos.core.network.ApiCaller
import uz.asos.core.network.api.DictionaryApi
import javax.inject.Inject

internal class DefaultDictionaryRepository @Inject constructor(
    private val dictionaryApi: DictionaryApi,
    private val apiCaller: ApiCaller,
    @Dispatcher(AsosDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : DictionaryRepository {

    override suspend fun getTerms(query: TermQuery): AsosResult<TermList> = withContext(ioDispatcher) {
        apiCaller.call {
            dictionaryApi.getTerms(
                q = query.text?.trim()?.takeIf { it.isNotEmpty() },
                letter = query.letter,
                saved = if (query.savedOnly) "1" else null,
            )
        }.mapSuccess { it.asModel() }
    }

    override suspend fun getTermOfTheDay(): AsosResult<Term> = withContext(ioDispatcher) {
        apiCaller.call { dictionaryApi.getTermOfTheDay() }.mapSuccess { it.term.asModel() }
    }

    override suspend fun getTerm(slug: String): AsosResult<Term> = withContext(ioDispatcher) {
        apiCaller.call { dictionaryApi.getTerm(slug) }.mapSuccess { it.term.asModel() }
    }
}
