package uz.asos.feature.home

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import uz.asos.core.common.result.AsosResult
import uz.asos.core.testing.MainDispatcherRule
import uz.asos.core.testing.repository.FakeAuthRepository
import uz.asos.feature.home.data.FakeHomeRepository
import uz.asos.feature.home.data.HomeSampleData
import uz.asos.feature.home.data.model.DailyAnswer
import java.time.Instant

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val home = FakeHomeRepository()
    private val auth = FakeAuthRepository(signedIn = true)

    private fun createViewModel() = HomeViewModel(home, auth)

    @Test
    fun `loads the feed and formats the header`() {
        val state = createViewModel().uiState.value

        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals("Xayrli kech", state.greeting)
        assertEquals("Aziza", state.userName)
        assertEquals("AK", state.initials)
        assertTrue(state.hasUnreadNotifications)
    }

    @Test
    fun `article of the day is formatted like the design`() {
        val article = createViewModel().uiState.value.articleOfDay

        assertEquals("33", article?.articleKey)
        assertEquals("VII bob · Shaxsiy huquq va erkinliklar", article?.meta)
        assertEquals("33-modda, 1-qism", article?.reference)
        assertEquals("“Har kim fikrlash, so‘z va e’tiqod erkinligi huquqiga ega.”", article?.quote)
    }

    @Test
    fun `shows six topic tiles`() {
        val topics = createViewModel().uiState.value.topics

        assertEquals(HomeViewModel.TOPIC_TILES, topics.size)
        assertEquals("11 modda", topics.first().countLabel)
    }

    @Test
    fun `continue and case cards are formatted`() {
        val state = createViewModel().uiState.value

        assertEquals("50", state.continueReading?.articleKey)
        assertEquals("8 / 13 modda o‘qildi", state.continueReading?.progressLabel)
        assertEquals("62%", state.continueReading?.percentLabel)
        assertEquals("Keyingi: 50-modda · Ta’lim →", state.continueReading?.nextLabel)
        assertEquals("2 / 3 bosqich", state.caseOfWeek?.progressLabel)
    }

    @Test
    fun `daily quiz header, streak and help labels`() {
        val state = createViewModel().uiState.value

        assertEquals("Shaxsiy huquqlar · O‘rta", state.dailyQuiz?.meta)
        assertEquals("7 kun ketma-ket", state.dailyQuiz?.streakLabel)
        assertEquals("To‘g‘ri javob uchun +20 ball", state.dailyQuiz?.footer)
        assertEquals("3 advokat onlayn", state.help.lawyersOnlineLabel)
        assertEquals("Kun atamasi: Daxlsizlik", state.help.termOfDayLabel)
    }

    @Test
    fun `first load error shows the error state`() {
        home.feedResult = AsosResult.Error(message = "Internet aloqasi yo‘q.")

        val state = createViewModel().uiState.value

        assertFalse(state.isLoading)
        assertEquals("Internet aloqasi yo‘q.", state.errorMessage)
    }

    @Test
    fun `retry after an error loads the feed`() {
        home.feedResult = AsosResult.Error(message = "Internet aloqasi yo‘q.")
        val viewModel = createViewModel()

        home.feedResult = AsosResult.Success(HomeSampleData.feed)
        viewModel.onAction(HomeAction.OnRetry)

        assertNull(viewModel.uiState.value.errorMessage)
        assertEquals("33", viewModel.uiState.value.articleOfDay?.articleKey)
    }

    @Test
    fun `correct answer marks the option and updates the streak`() {
        val viewModel = createViewModel()

        viewModel.onAction(HomeAction.OnQuizOptionClick(262))

        val quiz = viewModel.uiState.value.dailyQuiz!!
        assertEquals(listOf(262), home.answeredOptionIds)
        assertTrue(quiz.isAnswered)
        assertEquals(QuizOptionState.CORRECT, quiz.options.first { it.id == 262 }.state)
        assertEquals("8 kun ketma-ket", quiz.streakLabel)
        assertEquals("To‘g‘ri javob! +20 ball", quiz.footer)
    }

    @Test
    fun `wrong answer marks both options`() {
        home.answerResult = AsosResult.Success(
            DailyAnswer(isCorrect = false, correctOptionId = 262, pointsEarned = 0, streakDays = 0, explanation = "48 soat."),
        )
        val viewModel = createViewModel()

        viewModel.onAction(HomeAction.OnQuizOptionClick(261))

        val quiz = viewModel.uiState.value.dailyQuiz!!
        assertEquals(QuizOptionState.WRONG, quiz.options.first { it.id == 261 }.state)
        assertEquals(QuizOptionState.CORRECT, quiz.options.first { it.id == 262 }.state)
        assertEquals("48 soat.", quiz.explanation)
    }

    @Test
    fun `answered quiz ignores more taps`() {
        val viewModel = createViewModel()

        viewModel.onAction(HomeAction.OnQuizOptionClick(262))
        viewModel.onAction(HomeAction.OnQuizOptionClick(263))

        assertEquals(listOf(262), home.answeredOptionIds)
    }

    @Test
    fun `guest is asked to sign in before answering`() = runTest {
        auth.setSignedIn(false)
        home.feedResult = AsosResult.Success(HomeSampleData.guestFeed)
        val viewModel = createViewModel()

        viewModel.events.test {
            viewModel.onAction(HomeAction.OnQuizOptionClick(262))
            assertEquals(HomeEvent.SignInRequired, awaitItem())
        }
        assertTrue(home.answeredOptionIds.isEmpty())
        assertNull(viewModel.uiState.value.userName)
    }

    @Test
    fun `saving the article of the day uses its number`() = runTest {
        val viewModel = createViewModel()

        viewModel.events.test {
            viewModel.onAction(HomeAction.OnSaveArticleClick)
            assertEquals(HomeEvent.ArticleSaved, awaitItem())
        }
        assertEquals(listOf(33), home.savedArticleNumbers)
        assertTrue(viewModel.uiState.value.isArticleSaved)
    }

    @Test
    fun `failed save is rolled back`() = runTest {
        home.saveResult = AsosResult.Error(message = "Avval tizimga kiring.")
        val viewModel = createViewModel()

        viewModel.events.test {
            viewModel.onAction(HomeAction.OnSaveArticleClick)
            assertEquals(HomeEvent.Message("Avval tizimga kiring."), awaitItem())
        }
        assertFalse(viewModel.uiState.value.isArticleSaved)
    }

    @Test
    fun `signing in reloads the feed`() {
        auth.setSignedIn(false)
        val viewModel = createViewModel()
        val requestsBefore = home.feedRequests

        auth.setSignedIn(true)

        assertEquals(requestsBefore + 1, home.feedRequests)
        assertTrue(viewModel.uiState.value.isSignedIn)
    }

    @Test
    fun `relative time uses hours and days`() {
        val now = Instant.parse("2026-11-20T14:17:55Z")

        assertEquals("2 soat oldin", relativeTime("2026-11-20 12:17:55", now))
        assertEquals("3 kun oldin", relativeTime("2026-11-17 14:17:55", now))
        assertNull(relativeTime("bad", now))
    }
}
