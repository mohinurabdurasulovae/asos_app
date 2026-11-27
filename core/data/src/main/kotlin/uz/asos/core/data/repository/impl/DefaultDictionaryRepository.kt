/*
 * DefaultDictionaryRepository.kt
 * Module: :core:data   ·   Work package: WP-02
 *
 * DictionaryRepository backed by DictionaryApi.
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
import uz.asos.core.model.dictionary.AlphabetLetter
import uz.asos.core.model.dictionary.TermDetail
import uz.asos.core.model.dictionary.TermsPage
import uz.asos.core.model.user.BookmarkType
import uz.asos.core.network.ApiCaller
import uz.asos.core.network.api.DictionaryApi
import uz.asos.core.network.api.UserApi
import uz.asos.core.network.dto.BookmarkRequestDto
import javax.inject.Inject

internal class DefaultDictionaryRepository @Inject constructor(
    private val dictionaryApi: DictionaryApi,
    private val userApi: UserApi,
    private val apiCaller: ApiCaller,
    @Dispatcher(AsosDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : DictionaryRepository {

    override suspend fun getTerms(
        letter: String?,
        query: String?,
        savedOnly: Boolean,
        page: Int,
    ): AsosResult<TermsPage> = io {
        apiCaller.call {
            dictionaryApi.getTerms(
                letter = letter?.takeIf { it.isNotBlank() },
                query = query?.takeIf { it.isNotBlank() },
                saved = if (savedOnly) SAVED_FLAG else null,
                page = page.coerceAtLeast(1),
            )
        }.mapSuccess { it.asModel() }
    }

    override suspend fun getTermOfTheDay(): AsosResult<TermDetail> = io {
        apiCaller.call { dictionaryApi.getTermOfTheDay() }.mapSuccess { it.term.asModel() }
    }

    override suspend fun getTerm(slug: String): AsosResult<TermDetail> = io {
        apiCaller.call { dictionaryApi.getTerm(slug) }.mapSuccess { it.term.asModel() }
    }

    override suspend fun getLetters(): AsosResult<List<AlphabetLetter>> = io {
        // The list endpoint already carries the rail; asking for one row keeps it cheap.
        apiCaller.call { dictionaryApi.getTerms(perPage = 1) }
            .mapSuccess { response -> response.asModel().letters }
    }

    override suspend fun toggleSaved(termId: Int, isSaved: Boolean): AsosResult<Boolean> = io {
        val kind = BookmarkType.TERM.apiValue
        if (isSaved) {
            apiCaller.call { userApi.removeBookmark(kind = kind, id = termId) }
                .mapSuccess { false }
        } else {
            apiCaller.call { userApi.addBookmark(BookmarkRequestDto(kind = kind, targetId = termId)) }
                .mapSuccess { true }
        }
    }

    private suspend fun <T> io(block: suspend () -> T): T = withContext(ioDispatcher) { block() }

    private companion object {
        const val SAVED_FLAG = "1"
    }
}
