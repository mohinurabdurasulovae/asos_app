package uz.asos.feature.constitution.browse

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import uz.asos.core.common.result.AsosResult
import uz.asos.core.testing.MainDispatcherRule
import uz.asos.core.testing.data.SampleData
import uz.asos.core.testing.repository.FakeAuthRepository
import uz.asos.core.testing.repository.FakeConstitutionRepository

@OptIn(ExperimentalCoroutinesApi::class)
class ConstitutionViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeConstitutionRepository()
    private val auth = FakeAuthRepository(signedIn = false)

    private fun createViewModel() = ConstitutionViewModel(repository, auth)

    @Test
    fun `loads the tree and formats the header`() {
        val viewModel = createViewModel()
        val state = viewModel.uiState.value

        assertEquals(false, state.isLoading)
        assertNull(state.errorMessage)
        assertEquals("2023-yil tahriri · 6 bo‘lim · 27 bob · 155 modda", state.subtitle)
        assertEquals(6, state.sections.size)
        assertEquals("II BO‘LIM", state.sections[1].overline)
        assertEquals("7 bob · 19–64-moddalar", state.sections[1].meta)
        assertEquals("154–155-moddalar", state.sections[5].meta)
        assertEquals(4, state.topics.size)
    }

    @Test
    fun `chapter in progress is highlighted with its reading count`() {
        val viewModel = createViewModel()
        val chapterIX = viewModel.uiState.value.sections[1].chapters.first { it.roman == "IX" }

        assertEquals(ReadState.IN_PROGRESS, chapterIX.state)
        assertTrue(chapterIX.isHighlighted)
        assertEquals("41–53-moddalar · 8/13 o‘qildi", chapterIX.meta)
        assertEquals(ReadState.READ, viewModel.uiState.value.sections[1].chapters.first { it.roman == "VII" }.state)
    }

    @Test
    fun `guest sees no continue card and section II is open`() {
        val viewModel = createViewModel()

        assertNull(viewModel.uiState.value.continueReading)
        assertEquals(setOf("II"), viewModel.uiState.value.expandedSections)
    }

    @Test
    fun `signed in user gets the continue card`() {
        repository.overviewResult = AsosResult.Success(SampleData.readingOverview)
        auth.setSignedIn(true)

        val continueReading = createViewModel().uiState.value.continueReading

        assertEquals("50", continueReading?.articleKey)
        assertEquals("Ta’lim olish huquqi", continueReading?.title)
        assertEquals("IX bob · 3 daqiqa qoldi", continueReading?.meta)
    }

    @Test
    fun `first load error shows the message and retry recovers`() {
        repository.constitutionResult = AsosResult.Error(message = "Internet aloqasi yo‘q.")
        val viewModel = createViewModel()
        assertEquals("Internet aloqasi yo‘q.", viewModel.uiState.value.errorMessage)

        repository.constitutionResult = AsosResult.Success(SampleData.constitutionTree)
        viewModel.onAction(ConstitutionAction.OnRetry)

        assertNull(viewModel.uiState.value.errorMessage)
        assertEquals(6, viewModel.uiState.value.sections.size)
    }

    @Test
    fun `opening a chapter loads its articles`() {
        val viewModel = createViewModel()

        viewModel.onAction(ConstitutionAction.OnChapterToggle("IX"))

        assertEquals(listOf("IX"), repository.requestedChapters)
        val list = viewModel.uiState.value.chapterArticles["IX"] as ArticleListState.Loaded
        assertEquals(13, list.items.size)
        val article50 = list.items.first { it.key == "50" }
        assertEquals("Ta’lim olish huquqi", article50.title)
        assertEquals("IX bob · 2 daqiqa", article50.meta)
    }

    @Test
    fun `section without chapters loads its articles directly`() {
        val viewModel = createViewModel()

        viewModel.onAction(ConstitutionAction.OnSectionToggle("VI"))

        assertEquals("VI", repository.articleQueries.last().section)
        assertTrue(viewModel.uiState.value.chapterArticles[ConstitutionViewModel.sectionKey("VI")] is ArticleListState.Loaded)
    }

    @Test
    fun `selecting a topic lists its articles`() {
        val viewModel = createViewModel()

        viewModel.onAction(ConstitutionAction.OnTopicSelect("talim"))

        assertEquals("talim", repository.articleQueries.last().topic)
        assertTrue(viewModel.uiState.value.articleList is ArticleListState.Loaded)

        viewModel.onAction(ConstitutionAction.OnTopicSelect(null))
        assertEquals(ArticleListState.Hidden, viewModel.uiState.value.articleList)
    }

    @Test
    fun `saved tab asks guests to sign in`() {
        val viewModel = createViewModel()

        viewModel.onAction(ConstitutionAction.OnSavedClick)

        assertEquals(ConstitutionTab.SAVED, viewModel.uiState.value.selectedTab)
        assertEquals(ArticleListState.NeedsSignIn, viewModel.uiState.value.articleList)
        assertTrue(repository.articleQueries.isEmpty())
    }

    @Test
    fun `saved tab loads saved articles when signed in`() {
        auth.setSignedIn(true)
        val viewModel = createViewModel()

        viewModel.onAction(ConstitutionAction.OnTabSelect(ConstitutionTab.SAVED))

        assertTrue(repository.articleQueries.last().savedOnly)
    }

    @Test
    fun `search waits for typing to stop`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()

        viewModel.onAction(ConstitutionAction.OnQueryChange("ta"))
        viewModel.onAction(ConstitutionAction.OnQueryChange("ta’lim"))
        assertTrue(repository.articleQueries.isEmpty())

        advanceUntilIdle()

        assertEquals(1, repository.articleQueries.size)
        assertEquals("ta’lim", repository.articleQueries.single().text)
    }
}
