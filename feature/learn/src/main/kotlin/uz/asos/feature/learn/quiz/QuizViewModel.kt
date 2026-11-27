package uz.asos.feature.learn.quiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.asos.core.common.result.AsosErrorCode
import uz.asos.core.common.result.AsosResult
import uz.asos.core.common.text.UzbekFormatters
import uz.asos.core.data.repository.AuthRepository
import uz.asos.core.data.repository.LearningRepository
import uz.asos.core.model.learning.AnswerResult
import uz.asos.core.model.learning.AttemptResult
import uz.asos.core.model.learning.QuestionMark
import uz.asos.core.model.learning.QuizAttempt
import uz.asos.core.model.learning.QuizQuestion
import uz.asos.feature.learn.ui.label
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val learningRepository: LearningRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val quizKey: String = checkNotNull(savedStateHandle.get<String>(KEY_ARG)) {
        "QuizViewModel needs the \"$KEY_ARG\" navigation argument"
    }

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private val _events = Channel<QuizEvent>(Channel.BUFFERED)
    val events: Flow<QuizEvent> = _events.receiveAsFlow()

    private var attempt: QuizAttempt? = null
    private var currentQuestionId: Int? = null
    private var pendingResult: AttemptResult? = null
    private var started = false
    private var startJob: Job? = null

    init {
        viewModelScope.launch {
            authRepository.isSignedIn.collect { signedIn ->
                val phase = _uiState.value.phase
                when {
                    signedIn && (!started || phase == QuizPhase.NEEDS_SIGN_IN) -> start()
                    !signedIn && (phase == QuizPhase.LOADING || phase == QuizPhase.NEEDS_SIGN_IN) -> {
                        startJob?.cancel()
                        _uiState.update { it.copy(phase = QuizPhase.NEEDS_SIGN_IN) }
                    }
                }
            }
        }
    }

    fun onAction(action: QuizAction) {
        when (action) {
            is QuizAction.OnOptionClick -> answer(action.optionId)
            QuizAction.OnNextClick -> next()
            QuizAction.OnFinishClick -> pendingResult?.let(::showResult) ?: finish()
            QuizAction.OnRestartClick, QuizAction.OnRetry -> start()
            is QuizAction.OnArticleClick, QuizAction.OnCloseClick, QuizAction.OnSignInClick -> Unit
        }
    }

    private fun start() {
        started = true
        startJob?.cancel()
        startJob = viewModelScope.launch {
            attempt = null
            currentQuestionId = null
            pendingResult = null
            _uiState.value = QuizUiState(phase = QuizPhase.LOADING)

            when (val result = learningRepository.startAttempt(quizKey)) {
                is AsosResult.Success -> {
                    val opened = result.data
                    attempt = opened
                    val first = firstOpenQuestion(opened)
                    when {
                        opened.questions.isEmpty() -> _uiState.update {
                            it.copy(phase = QuizPhase.ERROR, errorMessage = MSG_NO_QUESTIONS)
                        }
                        first != null -> showQuestion(first.id)
                        else -> finish()
                    }
                }
                is AsosResult.Error -> _uiState.update {
                    if (result.code == AsosErrorCode.UNAUTHORIZED) {
                        it.copy(phase = QuizPhase.NEEDS_SIGN_IN)
                    } else {
                        it.copy(phase = QuizPhase.ERROR, errorMessage = result.message)
                    }
                }
            }
        }
    }

    private fun firstOpenQuestion(attempt: QuizAttempt): QuizQuestion? {
        val open = attempt.questions.filterNot { it.answered }
        return open.firstOrNull { it.id == attempt.nextQuestionId } ?: open.firstOrNull()
    }

    private fun showQuestion(questionId: Int) {
        val current = attempt ?: return
        val index = current.questions.indexOfFirst { it.id == questionId }
        if (index < 0) return
        val question = current.questions[index]
        currentQuestionId = questionId
        _uiState.update {
            it.copy(
                phase = QuizPhase.QUESTION,
                errorMessage = null,
                quizTitle = UzbekFormatters.display(current.quizTitle),
                difficultyLabel = current.difficulty.label(),
                marks = marks(current),
                currentNumber = index + 1,
                total = current.questions.size,
                question = QuestionUi(
                    id = question.id,
                    body = UzbekFormatters.display(question.body),
                    options = question.options.map { option ->
                        OptionUi(id = option.id, label = option.label, text = UzbekFormatters.display(option.body))
                    },
                ),
                selectedOptionId = null,
                isSubmitting = false,
                feedback = null,
                questionStartedAt = now(),
                answeredAfterSeconds = null,
                isFinishing = false,
                result = null,
            )
        }
    }

    private fun marks(attempt: QuizAttempt): List<QuestionMark> = attempt.questions.map { question ->
        when {
            question.answered && question.isCorrect == true -> QuestionMark.CORRECT
            question.answered -> QuestionMark.WRONG
            question.id == currentQuestionId -> QuestionMark.CURRENT
            else -> QuestionMark.PENDING
        }
    }

    private fun answer(optionId: Int) {
        val state = _uiState.value
        val current = attempt ?: return
        val questionId = currentQuestionId ?: return
        if (state.phase != QuizPhase.QUESTION || state.isSubmitting || state.feedback != null) return

        val seconds = ((now() - state.questionStartedAt) / 1000L).toInt().coerceIn(0, MAX_SECONDS)
        _uiState.update { it.copy(selectedOptionId = optionId, isSubmitting = true) }

        viewModelScope.launch {
            when (val result = learningRepository.answer(current.id, questionId, optionId, seconds)) {
                is AsosResult.Success -> onAnswered(questionId, optionId, seconds, result.data)
                is AsosResult.Error -> {
                    _uiState.update { it.copy(selectedOptionId = null, isSubmitting = false) }
                    _events.send(QuizEvent.Message(result.message))
                }
            }
        }
    }

    private fun onAnswered(questionId: Int, optionId: Int, seconds: Int, result: AnswerResult) {
        val current = attempt ?: return
        val question = current.questions.firstOrNull { it.id == questionId } ?: return
        val correctId = result.correctOptionId ?: optionId.takeIf { result.isCorrect }

        val answered = question.copy(
            answered = true,
            myOptionId = optionId,
            isCorrect = result.isCorrect,
            explanation = result.explanation ?: question.explanation,
            article = result.article ?: question.article,
            options = question.options.map { it.copy(isCorrect = correctId?.let { id -> it.id == id }) },
        )
        val updated = current.copy(
            answered = result.answered,
            questions = current.questions.map { if (it.id == questionId) answered else it },
        )
        attempt = updated
        pendingResult = result.result
        val isLast = result.result != null || updated.questions.none { !it.answered }

        _uiState.update {
            it.copy(
                isSubmitting = false,
                selectedOptionId = optionId,
                marks = marks(updated),
                answeredAfterSeconds = seconds,
                feedback = FeedbackUi(
                    isCorrect = result.isCorrect,
                    correctOptionId = correctId,
                    pointsEarned = result.pointsEarned,
                    correctAnswerText = if (result.isCorrect) {
                        null
                    } else {
                        question.options.firstOrNull { option -> option.id == correctId }
                            ?.body?.let(UzbekFormatters::display)
                    },
                    explanation = answered.explanation?.let(UzbekFormatters::display),
                    articleNumber = answered.article?.number,
                    isLastQuestion = isLast,
                ),
            )
        }
    }

    private fun next() {
        if (_uiState.value.feedback == null) return
        pendingResult?.let {
            showResult(it)
            return
        }
        val current = attempt ?: return
        val index = current.questions.indexOfFirst { it.id == currentQuestionId }
        val after = current.questions.drop(index + 1).firstOrNull { !it.answered }
        val next = after ?: current.questions.firstOrNull { !it.answered }
        if (next != null) showQuestion(next.id) else finish()
    }

    private fun finish() {
        val current = attempt ?: return
        if (_uiState.value.isFinishing) return
        _uiState.update { it.copy(isFinishing = true) }
        viewModelScope.launch {
            when (val result = learningRepository.finish(current.id)) {
                is AsosResult.Success -> showResult(result.data)
                is AsosResult.Error -> {
                    _uiState.update {
                        if (it.phase == QuizPhase.QUESTION) {
                            it.copy(isFinishing = false)
                        } else {
                            it.copy(isFinishing = false, phase = QuizPhase.ERROR, errorMessage = result.message)
                        }
                    }
                    if (_uiState.value.phase == QuizPhase.QUESTION) _events.send(QuizEvent.Message(result.message))
                }
            }
        }
    }

    private fun showResult(result: AttemptResult) {
        attempt = null
        currentQuestionId = null
        pendingResult = null
        _uiState.update {
            it.copy(
                phase = QuizPhase.RESULT,
                isFinishing = false,
                isSubmitting = false,
                result = QuizResultUi(
                    correct = result.correctCount,
                    total = result.total,
                    percent = result.percent.coerceIn(0, 100),
                    points = result.points,
                    isPerfect = result.isPerfect,
                    newAchievements = result.achievements.map { a -> UzbekFormatters.display(a.title) },
                ),
            )
        }
    }

    private fun now(): Long = System.currentTimeMillis()

    companion object {
        internal const val KEY_ARG = "quizId"
        private const val MAX_SECONDS = 3600
        private const val MSG_NO_QUESTIONS = "Bu testda hozircha savol yo‘q."
    }
}
