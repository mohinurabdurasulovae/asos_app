/*
 * LearnSampleData.kt
 * Module: :core:testing   ·   Work package: WP-07
 *
 * Sample O‘rganish / Test savoli data matching the design canvas (Organish.dc.html, Test.dc.html).
 */
package uz.asos.core.testing.data

import uz.asos.core.model.constitution.ArticleRef
import uz.asos.core.model.constitution.ReadingOverview
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

object LearnSampleData {

    private fun quiz(
        id: Int,
        slug: String,
        title: String,
        tone: String,
        questions: Int,
        minutes: Int,
        best: QuizBest? = null,
        difficulty: QuizDifficulty = QuizDifficulty.MEDIUM,
    ) = Quiz(
        id = id,
        slug = slug,
        title = title,
        difficulty = difficulty,
        icon = null,
        tone = tone,
        estMinutes = minutes,
        questionCount = questions,
        topicSlug = slug,
        topicTitle = title,
        best = best,
    )

    val quizzes = listOf(
        quiz(1, "shaxsiy-huquqlar", "Shaxsiy huquqlar", "brand", 12, 7, QuizBest(9, 12, 75, 1, false, openAttemptId = 41)),
        quiz(2, "ijtimoiy-huquqlar", "Ijtimoiy huquqlar", "teal", 15, 9, QuizBest(6, 15, 40, 1, false, null)),
        quiz(3, "siyosiy-huquqlar", "Siyosiy huquqlar", "purple", 10, 6),
        quiz(4, "fuqarolarning-burchlari", "Fuqarolarning burchlari", "orange", 8, 5, QuizBest(8, 8, 100, 2, true, null)),
        quiz(5, "davlat-hokimiyati", "Davlat hokimiyati", "navy", 14, 9),
        quiz(6, "konstitutsiya-asoslari", "Konstitutsiya asoslari", "brand", 8, 4, difficulty = QuizDifficulty.EASY),
    )

    val achievements = listOf(
        Achievement(1, "birinchi-qadam", "Birinchi qadam", null, "book", "brand", "articles_read", 1, 1, true),
        Achievement(2, "seriya-7", "7 kunlik seriya", null, "flame", "orange", "streak_days", 7, 7, true),
        Achievement(3, "burchlar-bilimdoni", "Burchlar bilimdoni", null, "trophy", "teal", "quizzes_done", 1, 1, true),
        Achievement(4, "kazus-ustasi", "Kazus ustasi", null, "scales", "purple", "cases_solved", 10, 7, false),
    )

    val hub = LearnHub(
        quizzes = quizzes,
        casesSolved = 7,
        casesNew = 5,
        overview = ReadingOverview(articlesTotal = 155, articlesRead = 59, percent = 38, continueReading = null),
        dailyGoal = DailyGoal(goal = 3, done = 2, completed = false),
        points = 1240,
        streakDays = 7,
        achievements = achievements,
    )

    /** /v1/learn without a token. */
    val guestHub = hub.copy(
        quizzes = quizzes.map { it.copy(best = null) },
        overview = null,
        dailyGoal = null,
        points = null,
        streakDays = null,
        achievements = achievements.map { it.copy(earned = false, progress = 0) },
    )

    private fun question(id: Int, body: String, answers: List<String>, correctIndex: Int, article: Int) = QuizQuestion(
        id = id,
        index = id,
        body = body,
        points = 20,
        seconds = 45,
        options = answers.mapIndexed { i, text -> QuizOption(id = id * 10 + i + 1, label = ('A' + i).toString(), body = text, isCorrect = null) },
        answered = false,
        myOptionId = null,
        isCorrect = null,
        explanation = null,
        article = ArticleRef(number = article, title = "$article-modda", slug = "$article-modda"),
    ).also { correctAnswers[id] = id * 10 + correctIndex + 1 }

    /** question id → right option id (the server's secret). */
    val correctAnswers = mutableMapOf<Int, Int>()

    /** Three questions; question 1 already answered right (resumed attempt). */
    val attempt: QuizAttempt by lazy {
        val q1 = question(1, "O‘zbekiston Respublikasi Konstitutsiyasi qachon qabul qilingan (yangi tahrir)?", listOf("2023-yil 30-aprel", "1992-yil 8-dekabr", "2021-yil 1-sentabr", "2017-yil 7-fevral"), 0, 1)
        val q2 = question(2, "Konstitutsiyaga ko‘ra, shaxs sud qarorisiz necha soatdan ortiq ushlab turilishi mumkin emas?", listOf("24 soat", "48 soat", "72 soat", "12 soat"), 1, 27)
        val q3 = question(3, "Har kim qaysi huquqqa ega?", listOf("Ta’lim olish", "Soliq to‘lamaslik", "Qonunni bilmaslik", "Hech biri"), 0, 50)
        QuizAttempt(
            id = 41,
            quizId = 1,
            quizSlug = "shaxsiy-huquqlar",
            quizTitle = "Shaxsiy huquqlar",
            difficulty = QuizDifficulty.MEDIUM,
            total = 3,
            answered = 1,
            correctCount = 1,
            points = 20,
            isFinished = false,
            nextQuestionId = 2,
            questions = listOf(
                q1.copy(answered = true, myOptionId = 11, isCorrect = true),
                q2,
                q3,
            ),
        )
    }

    val result = AttemptResult(
        attemptId = 41,
        total = 3,
        correctCount = 2,
        percent = 67,
        points = 40,
        isPerfect = false,
        achievements = emptyList(),
    )

    /** What the server answers for [optionId] on [questionId]. Question 3 is the last one. */
    fun answerFor(questionId: Int, optionId: Int): AnswerResult {
        attempt // make sure correctAnswers is filled
        val correct = correctAnswers.getValue(questionId)
        val isCorrect = optionId == correct
        return AnswerResult(
            isCorrect = isCorrect,
            correctOptionId = correct,
            pointsEarned = if (isCorrect) 20 else 0,
            totalPoints = 1260,
            explanation = "Izoh: $questionId-savol.",
            article = ArticleRef(number = 27, title = "27-modda", slug = "27-modda"),
            answered = questionId,
            total = 3,
            result = if (questionId == 3) result else null,
        )
    }
}
