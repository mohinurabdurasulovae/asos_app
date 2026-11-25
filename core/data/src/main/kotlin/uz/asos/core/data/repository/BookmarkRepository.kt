/*
 * BookmarkRepository.kt
 * Module: :core:data   ·   Work package: WP-02
 *
 * Interface. Save/unsave articles, terms, posts, cases (shared by many screens). All calls
 * need a signed-in user; for guests the API answers 401 with "Avval tizimga kiring.", which
 * comes back as AsosResult.Error with that message.
 *
 * Implemented: articles. Still to add: listing saved items (GET /v1/me/bookmarks).
 */
package uz.asos.core.data.repository

import uz.asos.core.common.result.AsosResult
import uz.asos.core.model.user.BookmarkType

interface BookmarkRepository {

    /**
     * POST /v1/me/bookmarks { kind: "article", targetId }. [articleId] is Article.id (works for
     * the preamble too, which has no number).
     */
    suspend fun saveArticle(articleId: Int): AsosResult<Unit>

    /** DELETE /v1/me/bookmarks/{kind}/{id}. [id] is the item's internal id (e.g. Article.id). */
    suspend fun remove(type: BookmarkType, id: Int): AsosResult<Unit>
}
