/*
 * DefaultBookmarkRepository.kt
 * Module: :core:data   ·   Work package: WP-02
 *
 * Network implementation of BookmarkRepository.
 */
package uz.asos.core.data.repository.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import uz.asos.core.common.dispatcher.AsosDispatchers
import uz.asos.core.common.dispatcher.Dispatcher
import uz.asos.core.common.result.AsosResult
import uz.asos.core.common.result.mapSuccess
import uz.asos.core.data.repository.BookmarkRepository
import uz.asos.core.model.user.BookmarkType
import uz.asos.core.network.ApiCaller
import uz.asos.core.network.api.UserApi
import uz.asos.core.network.dto.BookmarkRequestDto
import javax.inject.Inject

internal class DefaultBookmarkRepository @Inject constructor(
    private val userApi: UserApi,
    private val apiCaller: ApiCaller,
    @Dispatcher(AsosDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : BookmarkRepository {

    override suspend fun saveArticle(articleId: Int): AsosResult<Unit> = withContext(ioDispatcher) {
        apiCaller.call {
            userApi.addBookmark(
                BookmarkRequestDto(kind = BookmarkType.ARTICLE.apiValue, targetId = articleId),
            )
        }.mapSuccess { }
    }

    override suspend fun remove(type: BookmarkType, id: Int): AsosResult<Unit> = withContext(ioDispatcher) {
        apiCaller.call { userApi.removeBookmark(kind = type.apiValue, id = id) }.mapSuccess { }
    }
}
