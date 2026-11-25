/*
 * FakeConstitutionRepository.kt
 * Module: :core:testing   ·   Work package: WP-02
 *
 * In-memory fake of ConstitutionRepository with SampleData. Set the public vars to change
 * what it returns, and read the recorded calls to check what a ViewModel did.
 */
package uz.asos.core.testing.repository

import uz.asos.core.common.result.AsosResult
import uz.asos.core.data.repository.ConstitutionRepository
import uz.asos.core.model.constitution.Article
import uz.asos.core.model.constitution.ArticleBrief
import uz.asos.core.model.constitution.ArticleQuery
import uz.asos.core.model.constitution.ConstitutionTree
import uz.asos.core.model.constitution.ReadProgressResult
import uz.asos.core.model.constitution.ReadingOverview
import uz.asos.core.model.constitution.ReadingStatus
import uz.asos.core.model.constitution.Topic
import uz.asos.core.testing.data.SampleData

class FakeConstitutionRepository : ConstitutionRepository {

    var constitutionResult: AsosResult<ConstitutionTree> = AsosResult.Success(SampleData.constitutionTree)
    var topicsResult: AsosResult<List<Topic>> = AsosResult.Success(SampleData.topics)
    var chapterResult: AsosResult<List<ArticleBrief>> = AsosResult.Success(SampleData.chapterIXArticles)
    var articlesResult: AsosResult<List<ArticleBrief>> = AsosResult.Success(SampleData.chapterIXArticles)
    var articleResult: AsosResult<Article> = AsosResult.Success(SampleData.article50)
    var overviewResult: AsosResult<ReadingOverview?> = AsosResult.Success(null)
    var progressResult: AsosResult<ReadProgressResult?> = AsosResult.Success(null)

    val articleQueries = mutableListOf<ArticleQuery>()
    val requestedChapters = mutableListOf<String>()
    val progressUpdates = mutableListOf<Pair<String, ReadingStatus>>()
    var constitutionCalls = 0
        private set

    override suspend fun getConstitution(): AsosResult<ConstitutionTree> {
        constitutionCalls++
        return constitutionResult
    }

    override suspend fun getTopics(): AsosResult<List<Topic>> = topicsResult

    override suspend fun getChapterArticles(chapterRoman: String): AsosResult<List<ArticleBrief>> {
        requestedChapters += chapterRoman
        return chapterResult
    }

    override suspend fun getArticles(query: ArticleQuery): AsosResult<List<ArticleBrief>> {
        articleQueries += query
        return articlesResult
    }

    override suspend fun getArticle(key: String): AsosResult<Article> = articleResult

    override suspend fun getReadingOverview(): AsosResult<ReadingOverview?> = overviewResult

    override suspend fun updateProgress(
        key: String,
        status: ReadingStatus,
        seconds: Int,
    ): AsosResult<ReadProgressResult?> {
        progressUpdates += key to status
        return progressResult
    }
}
