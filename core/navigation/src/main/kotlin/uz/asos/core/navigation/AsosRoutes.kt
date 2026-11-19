/*
 * AsosRoutes.kt
 * Module: :core:navigation   ·   Work package: WP-00
 *
 * THE INTEGRATION CONTRACT. Every destination in the app as a @Serializable route. Features
 * navigate only through these, never by importing another feature.
 *
 * To include:
 *  - login  →  :feature:auth  (Kirish)
 *  - home  →  :feature:home  (Bosh sahifa)
 *  - constitution  →  :feature:constitution  (Konstitutsiya)
 *  - article/{number}  →  :feature:constitution  (Modda (50-modda))
 *  - dictionary  →  :feature:dictionary  (Konstitutsiyaviy lug‘at)
 *  - learn  →  :feature:learn  (O‘rganish)
 *  - quiz/{quizId}  →  :feature:learn  (Test savoli)
 *  - cases  →  :feature:cases  (Kazuslar)
 *  - case/{caseId}  →  :feature:cases  (Kazus — yechim)
 *  - community  →  :feature:community  (Hamjamiyat)
 *  - post/{postId}  →  :feature:community  (Muhokama)
 *  - legalHelp  →  :feature:legalhelp  (Huquqiy yordam)
 *  - chat/{chatId}  →  :feature:legalhelp  (Advokat bilan chat)
 *  - profile  →  :feature:profile  (Profil)
 *  - search  →  global search (TBD)
 *  - bookmarks  →  saved items (TBD)
 *
 * PLACEHOLDER: not implemented yet.
 */
