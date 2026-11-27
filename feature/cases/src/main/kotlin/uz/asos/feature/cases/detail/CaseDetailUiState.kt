package uz.asos.feature.cases.detail

import uz.asos.feature.cases.data.model.ArticleOption
import uz.asos.feature.cases.data.model.CaseDifficulty
import uz.asos.feature.cases.data.model.CaseTopic
import uz.asos.feature.cases.data.model.LawyerComment

data class CaseDetailUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val titleLabel: String = "",
    val title: String = "",
    val topic: CaseTopic? = null,
    val difficulty: CaseDifficulty? = null,
    val persona: PersonaUi? = null,
    val storyText: String = "",
    val questionText: String = "",
    val options: List<ArticleChoiceUi> = emptyList(),
    val userSolution: String = "",
    val isSubmitted: Boolean = false,
    val isCorrect: Boolean = false,
    val isBookmarked: Boolean = false,
    val rewardPoints: Int = 0,
    val lawyerComment: LawyerComment? = null,
    val correctArticles: List<ArticleOption> = emptyList(),
    val previewArticle: ArticleOption? = null,
    val nextCaseId: Int? = null,
) {
    val hasNextCase: Boolean get() = nextCaseId != null

    val currentStep: Int
        get() = when {
            isSubmitted -> STEP_SOLUTION
            options.any { it.isSelected } -> STEP_ARTICLE
            else -> STEP_SCENARIO
        }

    val canSubmit: Boolean
        get() = !isSubmitted &&
            options.any { it.isSelected } &&
            userSolution.trim().length >= MIN_SOLUTION_LENGTH

    companion object {
        const val STEP_SCENARIO = 1
        const val STEP_ARTICLE = 2
        const val STEP_SOLUTION = 3
        const val MIN_SOLUTION_LENGTH = 5
    }
}

data class PersonaUi(
    val initials: String,
    val name: String,
    val subtitle: String,
)

data class ArticleChoiceUi(
    val option: ArticleOption,
    val isSelected: Boolean,
    val isCorrectOption: Boolean,
)
