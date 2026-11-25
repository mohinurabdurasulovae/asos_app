/*
 * ConstitutionRepository.kt
 * Module: :core:data   ·   Work package: WP-02
 *
 * Interface for sections, chapters, articles and topics. Feature ViewModels depend on this
 * interface only; tests use FakeConstitutionRepository from :core:testing.
 *
 * TODO(WP-02): offline-first. When :core:database exists, serve the text from Room and
 * refresh from the network in the background (docs/ARCHITECTURE.md).
 */
package uz.asos.core.data.repository

import uz.asos.core.common.result.AsosResult
import uz.asos.core.model.constitution.Article
import uz.asos.core.model.constitution.ArticleBrief
import uz.asos.core.model.constitution.ArticleQuery
import uz.asos.core.model.constitution.ConstitutionTree
import uz.asos.core.model.constitution.ReadProgressResult
import uz.asos.core.model.constitution.ReadingOverview
import uz.asos.core.model.constitution.ReadingStatus
import uz.asos.core.model.constitution.Topic

interface ConstitutionRepository {

    /** GET /v1/constitution: stats + section/chapter tree (with progress when signed in). */
    suspend fun getConstitution(): AsosResult<ConstitutionTree>

    /** GET /v1/topics. */
    suspend fun getTopics(): AsosResult<List<Topic>>

    /** GET /v1/chapters/{roman}: the articles of one chapter. */
    suspend fun getChapterArticles(chapterRoman: String): AsosResult<List<ArticleBrief>>

    /** GET /v1/articles with filters (topic, search text, saved only). */
    suspend fun getArticles(query: ArticleQuery): AsosResult<List<ArticleBrief>>

    /** GET /v1/articles/{key}. [key] = "50", a slug, or "muqaddima". */
    suspend fun getArticle(key: String): AsosResult<Article>

    /** GET /v1/me/progress. Success(null) when the user is not signed in. */
    suspend fun getReadingOverview(): AsosResult<ReadingOverview?>

    /**
     * POST /v1/articles/{key}/progress. Success(null) when the user is not signed in
     * (guests read without saving progress). Only READ and IN_PROGRESS are sent.
     */
    suspend fun updateProgress(key: String, status: ReadingStatus, seconds: Int): AsosResult<ReadProgressResult?>
}
