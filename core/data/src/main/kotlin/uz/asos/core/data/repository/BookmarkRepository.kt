package uz.asos.core.data.repository

import uz.asos.core.common.result.AsosResult
import uz.asos.core.model.user.BookmarkType

interface BookmarkRepository {

    suspend fun saveArticle(articleId: Int): AsosResult<Unit>

    suspend fun remove(type: BookmarkType, id: Int): AsosResult<Unit>
}
