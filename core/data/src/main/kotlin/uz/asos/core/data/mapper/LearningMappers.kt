package uz.asos.core.data.mapper

import uz.asos.core.model.constitution.ArticleRef
import uz.asos.core.model.learning.Achievement
import uz.asos.core.model.learning.AnswerResult
import uz.asos.core.model.learning.AttemptResult
import uz.asos.core.model.learning.DailyGoal
import uz.asos.core.model.learning.LearnHub
import uz.asos.core.model.learning.Quiz
import uz.asos.core.model.learning.QuizAttempt
import uz.asos.core.model.learning.QuizBest
import uz.asos.core.model.learning.QuizDifficulty
import uz.asos.core.model.learning.QuizOption
import uz.asos.core.model.learning.QuizQuestion
import uz.asos.core.network.dto.AchievementDto
import uz.asos.core.network.dto.AnswerResponseDto
import uz.asos.core.network.dto.ArticleRefDto
import uz.asos.core.network.dto.AttemptDto
import uz.asos.core.network.dto.AttemptQuestionDto
import uz.asos.core.network.dto.AttemptResultDto
import uz.asos.core.network.dto.LearnResponseDto
import uz.asos.core.network.dto.QuizDto

internal fun LearnResponseDto.asModel() = LearnHub(
    quizzes = quizzes.map { it.asModel() },
    casesSolved = casesSolved,
    casesNew = casesNew,
    overview = overview?.asModel(),
    dailyGoal = dailyGoal?.let { DailyGoal(goal = it.goal, done = it.done, completed = it.completed) },
    points = points,
    streakDays = streakDays,
    achievements = achievements.map { it.asModel() },
)

internal fun QuizDto.asModel() = Quiz(
    id = id,
    slug = slug,
    title = title,
    difficulty = QuizDifficulty.fromApi(difficulty),
    icon = icon,
    tone = tone,
    estMinutes = estMinutes,
    questionCount = questionCount,
    topicSlug = topic?.slug?.takeIf { it.isNotBlank() },
    topicTitle = topic?.title?.takeIf { it.isNotBlank() },
    best = myBest?.let {
        QuizBest(
            correct = it.correct,
            total = it.total,
            percent = it.percent,
            attempts = it.attempts,
            isPerfect = it.isPerfect,
            openAttemptId = it.openAttemptId,
        )
    },
)

internal fun AchievementDto.asModel() = Achievement(
    id = id,
    slug = slug,
    title = title,
    hint = hint,
    icon = icon,
    tone = tone,
    metric = metric,
    threshold = threshold,
    progress = progress,
    earned = earned,
)

internal fun AttemptDto.asModel() = QuizAttempt(
    id = id,
    quizId = quiz.id,
    quizSlug = quiz.slug,
    quizTitle = quiz.title,
    difficulty = QuizDifficulty.fromApi(quiz.difficulty),
    total = total,
    answered = answered,
    correctCount = correctCount,
    points = points,
    isFinished = isFinished,
    nextQuestionId = nextQuestionId,
    questions = questions.sortedBy { it.index }.map { it.asModel() },
)

internal fun AttemptQuestionDto.asModel() = QuizQuestion(
    id = id,
    index = index,
    body = body,
    points = points,
    seconds = seconds,
    options = options.map { QuizOption(id = it.id, label = it.label, body = it.body, isCorrect = it.isCorrect) },
    answered = answered,
    myOptionId = myOptionId,
    isCorrect = isCorrect,
    explanation = explanation?.takeIf { it.isNotBlank() },
    article = article?.asModel(),
)

internal fun AnswerResponseDto.asModel() = AnswerResult(
    isCorrect = isCorrect,
    correctOptionId = correctOptionId,
    pointsEarned = pointsEarned,
    totalPoints = points,
    explanation = explanation?.takeIf { it.isNotBlank() },
    article = article?.asModel(),
    answered = answered,
    total = total,
    result = result?.asModel(),
)

internal fun AttemptResultDto.asModel() = AttemptResult(
    attemptId = attemptId,
    total = total,
    correctCount = correctCount,
    percent = percent,
    points = points,
    isPerfect = isPerfect,
    achievements = achievements.map { it.asModel() },
)

internal fun ArticleRefDto.asModel(): ArticleRef? =
    if (number <= 0) null else ArticleRef(number = number, title = title, slug = slug)
