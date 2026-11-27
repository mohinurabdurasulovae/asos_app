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

    suspend fun getConstitution(): AsosResult<ConstitutionTree>

    suspend fun getTopics(): AsosResult<List<Topic>>

    suspend fun getChapterArticles(chapterRoman: String): AsosResult<List<ArticleBrief>>

    suspend fun getArticles(query: ArticleQuery): AsosResult<List<ArticleBrief>>

    suspend fun getArticle(key: String): AsosResult<Article>

    suspend fun getReadingOverview(): AsosResult<ReadingOverview?>

    suspend fun updateProgress(key: String, status: ReadingStatus, seconds: Int): AsosResult<ReadProgressResult?>
}
