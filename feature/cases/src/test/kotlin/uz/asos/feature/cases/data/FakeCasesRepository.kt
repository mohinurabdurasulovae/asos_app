package uz.asos.feature.cases.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import uz.asos.core.common.result.AsosErrorCode
import uz.asos.core.common.result.AsosResult
import uz.asos.feature.cases.data.model.CaseItem
import uz.asos.feature.cases.data.model.CaseProgress
import uz.asos.feature.cases.data.model.CaseStatus

internal class FakeCasesRepository : CasesRepository {

    var cases: List<CaseItem> = CasesSampleData.allCases
    var casesResult: AsosResult<List<CaseItem>>? = null

    var getCasesCalls = 0
    var checkCalls = 0
    var resetCalls = 0
    var draftCalls = 0
    var bookmarkCalls = 0

    private val progress = MutableStateFlow(
        CasesSampleData.allCases.associate { item ->
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

    override suspend fun getCases(): AsosResult<List<CaseItem>> {
        getCasesCalls++
        return casesResult ?: AsosResult.Success(cases)
    }

    override suspend fun getCase(id: Int): AsosResult<CaseItem> =
        cases.firstOrNull { it.id == id }
            ?.let { AsosResult.Success(it) }
            ?: AsosResult.Error(message = "Kazus topilmadi", code = AsosErrorCode.NOT_FOUND)

    override fun observeProgress(): Flow<Map<Int, CaseProgress>> = progress.asStateFlow()

    override suspend fun saveDraft(id: Int, selectedArticles: Set<Int>, solution: String) {
        draftCalls++
        edit(id) {
            it.copy(
                selectedArticles = selectedArticles,
                userSolution = solution,
                status = if (it.isSubmitted) {
                    it.status
                } else if (selectedArticles.isNotEmpty() || solution.isNotBlank()) {
                    CaseStatus.JARAYONDA
                } else {
                    CaseStatus.YANGI
                },
            )
        }
    }

    override suspend fun checkSolution(id: Int): AsosResult<CaseProgress> {
        checkCalls++
        val case = cases.firstOrNull { it.id == id }
            ?: return AsosResult.Error(message = "Kazus topilmadi", code = AsosErrorCode.NOT_FOUND)
        val checked = edit(id) {
            val correct = it.selectedArticles == case.correctArticleNumbers
            it.copy(
                isSubmitted = true,
                isCorrect = correct,
                status = if (correct) CaseStatus.YECHILGAN else CaseStatus.JARAYONDA,
            )
        }
        return AsosResult.Success(checked)
    }

    override suspend fun resetSolution(id: Int) {
        resetCalls++
        edit(id) {
            it.copy(
                selectedArticles = emptySet(),
                userSolution = "",
                isSubmitted = false,
                isCorrect = false,
                status = CaseStatus.JARAYONDA,
            )
        }
    }

    override suspend fun toggleBookmark(id: Int) {
        bookmarkCalls++
        edit(id) { it.copy(isBookmarked = !it.isBookmarked) }
    }

    fun progressOf(id: Int): CaseProgress = progress.value[id] ?: CaseProgress()

    fun markBookmarked(id: Int) {
        edit(id) { it.copy(isBookmarked = true) }
    }

    fun markSolved(id: Int) {
        val case = cases.first { it.id == id }
        edit(id) {
            it.copy(
                selectedArticles = case.correctArticleNumbers,
                userSolution = case.sampleSolution.ifEmpty { "Yechim" },
                isSubmitted = true,
                isCorrect = true,
                status = CaseStatus.YECHILGAN,
            )
        }
    }

    private fun edit(id: Int, transform: (CaseProgress) -> CaseProgress): CaseProgress {
        val updated = transform(progress.value[id] ?: CaseProgress())
        progress.value = progress.value + (id to updated)
        return updated
    }
}
