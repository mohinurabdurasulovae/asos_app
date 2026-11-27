package uz.asos.feature.cases.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import uz.asos.core.common.result.AsosErrorCode
import uz.asos.core.common.result.AsosResult
import uz.asos.feature.cases.data.model.CaseItem
import uz.asos.feature.cases.data.model.CaseProgress
import uz.asos.feature.cases.data.model.CaseStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class InMemoryCasesRepository @Inject constructor() : CasesRepository {

    private val cases: List<CaseItem> = CasesSampleData.allCases

    private val progress = MutableStateFlow(
        cases.associate { item ->
            val solved = item.initialStatus == CaseStatus.YECHILGAN
            item.id to CaseProgress(
                selectedArticles = if (solved) item.correctArticleNumbers else emptySet(),
                userSolution = if (solved) item.sampleSolution else "",
                isSubmitted = solved,
                isCorrect = solved,
                status = item.initialStatus,
            )
        },
    )

    override suspend fun getCases(): AsosResult<List<CaseItem>> = AsosResult.Success(cases)

    override suspend fun getCase(id: Int): AsosResult<CaseItem> =
        cases.firstOrNull { it.id == id }
            ?.let { AsosResult.Success(it) }
            ?: AsosResult.Error(
                message = "Kazus topilmadi",
                code = AsosErrorCode.NOT_FOUND,
            )

    override fun observeProgress(): Flow<Map<Int, CaseProgress>> = progress.asStateFlow()

    override suspend fun saveDraft(id: Int, selectedArticles: Set<Int>, solution: String) {
        edit(id) { current ->
            current.copy(
                selectedArticles = selectedArticles,
                userSolution = solution,
                status = when {
                    current.isSubmitted -> current.status
                    selectedArticles.isNotEmpty() || solution.isNotBlank() -> CaseStatus.JARAYONDA
                    else -> CaseStatus.YANGI
                },
            )
        }
    }

    override suspend fun checkSolution(id: Int): AsosResult<CaseProgress> {
        val case = cases.firstOrNull { it.id == id }
            ?: return AsosResult.Error(message = "Kazus topilmadi", code = AsosErrorCode.NOT_FOUND)

        val checked = edit(id) { current ->
            val correct = current.selectedArticles == case.correctArticleNumbers
            current.copy(
                isSubmitted = true,
                isCorrect = correct,
                status = if (correct) CaseStatus.YECHILGAN else CaseStatus.JARAYONDA,
            )
        }
        return AsosResult.Success(checked)
    }

    override suspend fun resetSolution(id: Int) {
        edit(id) { current ->
            current.copy(
                selectedArticles = emptySet(),
                userSolution = "",
                isSubmitted = false,
                isCorrect = false,
                status = CaseStatus.JARAYONDA,
            )
        }
    }

    override suspend fun toggleBookmark(id: Int) {
        edit(id) { current -> current.copy(isBookmarked = !current.isBookmarked) }
    }

    private inline fun edit(id: Int, transform: (CaseProgress) -> CaseProgress): CaseProgress {
        var result = CaseProgress()
        progress.update { all ->
            result = transform(all[id] ?: CaseProgress())
            all + (id to result)
        }
        return result
    }
}
