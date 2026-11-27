package uz.asos.core.data.repository.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import uz.asos.core.common.dispatcher.AsosDispatchers
import uz.asos.core.common.dispatcher.Dispatcher
import uz.asos.core.common.result.AsosResult
import uz.asos.core.common.result.mapSuccess
import uz.asos.core.data.mapper.asModel
import uz.asos.core.data.repository.ConstitutionRepository
import uz.asos.core.datastore.TokenStore
import uz.asos.core.model.constitution.Article
import uz.asos.core.model.constitution.ArticleBrief
import uz.asos.core.model.constitution.ArticleQuery
import uz.asos.core.model.constitution.ConstitutionTree
import uz.asos.core.model.constitution.ReadProgressResult
import uz.asos.core.model.constitution.ReadingOverview
import uz.asos.core.model.constitution.ReadingStatus
import uz.asos.core.model.constitution.Topic
import uz.asos.core.network.ApiCaller
import uz.asos.core.network.api.ConstitutionApi
import uz.asos.core.network.api.UserApi
import uz.asos.core.network.dto.ArticleProgressRequestDto
import javax.inject.Inject

internal class DefaultConstitutionRepository @Inject constructor(
    private val constitutionApi: ConstitutionApi,
    private val userApi: UserApi,
    private val apiCaller: ApiCaller,
    private val tokenStore: TokenStore,
    @Dispatcher(AsosDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : ConstitutionRepository {

    override suspend fun getConstitution(): AsosResult<ConstitutionTree> = io {
        apiCaller.call { constitutionApi.getConstitution() }.mapSuccess { it.asModel() }
    }

    override suspend fun getTopics(): AsosResult<List<Topic>> = io {
        apiCaller.call { constitutionApi.getTopics() }.mapSuccess { response -> response.items.map { it.asModel() } }
    }

    override suspend fun getChapterArticles(chapterRoman: String): AsosResult<List<ArticleBrief>> = io {
        apiCaller.call { constitutionApi.getChapter(chapterRoman) }
            .mapSuccess { response -> response.items.map { it.asModel() } }
    }

    override suspend fun getArticles(query: ArticleQuery): AsosResult<List<ArticleBrief>> = io {
        apiCaller.call {
            constitutionApi.getArticles(
                topic = query.topic,
                chapter = query.chapter,
                section = query.section,
                q = query.text?.trim()?.takeIf { it.isNotEmpty() },
                status = if (query.savedOnly) "saved" else null,
            )
        }.mapSuccess { response -> response.items.map { it.asModel() } }
    }

    override suspend fun getArticle(key: String): AsosResult<Article> = io {
        apiCaller.call { constitutionApi.getArticle(key) }.mapSuccess { it.article.asModel() }
    }

    override suspend fun getReadingOverview(): AsosResult<ReadingOverview?> = io {
        if (!isSignedIn()) {
            AsosResult.Success(null)
        } else {
            apiCaller.call { userApi.getReadingProgress() }.mapSuccess { it.asModel() }
        }
    }

    override suspend fun updateProgress(
        key: String,
        status: ReadingStatus,
        seconds: Int,
    ): AsosResult<ReadProgressResult?> = io {
        if (!isSignedIn() || status == ReadingStatus.NOT_STARTED) {
            AsosResult.Success(null)
        } else {
            val body = ArticleProgressRequestDto(
                status = if (status == ReadingStatus.READ) "read" else "reading",
                seconds = seconds.coerceIn(0, 86_400),
            )
            apiCaller.call { constitutionApi.updateProgress(key, body) }.mapSuccess { it.asModel() }
        }
    }

    private suspend fun isSignedIn(): Boolean = !tokenStore.current().isNullOrBlank()

    private suspend fun <T> io(block: suspend () -> T): T = withContext(ioDispatcher) { block() }
}
