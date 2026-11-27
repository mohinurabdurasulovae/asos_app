package uz.asos.feature.cases.detail

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import uz.asos.core.testing.MainDispatcherRule
import uz.asos.feature.cases.data.FakeCasesRepository
import uz.asos.feature.cases.data.model.CaseStatus

class CaseDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeCasesRepository()

    private fun viewModel(caseId: String = "14") =
        CaseDetailViewModel(SavedStateHandle(mapOf("caseId" to caseId)), repository)

    @Test
    fun `loads the case and formats the header like the design`() {
        val state = viewModel().uiState.value

        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals("Kazus № 14", state.titleLabel)
        assertEquals("Direktor va telefon", state.title)
        assertEquals("T", state.persona?.initials)
        assertEquals(4, state.options.size)
        assertEquals(40, state.rewardPoints)
    }

    @Test
    fun `starts on step one and moves on as the user works`() {
        val vm = viewModel()
        assertEquals(CaseDetailUiState.STEP_SCENARIO, vm.uiState.value.currentStep)

        vm.onAction(CaseDetailAction.OnArticleToggle(31))
        assertEquals(CaseDetailUiState.STEP_ARTICLE, vm.uiState.value.currentStep)

        vm.onAction(CaseDetailAction.OnSolutionChange("Direktor yozishmalarni o‘qishga haqli emas"))
        vm.onAction(CaseDetailAction.OnSubmit)
        assertEquals(CaseDetailUiState.STEP_SOLUTION, vm.uiState.value.currentStep)
    }

    @Test
    fun `toggling an article selects and deselects it`() {
        val vm = viewModel()

        vm.onAction(CaseDetailAction.OnArticleToggle(31))
        assertTrue(vm.uiState.value.options.first { it.option.number == 31 }.isSelected)

        vm.onAction(CaseDetailAction.OnArticleToggle(31))
        assertFalse(vm.uiState.value.options.first { it.option.number == 31 }.isSelected)
    }

    @Test
    fun `cannot submit without an article and a few words`() {
        val vm = viewModel()
        assertFalse(vm.uiState.value.canSubmit)

        vm.onAction(CaseDetailAction.OnArticleToggle(31))
        assertFalse(vm.uiState.value.canSubmit)

        vm.onAction(CaseDetailAction.OnSolutionChange("yo‘q"))
        assertFalse(vm.uiState.value.canSubmit)

        vm.onAction(CaseDetailAction.OnSolutionChange("Direktor haqli emas"))
        assertTrue(vm.uiState.value.canSubmit)
    }

    @Test
    fun `the right article scores the case as solved`() {
        val vm = viewModel()
        vm.onAction(CaseDetailAction.OnArticleToggle(31))
        vm.onAction(CaseDetailAction.OnSolutionChange("31-modda buzilgan"))

        vm.onAction(CaseDetailAction.OnSubmit)

        val state = vm.uiState.value
        assertTrue(state.isSubmitted)
        assertTrue(state.isCorrect)
        assertEquals(CaseStatus.YECHILGAN, repository.progressOf(14).status)
        assertEquals(listOf(31), state.correctArticles.map { it.number })
    }

    @Test
    fun `a wrong article leaves the case in progress`() {
        val vm = viewModel()
        vm.onAction(CaseDetailAction.OnArticleToggle(33))
        vm.onAction(CaseDetailAction.OnSolutionChange("33-modda buzilgan"))

        vm.onAction(CaseDetailAction.OnSubmit)

        val state = vm.uiState.value
        assertTrue(state.isSubmitted)
        assertFalse(state.isCorrect)
        assertEquals(CaseStatus.JARAYONDA, repository.progressOf(14).status)
    }

    @Test
    fun `the answer is locked after checking`() {
        val vm = viewModel()
        vm.onAction(CaseDetailAction.OnArticleToggle(31))
        vm.onAction(CaseDetailAction.OnSolutionChange("31-modda buzilgan"))
        vm.onAction(CaseDetailAction.OnSubmit)

        vm.onAction(CaseDetailAction.OnArticleToggle(27))
        vm.onAction(CaseDetailAction.OnSolutionChange("boshqa matn"))

        val state = vm.uiState.value
        assertFalse(state.options.first { it.option.number == 27 }.isSelected)
        assertEquals("31-modda buzilgan", state.userSolution)
        assertFalse(state.canSubmit)
    }

    @Test
    fun `Qaytadan clears the answer and reopens the form`() {
        val vm = viewModel()
        vm.onAction(CaseDetailAction.OnArticleToggle(31))
        vm.onAction(CaseDetailAction.OnSolutionChange("31-modda buzilgan"))
        vm.onAction(CaseDetailAction.OnSubmit)

        vm.onAction(CaseDetailAction.OnRetry)

        val state = vm.uiState.value
        assertFalse(state.isSubmitted)
        assertFalse(state.isCorrect)
        assertEquals("", state.userSolution)
        assertTrue(state.options.none { it.isSelected })
        assertEquals(1, repository.resetCalls)
    }

    @Test
    fun `bookmark toggles both ways`() {
        val vm = viewModel()
        assertFalse(vm.uiState.value.isBookmarked)

        vm.onAction(CaseDetailAction.OnBookmarkToggle)
        assertTrue(vm.uiState.value.isBookmarked)
        assertTrue(repository.progressOf(14).isBookmarked)

        vm.onAction(CaseDetailAction.OnBookmarkToggle)
        assertFalse(vm.uiState.value.isBookmarked)
    }

    @Test
    fun `offers the first unsolved case as the next one`() {
        assertEquals(12, viewModel().uiState.value.nextCaseId)
    }

    @Test
    fun `the article sheet opens and closes`() {
        val vm = viewModel()
        val article = vm.uiState.value.options.first().option

        vm.onAction(CaseDetailAction.OnArticlePreviewOpen(article))
        assertNotNull(vm.uiState.value.previewArticle)

        vm.onAction(CaseDetailAction.OnArticlePreviewDismiss)
        assertNull(vm.uiState.value.previewArticle)
    }

    @Test
    fun `an unknown case reports an error instead of crashing`() {
        val state = viewModel(caseId = "999").uiState.value

        assertFalse(state.isLoading)
        assertEquals("Kazus topilmadi", state.errorMessage)
    }
}
