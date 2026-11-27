package uz.asos.feature.cases.data.model

enum class CaseTopic(val label: String) {
    SHAXSIY_HAYOT("Shaxsiy hayot"),
    TALIM("Ta’lim"),
    MEHNAT("Mehnat"),
    SUD("Sud"),
    UY_JOY("Uy-joy"),
}

enum class CaseDifficulty(val label: String, val level: Int) {
    OSON("Oson", 1),
    ORTA("O‘rta", 2),
    MURAKKAB("Murakkab", 3),
}

enum class CaseStatus(val label: String) {
    YANGI("Yangi"),
    JARAYONDA("Jarayonda"),
    YECHILGAN("Yechilgan"),
}

data class ArticleOption(
    val number: Int,
    val title: String,
    val officialText: String,
    val simpleExplanation: String,
)

data class LawyerComment(
    val initials: String,
    val fullName: String,
    val role: String,
    val paragraphs: List<String>,
    val practicalSteps: List<String>,
)

data class CaseItem(
    val id: Int,
    val number: Int,
    val title: String,
    val shortDescription: String,
    val topic: CaseTopic,
    val difficulty: CaseDifficulty,
    val estimatedMinutes: Int,
    val isFeaturedOfWeek: Boolean = false,
    val initialStatus: CaseStatus = CaseStatus.YANGI,
    val initialProgressFraction: Float = 0f,
    val personaInitials: String,
    val personaName: String,
    val personaSubtitle: String,
    val storyText: String,
    val questionText: String,
    val options: List<ArticleOption>,
    val correctArticleNumbers: Set<Int>,
    val rewardPoints: Int = 40,
    val lawyerComment: LawyerComment,
    val sampleSolution: String = "",
)

data class CaseProgress(
    val selectedArticles: Set<Int> = emptySet(),
    val userSolution: String = "",
    val isSubmitted: Boolean = false,
    val isCorrect: Boolean = false,
    val isBookmarked: Boolean = false,
    val status: CaseStatus = CaseStatus.YANGI,
)
