package uz.asos.feature.home.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import uz.asos.core.common.dispatcher.AsosDispatchers
import uz.asos.core.common.dispatcher.Dispatcher
import uz.asos.core.common.result.AsosResult
import uz.asos.core.common.result.mapSuccess
import uz.asos.core.model.user.BookmarkType
import uz.asos.core.network.ApiCaller
import uz.asos.core.network.api.UserApi
import uz.asos.core.network.dto.BookmarkRequestDto
import uz.asos.feature.home.data.model.DailyAnswer
import uz.asos.feature.home.data.model.HomeFeed
import uz.asos.feature.home.data.network.DailyAnswerBodyDto
import uz.asos.feature.home.data.network.HomeFeedApi
import javax.inject.Inject

internal class DefaultHomeRepository @Inject constructor(
    private val homeApi: HomeFeedApi,
    private val userApi: UserApi,
    private val apiCaller: ApiCaller,
    @Dispatcher(AsosDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : HomeRepository {

    override suspend fun getHomeFeed(): AsosResult<HomeFeed> = withContext(ioDispatcher) {
        apiCaller.call { homeApi.getHome() }.mapSuccess { it.asModel() }
    }

    override suspend fun answerDailyQuestion(optionId: Int): AsosResult<DailyAnswer> = withContext(ioDispatcher) {
        apiCaller.call { homeApi.answerDailyQuestion(DailyAnswerBodyDto(optionId = optionId)) }
            .mapSuccess { it.asModel() }
    }

    override suspend fun saveArticleByNumber(number: Int): AsosResult<Unit> = withContext(ioDispatcher) {
        apiCaller.call {
            userApi.addBookmark(BookmarkRequestDto(kind = BookmarkType.ARTICLE.apiValue, articleNumber = number))
        }.mapSuccess { }
    }
}
