/*
 * AsosRoutes.kt
 * Module: :core:navigation   ·   Work package: WP-00
 *
 * THE INTEGRATION CONTRACT. Every destination in the app as a @Serializable route
 * (Navigation Compose type-safe routes). Features navigate only through these, never by
 * importing another feature:
 *
 *     onNavigate(ArticleRoute(50))
 *
 * Arguments use non-null types with defaults (Navigation 2.8 handles those everywhere).
 */
package uz.asos.core.navigation

import kotlinx.serialization.Serializable

/** Marker for every route, so features can expose `onNavigate: (AsosRoute) -> Unit`. */
interface AsosRoute

/** Kirish  ·  :feature:auth */
@Serializable data object LoginRoute : AsosRoute

/** Bosh sahifa  ·  :feature:home */
@Serializable data object HomeRoute : AsosRoute

/** Konstitutsiya  ·  :feature:constitution */
@Serializable data object ConstitutionRoute : AsosRoute

/**
 * Modda (article reader)  ·  :feature:constitution
 * @param key article number ("50"), slug, or "muqaddima" for the preamble.
 */
@Serializable
data class ArticleRoute(val key: String) : AsosRoute {
    constructor(number: Int) : this(number.toString())

    companion object {
        const val PREAMBLE = "muqaddima"
    }
}

/**
 * Konstitutsiyaviy lug‘at  ·  :feature:dictionary
 * @param termSlug open this term directly ("" = the list).
 */
@Serializable data class DictionaryRoute(val termSlug: String = "") : AsosRoute

/** O‘rganish  ·  :feature:learn */
@Serializable data object LearnRoute : AsosRoute

/** Test savoli  ·  :feature:learn. @param quizId quiz id or slug. */
@Serializable data class QuizRoute(val quizId: String) : AsosRoute

/** Kazuslar  ·  :feature:cases */
@Serializable data object CasesRoute : AsosRoute

/** Kazus — yechim  ·  :feature:cases. @param caseId case number or slug. */
@Serializable data class CaseRoute(val caseId: String) : AsosRoute

/**
 * Hamjamiyat  ·  :feature:community
 * @param articleNumber show only discussions about this article (0 = all).
 */
@Serializable data class CommunityRoute(val articleNumber: Int = 0) : AsosRoute

/** Muhokama  ·  :feature:community */
@Serializable data class PostRoute(val postId: Int) : AsosRoute

/** Huquqiy yordam  ·  :feature:legalhelp */
@Serializable data object LegalHelpRoute : AsosRoute

/** Advokat bilan chat  ·  :feature:legalhelp */
@Serializable data class ChatRoute(val chatId: Int) : AsosRoute

/** Profil  ·  :feature:profile */
@Serializable data object ProfileRoute : AsosRoute

/** Global search (screen TBD). */
@Serializable data object SearchRoute : AsosRoute

/** Saved items, Saqlanganlar (screen TBD). */
@Serializable data object BookmarksRoute : AsosRoute
