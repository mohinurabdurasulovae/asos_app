/*
 * DictionaryDto.kt
 * Module: :core:network   ·   Work package: WP-02 (filled in for WP-06)
 *
 * @Serializable DTOs for the Lug‘at endpoints. Field names mirror the OpenAPI schemas.
 * TermBriefDto lives in ConstitutionDto.kt (the article reader uses it too).
 */
package uz.asos.core.network.dto

import kotlinx.serialization.Serializable

/** openapi.yaml: Term. */
@Serializable
data class TermDto(
    val id: Int = 0,
    val term: String = "",
    val slug: String = "",
    val letter: String? = null,
    val partOfSpeech: String? = null,
    val etymology: String? = null,
    val definition: String = "",
    val inLife: String? = null,
    val shortDef: String? = null,
    val articles: List<ArticleRefDto> = emptyList(),
    val isSaved: Boolean = false,
)

/** openapi.yaml: Letter. */
@Serializable
data class TermLetterDto(
    val letter: String = "",
    val count: Int = 0,
)

/** GET /v1/terms */
@Serializable
data class TermListResponseDto(
    val ok: Boolean = true,
    val items: List<TermBriefDto> = emptyList(),
    val meta: ListMetaDto = ListMetaDto(),
    val letters: List<TermLetterDto> = emptyList(),
)

/** GET /v1/terms/{slug}, GET /v1/terms/of-the-day */
@Serializable
data class TermResponseDto(
    val ok: Boolean = true,
    val term: TermDto = TermDto(),
)
