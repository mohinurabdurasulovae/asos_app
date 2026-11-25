/*
 * UserModels.kt
 * Module: :core:model   ·   Work package: WP-02
 *
 * Domain models shared by every layer.
 *
 * Still to add (WP-11 / WP-02): User, UserStats, WeeklyActivity, Bookmark, UserSettings.
 */
package uz.asos.core.model.user

/** Kinds accepted by /v1/me/bookmarks. [apiValue] is the value the API expects. */
enum class BookmarkType(val apiValue: String) {
    ARTICLE("article"),
    TERM("term"),
    POST("post"),
    CASE("case"),
}

/** Reader text size. [apiValue] matches PATCH /v1/me `textSize`. */
enum class ReaderTextSize(val apiValue: String) {
    SMALL("small"),
    MEDIUM("medium"),
    LARGE("large"),
    XLARGE("xlarge");

    companion object {
        fun fromApi(value: String?): ReaderTextSize =
            entries.firstOrNull { it.apiValue == value } ?: MEDIUM
    }
}
