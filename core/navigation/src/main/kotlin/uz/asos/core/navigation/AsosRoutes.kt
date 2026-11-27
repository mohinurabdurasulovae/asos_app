package uz.asos.core.navigation

import kotlinx.serialization.Serializable

interface AsosRoute

@Serializable data object LoginRoute : AsosRoute

@Serializable data object HomeRoute : AsosRoute

@Serializable data object ConstitutionRoute : AsosRoute

@Serializable
data class ArticleRoute(val key: String) : AsosRoute {
    constructor(number: Int) : this(if (number == 0) PREAMBLE else number.toString())

    companion object {
        const val PREAMBLE = "muqaddima"
    }
}

@Serializable data class DictionaryRoute(val termSlug: String = "") : AsosRoute

@Serializable data object LearnRoute : AsosRoute

@Serializable data class QuizRoute(val quizId: String) : AsosRoute

@Serializable data object CasesRoute : AsosRoute

@Serializable data class CaseRoute(val caseId: String) : AsosRoute

@Serializable data class CommunityRoute(val articleNumber: Int = 0) : AsosRoute

@Serializable data class PostRoute(val postId: Int) : AsosRoute

@Serializable data object LegalHelpRoute : AsosRoute

@Serializable data class ChatRoute(val chatId: Int) : AsosRoute

@Serializable data object ProfileRoute : AsosRoute

@Serializable data object SearchRoute : AsosRoute

@Serializable data object BookmarksRoute : AsosRoute
