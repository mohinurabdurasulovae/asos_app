/*
 * DeepLinks.kt
 * Module: :core:navigation   ·   Work package: WP-00
 *
 * Deep link patterns, e.g. asos://article/{key}. Register them on the destination with
 * navDeepLink<ArticleRoute>(basePath = DeepLinks.ARTICLE) and add the intent filter in
 * app/src/main/AndroidManifest.xml.
 *
 * Implemented: article. Still to add: post, case, quiz, chat.
 */
package uz.asos.core.navigation

object DeepLinks {
    const val SCHEME = "asos"

    /** asos://article/{key} → ArticleRoute(key) */
    const val ARTICLE = "$SCHEME://article"
}
