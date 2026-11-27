package uz.asos.core.testing.repository

import uz.asos.core.common.result.AsosResult
import uz.asos.core.data.repository.BookmarkRepository
import uz.asos.core.model.user.BookmarkType

class FakeBookmarkRepository : BookmarkRepository {

    var result: AsosResult<Unit> = AsosResult.Success(Unit)
    val savedArticleIds = mutableListOf<Int>()
    val removed = mutableListOf<Pair<BookmarkType, Int>>()

    override suspend fun saveArticle(articleId: Int): AsosResult<Unit> {
        savedArticleIds += articleId
        return result
    }

    override suspend fun remove(type: BookmarkType, id: Int): AsosResult<Unit> {
        removed += type to id
        return result
    }
}
