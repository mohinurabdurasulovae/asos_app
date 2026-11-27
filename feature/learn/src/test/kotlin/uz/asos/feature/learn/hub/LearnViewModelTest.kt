/*
 * LearnViewModelTest.kt
 * Module: :feature:learn   ·   Work package: WP-07
 */
package uz.asos.feature.learn.hub

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import uz.asos.core.common.result.AsosResult
import uz.asos.core.model.learning.QuizDifficulty
import uz.asos.core.testing.MainDispatcherRule
import uz.asos.core.testing.data.LearnSampleData
import uz.asos.core.testing.repository.FakeAuthRepository
import uz.asos.core.testing.repository.FakeLearningRepository

class LearnViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val learning = FakeLearningRepository()
    private val auth = FakeAuthRepository(signedIn = true)

    private fun viewModel() = LearnViewModel(learning, auth)

    @Test
    fun `loads the hub into header, progress, cases and tests`() {
        val state = viewModel().uiState.value

        assertFalse(state.isLoading)
        assertEquals(7, state.streakDays)
        assertEquals("1 240", state.pointsLabel)
        assertEquals(OverallProgressUi(38, 59, 155, goal = 3, done = 2), state.progress)
        assertEquals(CasesPromoUi(solved = 7, new = 5), state.cases)
        assertEquals(LearnSampleData.quizzes.size, state.quizzes.size)
        assertEquals(4, state.achievements.size)
    }

    @Test
    fun `defaults to O‘rta and filters tests by level`() {
        val vm = viewModel()
        assertEquals(QuizDifficulty.MEDIUM, vm.uiState.value.selectedDifficulty)
        assertEquals(5, vm.uiState.value.visibleQuizzes.size)

        vm.onAction(LearnAction.OnDifficultySelect(QuizDifficulty.EASY))

        assertEquals(listOf("konstitutsiya-asoslari"), vm.uiState.value.visibleQuizzes.map { it.key })
    }

    @Test
    fun `picks the first level with tests when O‘rta is empty`() {
        learning.hubResult = AsosResult.Success(
            LearnSampleData.hub.copy(quizzes = LearnSampleData.quizzes.filter { it.difficulty == QuizDifficulty.EASY }),
        )

        assertEquals(QuizDifficulty.EASY, viewModel().uiState.value.selectedDifficulty)
    }

    @Test
    fun `keeps the user's level after a refresh`() {
        val vm = viewModel()
        vm.onAction(LearnAction.OnDifficultySelect(QuizDifficulty.HARD))
        val callsBeforeResume = learning.hubCalls

        vm.onAction(LearnAction.OnResume)

        assertEquals(QuizDifficulty.HARD, vm.uiState.value.selectedDifficulty)
        assertEquals(callsBeforeResume + 1, learning.hubCalls)
    }

    @Test
    fun `row states follow the best result`() {
        val rows = viewModel().uiState.value.quizzes.associateBy { it.key }

        val inProgress = rows.getValue("shaxsiy-huquqlar")
        assertEquals(QuizRowState.IN_PROGRESS, inProgress.state)
        assertEquals(75, inProgress.percent)
        assertEquals("12 savol · 9/12 to‘g‘ri · Davom etish", inProgress.meta)

        assertEquals("15 savol · 6/15 to‘g‘ri", rows.getValue("ijtimoiy-huquqlar").meta)

        val fresh = rows.getValue("siyosiy-huquqlar")
        assertEquals(QuizRowState.NEW, fresh.state)
        assertEquals("10 savol · ≈ 6 daqiqa", fresh.meta)

        val done = rows.getValue("fuqarolarning-burchlari")
        assertEquals(QuizRowState.DONE, done.state)
        assertEquals("8/8 to‘g‘ri · Tugallangan", done.meta)
    }

    @Test
    fun `locked achievements show their progress`() {
        val locked = viewModel().uiState.value.achievements.single { !it.earned }

        assertEquals("7 / 10", locked.progressLabel)
    }

    @Test
    fun `guests get no pills and no progress card`() {
        auth.setSignedIn(false)
        learning.hubResult = AsosResult.Success(LearnSampleData.guestHub)

        val state = viewModel().uiState.value

        assertNull(state.streakDays)
        assertNull(state.pointsLabel)
        assertNull(state.progress)
        assertTrue(state.quizzes.all { it.state == QuizRowState.NEW })
    }

    @Test
    fun `shows the error when nothing loaded and recovers on retry`() {
        learning.hubResult = AsosResult.Error(message = "Internet aloqasi yo‘q.")
        val vm = viewModel()
        assertEquals("Internet aloqasi yo‘q.", vm.uiState.value.errorMessage)

        learning.hubResult = AsosResult.Success(LearnSampleData.hub)
        vm.onAction(LearnAction.OnRetry)

        assertNull(vm.uiState.value.errorMessage)
        assertTrue(vm.uiState.value.quizzes.isNotEmpty())
    }

    @Test
    fun `a failed refresh keeps the loaded content`() {
        val vm = viewModel()
        learning.hubResult = AsosResult.Error(message = "Xato")

        vm.onAction(LearnAction.OnResume)

        assertNull(vm.uiState.value.errorMessage)
        assertNotNull(vm.uiState.value.progress)
    }

    @Test
    fun `signing in reloads the hub`() {
        auth.setSignedIn(false)
        val vm = viewModel()
        assertEquals(1, learning.hubCalls)

        auth.setSignedIn(true)

        assertTrue(vm.uiState.value.isSignedIn)
        assertEquals(2, learning.hubCalls)
    }
}
