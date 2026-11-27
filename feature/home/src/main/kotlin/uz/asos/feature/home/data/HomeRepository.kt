package uz.asos.feature.home.data

import uz.asos.core.common.result.AsosResult
import uz.asos.feature.home.data.model.DailyAnswer
import uz.asos.feature.home.data.model.HomeFeed

interface HomeRepository {

    suspend fun getHomeFeed(): AsosResult<HomeFeed>

    suspend fun answerDailyQuestion(optionId: Int): AsosResult<DailyAnswer>

    suspend fun saveArticleByNumber(number: Int): AsosResult<Unit>
}
