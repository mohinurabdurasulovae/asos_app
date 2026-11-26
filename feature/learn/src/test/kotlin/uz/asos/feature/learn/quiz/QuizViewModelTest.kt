/*
 * QuizViewModelTest.kt
 * Module: :feature:learn   ·   Work package: WP-07
 */
package uz.asos.feature.learn.quiz

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import uz.asos.core.common.result.AsosErrorCode
import uz.asos.core.common.result.AsosResult
import uz.asos.core.model.learning.QuestionMark
import uz.asos.core.testing.MainDispatcherRule
import uz.asos.core.testing.data.LearnSampleData
import uz.asos.core.testing.repository.FakeAuthRepository
import uz.asos.core.testing.repository.FakeLearningRepository

class QuizViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val learning = FakeLearningRepository()
    private val auth = FakeAuthRepository(signedIn = true)

    private fun viewModel(quizId: String = "shaxsiy-huquqlar") = QuizViewModel(
        savedStateHandle = SavedStateHandle(mapOf(QuizViewModel.KEY_ARG to quizId)),
        learningRepository = learning,
        authRepository = auth,
    )

    private val q2Correct get() = LearnSampleData.correctAnswers.getValue(2)
    private val q2Wrong get() = 21

    @Test
    fun `resumes the open attempt at the next question`() {
        val state = viewModel().uiState.value

        assertEquals(listOf("shaxsiy-huquqlar"), learning.startedQuizzes)
        assertEquals(QuizPhase.QUESTION, state.phase)
        assertEquals(2, state.question?.id)
        assertEquals(2, state.currentNumber)
        assertEquals(3, state.total)
        assertEquals("Shaxsiy huquqlar", state.quizTitle)
        assertEquals("O‘rta", state.difficultyLabel)
        assertEquals(listOf(QuestionMark.CORRECT, QuestionMark.CURRENT, QuestionMark.PENDING), state.marks)
        assertNull(state.feedback)
    }

    @Test
    fun `guests are asked to sign in and the quiz opens after signing in`() {
        auth.setSignedIn(false)
        val vm = viewModel()
        assertEquals(QuizPhase.NEEDS_SIGN_IN, vm.uiState.value.phase)
        assertTrue(learning.startedQuizzes.isEmpty())

        auth.setSignedIn(true)

        assertEquals(QuizPhase.QUESTION, vm.uiState.value.phase)
    }

    @Test
    fun `an expired token shows the sign-in prompt`() {
        learning.attemptResult = AsosResult.Error(message = "Avval tizimga kiring.", code = AsosErrorCode.UNAUTHORIZED)

        assertEquals(QuizPhase.NEEDS_SIGN_IN, viewModel().uiState.value.phase)
    }

    @Test
    fun `other errors show the message and retry opens the attempt again`() {
        learning.attemptResult = AsosResult.Error(message = "Topilmadi")
        val vm = viewModel()
        assertEquals(QuizPhase.ERROR, vm.uiState.value.phase)
        assertEquals("Topilmadi", vm.uiState.value.errorMessage)

        learning.attemptResult = AsosResult.Success(LearnSampleData.attempt)
        vm.onAction(QuizAction.OnRetry)

        assertEquals(QuizPhase.QUESTION, vm.uiState.value.phase)
    }

    @Test
    fun `a right answer shows the green panel with points`() {
        val vm = viewModel()

        vm.onAction(QuizAction.OnOptionClick(q2Correct))

        val state = vm.uiState.value
        val call = learning.answerCalls.single()
        assertEquals(41, call.attemptId)
        assertEquals(2, call.questionId)
        assertEquals(q2Correct, call.optionId)
        assertTrue(call.seconds in 0..3600)
        assertNotNull(state.feedback)
        val feedback = state.feedback!!
        assertTrue(feedback.isCorrect)
        assertEquals(20, feedback.pointsEarned)
        assertNull(feedback.correctAnswerText)
        assertEquals(27, feedback.articleNumber)
        assertFalse(feedback.isLastQuestion)
        assertEquals(QuestionMark.CORRECT, state.marks[1])
    }

    @Test
    fun `a wrong answer shows the right answer`() {
        val vm = viewModel()

        vm.onAction(QuizAction.OnOptionClick(q2Wrong))

        val feedback = vm.uiState.value.feedback!!
        assertFalse(feedback.isCorrect)
        assertEquals(q2Correct, feedback.correctOptionId)
        assertEquals("48 soat", feedback.correctAnswerText)
        assertEquals(QuestionMark.WRONG, vm.uiState.value.marks[1])
    }

    @Test
    fun `a question can be answered only once`() {
        val vm = viewModel()

        vm.onAction(QuizAction.OnOptionClick(q2Wrong))
        vm.onAction(QuizAction.OnOptionClick(q2Correct))

        assertEquals(1, learning.answerCalls.size)
    }

    @Test
    fun `next goes to the following question`() {
        val vm = viewModel()
        vm.onAction(QuizAction.OnOptionClick(q2Correct))

        vm.onAction(QuizAction.OnNextClick)

        val state = vm.uiState.value
        assertEquals(3, state.question?.id)
        assertNull(state.feedback)
        assertNull(state.selectedOptionId)
        assertEquals(listOf(QuestionMark.CORRECT, QuestionMark.CORRECT, QuestionMark.CURRENT), state.marks)
    }

    @Test
    fun `the last answer leads to the result without calling finish`() {
        val vm = viewModel()
        vm.onAction(QuizAction.OnOptionClick(q2Correct))
        vm.onAction(QuizAction.OnNextClick)
        vm.onAction(QuizAction.OnOptionClick(LearnSampleData.correctAnswers.getValue(3)))
        assertTrue(vm.uiState.value.feedback!!.isLastQuestion)

        vm.onAction(QuizAction.OnNextClick)

        val state = vm.uiState.value
        assertEquals(QuizPhase.RESULT, state.phase)
        assertEquals(2, state.result?.correct)
        assertEquals(67, state.result?.percent)
        assertEquals(0, learning.finishCalls)
    }

    @Test
    fun `Tugatish finishes the attempt early`() {
        val vm = viewModel()
        vm.onAction(QuizAction.OnOptionClick(q2Correct))

        vm.onAction(QuizAction.OnFinishClick)

        assertEquals(1, learning.finishCalls)
        assertEquals(QuizPhase.RESULT, vm.uiState.value.phase)
    }

    @Test
    fun `Qaytadan on the result opens a new attempt`() {
        val vm = viewModel()
        vm.onAction(QuizAction.OnOptionClick(q2Correct))
        vm.onAction(QuizAction.OnFinishClick)

        vm.onAction(QuizAction.OnRestartClick)

        assertEquals(2, learning.startedQuizzes.size)
        assertEquals(QuizPhase.QUESTION, vm.uiState.value.phase)
    }

    @Test
    fun `a failed answer clears the selection and shows a message`() = runTest {
        learning.answerResults[2] = AsosResult.Error(message = "Allaqachon javob berilgan")
        val vm = viewModel()

        vm.events.test {
            vm.onAction(QuizAction.OnOptionClick(q2Correct))

            assertEquals(QuizEvent.Message("Allaqachon javob berilgan"), awaitItem())
        }
        assertNull(vm.uiState.value.selectedOptionId)
        assertFalse(vm.uiState.value.isSubmitting)
        assertNull(vm.uiState.value.feedback)
    }
}
