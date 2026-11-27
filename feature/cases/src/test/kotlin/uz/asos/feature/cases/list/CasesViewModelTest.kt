package uz.asos.feature.cases.list

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import uz.asos.core.common.result.AsosResult
import uz.asos.core.testing.MainDispatcherRule
import uz.asos.feature.cases.data.FakeCasesRepository
import uz.asos.feature.cases.data.model.CaseDifficulty
import uz.asos.feature.cases.data.model.CaseStatus
import uz.asos.feature.cases.data.model.CaseTopic

class CasesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeCasesRepository()

    private fun viewModel() = CasesViewModel(repository)

    @Test
    fun `loads the cases and lifts the featured one out of the list`() {
        val state = viewModel().uiState.value

        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertNotNull(state.featured)
        assertEquals(14, state.featured?.id)
        assertEquals(4, state.cases.size)
        assertTrue(state.cases.none { it.id == 14 })
    }

    @Test
    fun `formats the featured card like the design`() {
        val featured = viewModel().uiState.value.featured

        assertEquals("№ 14", featured?.numberLabel)
        assertEquals("Direktor va telefon", featured?.title)
        assertEquals("≈ 6 daqiqa", featured?.durationLabel)
        assertFalse(featured?.isDone ?: true)
    }

    @Test
    fun `topic filter keeps only that category`() {
        val vm = viewModel()

        vm.onAction(CasesAction.OnTopicSelect(CaseTopic.TALIM))

        val state = vm.uiState.value
        assertTrue(state.cases.all { it.topic == CaseTopic.TALIM })
        assertNull(state.featured)
    }

    @Test
    fun `difficulty filter keeps only that level`() {
        val vm = viewModel()

        vm.onAction(CasesAction.OnDifficultySelect(CaseDifficulty.MURAKKAB))

        val state = vm.uiState.value
        assertTrue(state.cases.isNotEmpty())
        assertTrue(state.cases.all { it.difficulty == CaseDifficulty.MURAKKAB })
    }

    @Test
    fun `status filter uses the progress the user has made`() {
        val vm = viewModel()

        vm.onAction(CasesAction.OnStatusFilterSelect(CaseStatus.YECHILGAN))

        val state = vm.uiState.value
        assertTrue(state.cases.isNotEmpty())
        assertTrue(state.cases.all { it.status == CaseStatus.YECHILGAN })
        assertTrue(state.hasSheetFilter)
    }

    @Test
    fun `only bookmarked hides everything until a case is saved`() {
        val vm = viewModel()

        vm.onAction(CasesAction.OnOnlyBookmarkedChange(true))
        assertTrue(vm.uiState.value.isEmpty)

        repository.markBookmarked(13)
        assertEquals(listOf(13), vm.uiState.value.cases.map { it.id })
    }

    @Test
    fun `clearing the filters brings every case back`() {
        val vm = viewModel()
        vm.onAction(CasesAction.OnTopicSelect(CaseTopic.MEHNAT))
        vm.onAction(CasesAction.OnOnlyBookmarkedChange(true))

        vm.onAction(CasesAction.OnClearFilters)

        val state = vm.uiState.value
        assertNotNull(state.featured)
        assertEquals(4, state.cases.size)
        assertFalse(state.hasSheetFilter)
    }

    @Test
    fun `solving a case updates its row in the list`() {
        val vm = viewModel()
        assertEquals(CaseStatus.YANGI, vm.uiState.value.cases.first { it.id == 12 }.status)

        repository.markSolved(12)

        assertEquals(CaseStatus.YECHILGAN, vm.uiState.value.cases.first { it.id == 12 }.status)
    }

    @Test
    fun `shows the error when nothing is loaded and retries`() {
        repository.casesResult = AsosResult.Error(message = "Internet aloqasi yo‘q")
        val vm = viewModel()

        assertEquals("Internet aloqasi yo‘q", vm.uiState.value.errorMessage)
        assertTrue(vm.uiState.value.isEmpty)

        repository.casesResult = null
        vm.onAction(CasesAction.OnRetry)

        assertNull(vm.uiState.value.errorMessage)
        assertNotNull(vm.uiState.value.featured)
    }

    @Test
    fun `the filter sheet opens and closes`() {
        val vm = viewModel()
        assertFalse(vm.uiState.value.isFilterSheetOpen)

        vm.onAction(CasesAction.OnFilterSheetOpen)
        assertTrue(vm.uiState.value.isFilterSheetOpen)

        vm.onAction(CasesAction.OnFilterSheetDismiss)
        assertFalse(vm.uiState.value.isFilterSheetOpen)
    }
}
