/*
 * UserDto.kt
 * Module: :core:network   ·   Work package: WP-02
 *
 * @Serializable DTOs for the /v1/me endpoints. Field names mirror the OpenAPI schemas.
 *
 * Implemented: bookmarks (save / remove). Still to add: Me, profile screen, points,
 * notifications, achievements.
 */
package uz.asos.core.network.dto

import kotlinx.serialization.Serializable

/**
 * POST /v1/me/bookmarks. Send [kind] plus one of: [targetId], [articleNumber], [termSlug],
 * [caseNumber]. Null fields are left out of the JSON.
 */
@Serializable
data class BookmarkRequestDto(
    /** article | term | post | case */
    val kind: String,
    val targetId: Int? = null,
    val articleNumber: Int? = null,
    val termSlug: String? = null,
    val caseNumber: Int? = null,
)

@Serializable
data class BookmarkResponseDto(
    val ok: Boolean = true,
    val saved: Boolean = false,
    val kind: String? = null,
    val targetId: Int? = null,
)
