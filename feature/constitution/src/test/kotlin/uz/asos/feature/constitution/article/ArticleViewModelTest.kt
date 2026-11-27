package uz.asos.feature.constitution.article

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import uz.asos.core.common.result.AsosResult
import uz.asos.core.model.constitution.ReadProgressResult
import uz.asos.core.model.constitution.ReadingProgress
import uz.asos.core.model.constitution.ReadingStatus
import uz.asos.core.model.user.BookmarkType
import uz.asos.core.model.user.ReaderTextSize
import uz.asos.core.testing.MainDispatcherRule
import uz.asos.core.testing.data.SampleData
import uz.asos.core.testing.repository.FakeAuthRepository
import uz.asos.core.testing.repository.FakeBookmarkRepository
import uz.asos.core.testing.repository.FakeConstitutionRepository
import uz.asos.core.testing.repository.FakeUserRepository

class ArticleViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val constitution = FakeConstitutionRepository()
    private val bookmarks = FakeBookmarkRepository()
    private val user = FakeUserRepository()
    private val auth = FakeAuthRepository(signedIn = true)

    private fun createViewModel(key: String = "50") = ArticleViewModel(
        savedStateHandle = SavedStateHandle(mapOf("key" to key)),
        constitutionRepository = constitution,
        bookmarkRepository = bookmarks,
        userRepository = user,
        authRepository = auth,
    )

    private val ArticleViewModel.success: ArticleUiState.Success
        get() = uiState.value as ArticleUiState.Success

    @Test
    fun `loads the article ready for the screen`() {
        val article = createViewModel().success.article

        assertEquals("50-modda", article.heading)
        assertEquals("Ta’lim olish huquqi", article.title)
        assertEquals("II", article.sectionRoman)
        assertEquals("IX", article.chapterRoman)
        assertEquals("01.05.2023", article.effectiveFrom)
        assertEquals("4 daqiqa", article.readingTime)
        assertEquals(3, article.parts.size)
        assertEquals(listOf("uzluksiz ta’lim tizimi"), article.parts[1].highlights)
        assertEquals("ijtimoiy-huquqlar", article.quizId)
        assertEquals(49, article.previousNumber)
        assertEquals(51, article.nextNumber)
    }

    @Test
    fun `opening an unread article saves reading progress`() {
        createViewModel()

        assertEquals(listOf("50" to ReadingStatus.IN_PROGRESS), constitution.progressUpdates)
    }

    @Test
    fun `an article already read is not reported again`() {
        constitution.articleResult = AsosResult.Success(
            SampleData.article50.copy(progress = ReadingProgress(ReadingStatus.READ, 120, null)),
        )
        val viewModel = createViewModel()

        viewModel.onAction(ArticleAction.OnReachedEnd)

        assertTrue(constitution.progressUpdates.isEmpty())
    }

    @Test
    fun `load error shows the API message`() {
        constitution.articleResult = AsosResult.Error(message = "Ma’lumot topilmadi.", code = "not_found", httpStatus = 404)

        val state = createViewModel().uiState.value

        assertEquals(ArticleUiState.Error("Ma’lumot topilmadi."), state)
    }

    @Test
    fun `reaching the end marks it read and announces the points`() = runTest {
        constitution.progressResult = AsosResult.Success(ReadProgressResult(pointsEarned = 10, totalPoints = 1250, streakDays = 7))
        val viewModel = createViewModel()

        viewModel.events.test {
            viewModel.onAction(ArticleAction.OnReachedEnd)
            assertEquals(ArticleEvent.PointsEarned(10), awaitItem())
        }
        assertEquals("50" to ReadingStatus.READ, constitution.progressUpdates.last())

        viewModel.onAction(ArticleAction.OnReachedEnd)
        assertEquals(2, constitution.progressUpdates.size)
    }

    @Test
    fun `bookmark saves the article when signed in`() {
        val viewModel = createViewModel()

        viewModel.onAction(ArticleAction.OnBookmarkClick)

        assertTrue(viewModel.success.isSaved)
        assertFalse(viewModel.success.isBookmarkBusy)
        assertEquals(listOf(50), bookmarks.savedArticleIds)
    }

    @Test
    fun `bookmark on a saved article removes it`() {
        constitution.articleResult = AsosResult.Success(SampleData.article50.copy(isSaved = true))
        val viewModel = createViewModel()

        viewModel.onAction(ArticleAction.OnBookmarkClick)

        assertFalse(viewModel.success.isSaved)
        assertEquals(listOf(BookmarkType.ARTICLE to 50), bookmarks.removed)
    }

    @Test
    fun `bookmark failure rolls back and shows the message`() = runTest {
        bookmarks.result = AsosResult.Error(message = "Serverda xatolik yuz berdi.")
        val viewModel = createViewModel()

        viewModel.events.test {
            viewModel.onAction(ArticleAction.OnBookmarkClick)
            assertEquals(ArticleEvent.Message("Serverda xatolik yuz berdi."), awaitItem())
        }
        assertFalse(viewModel.success.isSaved)
    }

    @Test
    fun `guest is asked to sign in before saving`() = runTest {
        auth.setSignedIn(false)
        val viewModel = createViewModel()

        viewModel.events.test {
            viewModel.onAction(ArticleAction.OnBookmarkClick)
            assertEquals(ArticleEvent.SignInRequired, awaitItem())
        }
        assertTrue(bookmarks.savedArticleIds.isEmpty())
        assertFalse(viewModel.success.isSaved)
    }

    @Test
    fun `text size follows the saved preference`() {
        val viewModel = createViewModel()
        assertEquals(ReaderTextSize.MEDIUM, viewModel.success.textSize)

        viewModel.onAction(ArticleAction.OnTextSizeSelect(ReaderTextSize.LARGE))

        assertEquals(ReaderTextSize.LARGE, viewModel.success.textSize)
    }

    @Test
    fun `full text toggles`() {
        val viewModel = createViewModel()

        viewModel.onAction(ArticleAction.OnToggleFullText)

        assertTrue(viewModel.success.showFullText)
    }
}
