/*
 * DictionaryViewModelTest.kt
 * Module: :feature:dictionary   ·   Work package: WP-06
 * Screen: Konstitutsiyaviy lug‘at (design artboard: Lugat.dc.html)
 *
 * Unit tests for DictionaryViewModel using fakes from core:testing (no network).
 */
package uz.asos.feature.dictionary

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import uz.asos.core.common.result.AsosResult
import uz.asos.core.model.dictionary.TermQuery
import uz.asos.core.model.user.BookmarkType
import uz.asos.core.testing.MainDispatcherRule
import uz.asos.core.testing.repository.FakeAuthRepository
import uz.asos.core.testing.repository.FakeBookmarkRepository
import uz.asos.core.testing.repository.FakeDictionaryRepository

@OptIn(ExperimentalCoroutinesApi::class)
class DictionaryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeDictionaryRepository()
    private val bookmarks = FakeBookmarkRepository()
    private val auth = FakeAuthRepository(signedIn = false)

    private fun createViewModel(termSlug: String = "") = DictionaryViewModel(
        savedStateHandle = SavedStateHandle(mapOf("termSlug" to termSlug)),
        dictionaryRepository = repository,
        bookmarkRepository = bookmarks,
        authRepository = auth,
    )

    private val DictionaryUiState.groups get() = (list as TermListState.Loaded).groups
    private val DictionaryUiState.rows get() = groups.flatMap { it.items }

    @Test
    fun `loads the term of the day and the list grouped by letter`() {
        val state = createViewModel().uiState.value

        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        val card = state.termOfTheDay!!
        assertEquals("Daxlsizlik", card.term)
        assertEquals("ot · arabcha “daxl” — tegish, aralashish", card.meta)
        assertEquals(listOf("27-modda", "31-modda"), card.articles.map { it.label })
        assertTrue(state.showTermOfTheDay)

        // Groups follow the alphabet rail: A, B, D, … then Sh, Ch (Uzbek order).
        assertEquals(listOf("A", "B", "D", "F", "K", "Sh", "Ch"), state.groups.map { it.letter })
        assertEquals(5, state.groups.first { it.letter == "D" }.items.size)
    }

    @Test
    fun `article labels are formatted like the design`() {
        val rows = createViewModel().uiState.value.rows.associateBy { it.slug }

        assertEquals("27 · 31-moddalar", rows.getValue("daxlsizlik").articlesLabel)
        assertEquals("1-modda", rows.getValue("davlat-suvereniteti").articlesLabel)
        assertNull(rows.getValue("demokratiya").articlesLabel)
    }

    @Test
    fun `rail has the whole alphabet and enables only letters with terms`() {
        val letters = createViewModel().uiState.value.letters

        assertEquals(29, letters.size)
        assertTrue(letters.first { it.letter == "D" }.enabled)
        assertFalse(letters.first { it.letter == "G" }.enabled)
        assertTrue(letters.first { it.letter == "Sh" }.enabled)
    }

    @Test
    fun `failed first load shows the error, retry recovers`() {
        repository.termsResult = AsosResult.Error(message = "Internet aloqasi yo‘q.")
        val viewModel = createViewModel()
        assertEquals("Internet aloqasi yo‘q.", viewModel.uiState.value.errorMessage)

        repository.termsResult = null
        viewModel.onAction(DictionaryAction.OnRetry)

        assertNull(viewModel.uiState.value.errorMessage)
        assertTrue(viewModel.uiState.value.list is TermListState.Loaded)
    }

    @Test
    fun `search waits for typing to stop and hides the term of the day`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()
        val callsBefore = repository.termQueries.size

        viewModel.onAction(DictionaryAction.OnQueryChange("dav"))
        assertEquals(callsBefore, repository.termQueries.size)
        assertFalse(viewModel.uiState.value.showTermOfTheDay)
        advanceUntilIdle()

        assertEquals(TermQuery(text = "dav"), repository.termQueries.last())
        assertEquals(listOf("Davlat suvereniteti", "Davlat tili", "Dunyoviy davlat"), viewModel.uiState.value.rows.map { it.term })
    }

    @Test
    fun `search with no results gives an empty list`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()
        viewModel.onAction(DictionaryAction.OnQueryChange("zzz"))
        advanceUntilIdle()

        assertTrue((viewModel.uiState.value.list as TermListState.Loaded).isEmpty)
    }

    @Test
    fun `guest on Saqlangan is asked to sign in without calling the API`() {
        val viewModel = createViewModel()
        val callsBefore = repository.termQueries.size

        viewModel.onAction(DictionaryAction.OnFilterSelect(DictionaryFilter.SAVED))

        assertEquals(TermListState.NeedsSignIn, viewModel.uiState.value.list)
        assertEquals(callsBefore, repository.termQueries.size)
        assertNull(viewModel.uiState.value.savedCount)
    }

    @Test
    fun `signed in user sees the saved count and saved terms`() {
        auth.setSignedIn(true)
        val viewModel = createViewModel()
        assertEquals(2, viewModel.uiState.value.savedCount)

        viewModel.onAction(DictionaryAction.OnFilterSelect(DictionaryFilter.SAVED))

        assertEquals(TermQuery(savedOnly = true), repository.termQueries.last())
        assertEquals(listOf("Daxlsizlik", "Demokratiya"), viewModel.uiState.value.rows.map { it.term })
        assertFalse(viewModel.uiState.value.showTermOfTheDay)
    }

    @Test
    fun `signing in reloads the list`() {
        val viewModel = createViewModel()
        val callsBefore = repository.termQueries.size

        auth.setSignedIn(true)

        assertTrue(viewModel.uiState.value.isSignedIn)
        assertTrue(repository.termQueries.size > callsBefore)
        assertEquals(2, viewModel.uiState.value.savedCount)
    }

    @Test
    fun `guest tapping save gets a sign-in prompt`() = runTest {
        val viewModel = createViewModel()

        viewModel.events.test {
            viewModel.onAction(DictionaryAction.OnBookmarkClick(id = 3, slug = "davlat-tili", isSaved = false))
            assertEquals(DictionaryEvent.SignInRequired, awaitItem())
        }
        assertTrue(bookmarks.savedTermSlugs.isEmpty())
    }

    @Test
    fun `saving a term updates the row, the card and the badge`() {
        auth.setSignedIn(true)
        val viewModel = createViewModel()

        viewModel.onAction(DictionaryAction.OnBookmarkClick(id = 3, slug = "davlat-tili", isSaved = false))

        assertEquals(listOf("davlat-tili"), bookmarks.savedTermSlugs)
        assertTrue(viewModel.uiState.value.rows.first { it.slug == "davlat-tili" }.isSaved)
        assertEquals(3, viewModel.uiState.value.savedCount)
    }

    @Test
    fun `unsaving uses the term id and also updates the term of the day`() {
        auth.setSignedIn(true)
        val viewModel = createViewModel()

        viewModel.onAction(DictionaryAction.OnBookmarkClick(id = 1, slug = "daxlsizlik", isSaved = true))

        assertEquals(listOf(BookmarkType.TERM to 1), bookmarks.removed)
        assertFalse(viewModel.uiState.value.termOfTheDay!!.isSaved)
        assertFalse(viewModel.uiState.value.rows.first { it.slug == "daxlsizlik" }.isSaved)
        assertEquals(1, viewModel.uiState.value.savedCount)
    }

    @Test
    fun `bookmark failure rolls back and shows the message`() = runTest {
        auth.setSignedIn(true)
        bookmarks.result = AsosResult.Error(message = "Serverda xatolik yuz berdi.")
        val viewModel = createViewModel()

        viewModel.events.test {
            viewModel.onAction(DictionaryAction.OnBookmarkClick(id = 3, slug = "davlat-tili", isSaved = false))
            assertEquals(DictionaryEvent.Message("Serverda xatolik yuz berdi."), awaitItem())
        }
        assertFalse(viewModel.uiState.value.rows.first { it.slug == "davlat-tili" }.isSaved)
        assertEquals(2, viewModel.uiState.value.savedCount)
    }

    @Test
    fun `opening a term from the route shows it in the sheet`() {
        val viewModel = createViewModel(termSlug = "daxlsizlik")

        val opened = viewModel.uiState.value.openedTerm as OpenedTermState.Loaded
        assertEquals("Daxlsizlik", opened.term.term)
        assertEquals(listOf("daxlsizlik"), repository.requestedSlugs)
    }

    @Test
    fun `unknown term shows the API message, dismiss closes the sheet`() {
        val viewModel = createViewModel()

        viewModel.onAction(DictionaryAction.OnTermClick("yoq-atama"))
        val failed = viewModel.uiState.value.openedTerm as OpenedTermState.Failed
        assertEquals("Bunday atama lug‘atda yo‘q.", failed.message)

        viewModel.onAction(DictionaryAction.OnTermSheetDismiss)
        assertNull(viewModel.uiState.value.openedTerm)
    }
}
