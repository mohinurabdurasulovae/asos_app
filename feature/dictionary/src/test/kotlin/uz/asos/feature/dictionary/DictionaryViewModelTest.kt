package uz.asos.feature.dictionary

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import uz.asos.core.common.result.AsosResult
import uz.asos.core.testing.MainDispatcherRule
import uz.asos.core.testing.repository.FakeDictionaryRepository

@OptIn(ExperimentalCoroutinesApi::class)
class DictionaryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeDictionaryRepository()

    private fun viewModel(termSlug: String = "") =
        DictionaryViewModel(SavedStateHandle(mapOf("termSlug" to termSlug)), repository)

    @Test
    fun `loads the terms and groups them under their letter`() {
        val state = viewModel().uiState.value

        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals(listOf("D", "E", "A"), state.groups.map { it.letter })
        assertEquals(4, state.groups.first { it.letter == "D" }.terms.size)
    }

    @Test
    fun `shows the term of the day only on the plain A-Z list`() {
        val vm = viewModel()
        assertTrue(vm.uiState.value.showTermOfTheDay)
        assertEquals("Daxlsizlik", vm.uiState.value.termOfTheDay?.term)

        vm.onAction(DictionaryAction.OnSavedOnlyChange(true))
        assertFalse(vm.uiState.value.showTermOfTheDay)
    }

    @Test
    fun `the saved tab asks the repository for saved terms only`() {
        val vm = viewModel()

        vm.onAction(DictionaryAction.OnSavedOnlyChange(true))

        assertTrue(vm.uiState.value.savedOnly)
        assertTrue(vm.uiState.value.groups.flatMap { it.terms }.all { it.isSaved })
    }

    @Test
    fun `search runs after the user stops typing`() = runTest {
        val vm = viewModel()
        val callsBefore = repository.termsCalls

        vm.onAction(DictionaryAction.OnQueryChange("Dav"))
        vm.onAction(DictionaryAction.OnQueryChange("Davl"))
        vm.onAction(DictionaryAction.OnQueryChange("Davlat"))
        advanceUntilIdle()

        assertEquals(callsBefore + 1, repository.termsCalls)
        assertEquals("Davlat", repository.queries.last())
        assertTrue(
            vm.uiState.value.groups.flatMap { it.terms }.all { it.term.startsWith("Davlat") },
        )
    }

    @Test
    fun `clearing the query reloads at once`() = runTest {
        val vm = viewModel()
        vm.onAction(DictionaryAction.OnQueryChange("Davlat"))
        advanceUntilIdle()

        vm.onAction(DictionaryAction.OnQueryClear)
        advanceUntilIdle()

        assertEquals("", vm.uiState.value.query)
        assertNull(repository.queries.last())
    }

    @Test
    fun `a letter filters the list and tapping it again clears the filter`() {
        val vm = viewModel()

        vm.onAction(DictionaryAction.OnLetterSelect("E"))
        assertEquals("E", vm.uiState.value.selectedLetter)
        assertEquals(listOf("E"), vm.uiState.value.groups.map { it.letter })

        vm.onAction(DictionaryAction.OnLetterSelect("E"))
        assertNull(vm.uiState.value.selectedLetter)
    }

    @Test
    fun `saving a term flips the row and moves the counter`() {
        val vm = viewModel()
        val before = vm.uiState.value.savedCount
        val term = vm.uiState.value.groups.flatMap { it.terms }.first { !it.isSaved }

        vm.onAction(DictionaryAction.OnSaveToggle(term.id, term.isSaved))

        val updated = vm.uiState.value.groups.flatMap { it.terms }.first { it.id == term.id }
        assertTrue(updated.isSaved)
        assertEquals(before + 1, vm.uiState.value.savedCount)
    }

    @Test
    fun `unsaving inside the saved tab drops the row`() {
        val vm = viewModel()
        vm.onAction(DictionaryAction.OnSavedOnlyChange(true))
        val term = vm.uiState.value.groups.flatMap { it.terms }.first()

        vm.onAction(DictionaryAction.OnSaveToggle(term.id, term.isSaved))

        assertTrue(vm.uiState.value.groups.flatMap { it.terms }.none { it.id == term.id })
    }

    @Test
    fun `a route slug opens that term straight away`() {
        val state = viewModel(termSlug = "daxlsizlik").uiState.value

        assertNotNull(state.openTerm)
        assertEquals("Daxlsizlik", state.openTerm?.term)
    }

    @Test
    fun `shows the error when nothing is loaded and retries`() {
        repository.termsResult = AsosResult.Error(message = "Internet aloqasi yo‘q")
        val vm = viewModel()

        assertEquals("Internet aloqasi yo‘q", vm.uiState.value.errorMessage)
        assertTrue(vm.uiState.value.isEmpty)

        repository.termsResult = null
        vm.onAction(DictionaryAction.OnRetry)

        assertNull(vm.uiState.value.errorMessage)
        assertFalse(vm.uiState.value.isEmpty)
    }

    @Test
    fun `a failed term of the day does not take the list down`() {
        repository.termOfTheDayResult = AsosResult.Error(message = "Topilmadi")
        val state = viewModel().uiState.value

        assertNull(state.termOfTheDay)
        assertFalse(state.isEmpty)
        assertNull(state.errorMessage)
    }
}
