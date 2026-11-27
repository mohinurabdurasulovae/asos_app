package uz.asos.feature.cases.data

import kotlinx.coroutines.flow.Flow
import uz.asos.core.common.result.AsosResult
import uz.asos.feature.cases.data.model.CaseItem
import uz.asos.feature.cases.data.model.CaseProgress

interface CasesRepository {

    suspend fun getCases(): AsosResult<List<CaseItem>>

    suspend fun getCase(id: Int): AsosResult<CaseItem>

    fun observeProgress(): Flow<Map<Int, CaseProgress>>

    suspend fun saveDraft(id: Int, selectedArticles: Set<Int>, solution: String)

    suspend fun checkSolution(id: Int): AsosResult<CaseProgress>

    suspend fun resetSolution(id: Int)

    suspend fun toggleBookmark(id: Int)
}
