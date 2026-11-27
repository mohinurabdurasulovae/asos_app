/*
 * CaseDetailUiState.kt
 * Module: :feature:cases   ·   Work package: WP-08
 * Screen: Kazus — yechim (design artboard: Kazus.dc.html)
 *
 * Everything CaseDetailScreen draws, already formatted. No Android UI types.
 */
package uz.asos.feature.cases.detail

import uz.asos.feature.cases.data.model.ArticleOption
import uz.asos.feature.cases.data.model.CaseDifficulty
import uz.asos.feature.cases.data.model.CaseTopic
import uz.asos.feature.cases.data.model.LawyerComment

data class CaseDetailUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    /** "Kazus № 14" */
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
    /** The correct articles, shown as "Moddani o'qish" chips under the explanation. */
    val correctArticles: List<ArticleOption> = emptyList(),
    /** Non-null while the article sheet is open. */
    val previewArticle: ArticleOption? = null,
    /** The next unsolved case, for "Keyingi kazus"; null hides the button. */
    val nextCaseId: Int? = null,
) {
    val hasNextCase: Boolean get() = nextCaseId != null

    /** 1 Vaziyat · 2 Modda · 3 Yechim. */
    val currentStep: Int
        get() = when {
            isSubmitted -> STEP_SOLUTION
            options.any { it.isSelected } -> STEP_ARTICLE
            else -> STEP_SCENARIO
        }

    /** The prototype's rule: at least one article and a few words of reasoning. */
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

/** Whether it is right is only revealed once the screen is submitted. */
data class ArticleChoiceUi(
    val option: ArticleOption,
    val isSelected: Boolean,
    val isCorrectOption: Boolean,
)
