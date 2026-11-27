package uz.asos.feature.learn.hub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.asos.core.common.result.AsosResult
import uz.asos.core.common.text.UzbekFormatters
import uz.asos.core.data.repository.AuthRepository
import uz.asos.core.data.repository.LearningRepository
import uz.asos.core.model.learning.Achievement
import uz.asos.core.model.learning.LearnHub
import uz.asos.core.model.learning.Quiz
import uz.asos.core.model.learning.QuizDifficulty
import javax.inject.Inject

@HiltViewModel
class LearnViewModel @Inject constructor(
    private val learningRepository: LearningRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LearnUiState())
    val uiState: StateFlow<LearnUiState> = _uiState.asStateFlow()

    private var hasLoaded = false
    private var userPickedDifficulty = false
    private var loadJob: Job? = null

    init {
        load(showSkeleton = true)
        observeSession()
    }

    fun onAction(action: LearnAction) {
        when (action) {
            is LearnAction.OnDifficultySelect -> {
                userPickedDifficulty = true
                _uiState.update { it.copy(selectedDifficulty = action.difficulty) }
            }
            LearnAction.OnRetry -> load(showSkeleton = true)
            LearnAction.OnResume -> if (hasLoaded) load(showSkeleton = false)
            is LearnAction.OnQuizClick,
            LearnAction.OnCasesClick,
            LearnAction.OnAchievementsClick,
            LearnAction.OnSignInClick,
            -> Unit
        }
    }

    private fun observeSession() {
        viewModelScope.launch {
            authRepository.isSignedIn.collect { signedIn ->
                val changed = _uiState.value.isSignedIn != signedIn
                _uiState.update { it.copy(isSignedIn = signedIn) }
                if (changed && hasLoaded) load(showSkeleton = false)
            }
        }
    }

    private fun load(showSkeleton: Boolean) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            if (showSkeleton) _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = learningRepository.getLearnHub()) {
                is AsosResult.Success -> {
                    hasLoaded = true
                    apply(result.data)
                }
                is AsosResult.Error -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = if (it.quizzes.isEmpty()) result.message else null,
                    )
                }
            }
        }
    }

    private fun apply(hub: LearnHub) {
        val quizzes = hub.quizzes.map { it.toRow() }
        _uiState.update { state ->
            state.copy(
                isLoading = false,
                errorMessage = null,
                streakDays = hub.streakDays,
                pointsLabel = hub.points?.let { UzbekFormatters.groupThousands(it) },
                progress = hub.overview?.let { overview ->
                    OverallProgressUi(
                        percent = overview.percent.coerceIn(0, 100),
                        articlesRead = overview.articlesRead,
                        articlesTotal = overview.articlesTotal,
                        goal = hub.dailyGoal?.goal ?: 0,
                        done = hub.dailyGoal?.done ?: 0,
                    )
                },
                cases = CasesPromoUi(solved = hub.casesSolved, new = hub.casesNew),
                quizzes = quizzes,
                selectedDifficulty = if (userPickedDifficulty) {
                    state.selectedDifficulty
                } else {
                    defaultDifficulty(quizzes)
                },
                achievements = hub.achievements.map { it.toUi() },
            )
        }
    }

    private fun defaultDifficulty(quizzes: List<QuizRowUi>): QuizDifficulty {
        val levels = quizzes.map { it.difficulty }.toSet()
        return when {
            QuizDifficulty.MEDIUM in levels || levels.isEmpty() -> QuizDifficulty.MEDIUM
            else -> QuizDifficulty.entries.first { it in levels }
        }
    }
}

internal fun Quiz.toRow(): QuizRowUi {
    val best = best
    val questions = "$questionCount savol"
    val state = when {
        best != null && best.isPerfect -> QuizRowState.DONE
        best != null && (best.attempts > 0 || best.openAttemptId != null) -> QuizRowState.IN_PROGRESS
        else -> QuizRowState.NEW
    }
    val meta = when (state) {
        QuizRowState.DONE -> "${best!!.correct}/${best.total} to‘g‘ri · Tugallangan"
        QuizRowState.IN_PROGRESS -> listOfNotNull(
            questions,
            best!!.takeIf { it.total > 0 }?.let { "${it.correct}/${it.total} to‘g‘ri" },
            "Davom etish".takeIf { best.openAttemptId != null },
        ).joinToString(" · ")
        QuizRowState.NEW -> if (estMinutes > 0) "$questions · ≈ ${UzbekFormatters.minutes(estMinutes)}" else questions
    }
    return QuizRowUi(
        key = key,
        title = UzbekFormatters.display(title),
        difficulty = difficulty,
        iconHint = listOfNotNull(icon, topicSlug, slug, title).joinToString(" "),
        tone = tone,
        state = state,
        meta = meta,
        percent = (best?.percent ?: 0).coerceIn(0, 100),
    )
}

internal fun Achievement.toUi() = AchievementUi(
    slug = slug,
    title = UzbekFormatters.display(title),
    iconHint = listOfNotNull(icon, metric, slug).joinToString(" "),
    tone = tone,
    earned = earned,
    progressLabel = if (!earned && threshold > 0) "${progress.coerceAtMost(threshold)} / $threshold" else null,
)
