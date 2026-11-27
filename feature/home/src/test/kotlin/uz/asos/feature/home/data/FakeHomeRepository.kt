package uz.asos.feature.home.data

import uz.asos.core.common.result.AsosResult
import uz.asos.feature.home.data.model.DailyAnswer
import uz.asos.feature.home.data.model.HomeFeed

class FakeHomeRepository : HomeRepository {

    var feedResult: AsosResult<HomeFeed> = AsosResult.Success(HomeSampleData.feed)
    var answerResult: AsosResult<DailyAnswer> = AsosResult.Success(HomeSampleData.correctAnswer)
    var saveResult: AsosResult<Unit> = AsosResult.Success(Unit)

    var feedRequests = 0
        private set
    val answeredOptionIds = mutableListOf<Int>()
    val savedArticleNumbers = mutableListOf<Int>()

    override suspend fun getHomeFeed(): AsosResult<HomeFeed> {
        feedRequests++
        return feedResult
    }

    override suspend fun answerDailyQuestion(optionId: Int): AsosResult<DailyAnswer> {
        answeredOptionIds += optionId
        return answerResult
    }

    override suspend fun saveArticleByNumber(number: Int): AsosResult<Unit> {
        savedArticleNumbers += number
        return saveResult
    }
}
