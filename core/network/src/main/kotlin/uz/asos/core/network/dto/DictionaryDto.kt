/*
 * DictionaryDto.kt
 * Module: :core:network   ·   Work package: WP-02
 *
 * Wire shapes of the Lug‘at tag in docs/api/openapi.yaml.
 *
 * One deviation from the written spec, checked against the live API: the list endpoint sends
 * `articles` as bare article numbers, while the detail endpoint sends full objects. Each has
 * its own DTO below, so neither has to guess.
 */
package uz.asos.core.network.dto

import kotlinx.serialization.Serializable

/** GET /v1/terms */
@Serializable
data class TermsResponseDto(
    val ok: Boolean = true,
    val items: List<TermListItemDto> = emptyList(),
    val meta: ListMetaDto? = null,
    val letters: List<AlphabetLetterDto> = emptyList(),
)

@Serializable
data class TermListItemDto(
    val id: Int,
    val term: String,
    val slug: String,
    val letter: String = "",
    val shortDef: String = "",
    /** Article numbers only on this endpoint. */
    val articles: List<Int> = emptyList(),
    val isSaved: Boolean = false,
)

@Serializable
data class AlphabetLetterDto(
    val letter: String,
    val count: Int = 0,
)

/** GET /v1/terms/{slug} and GET /v1/terms/of-the-day */
@Serializable
data class TermResponseDto(
    val ok: Boolean = true,
    val term: TermDto,
)

@Serializable
data class TermDto(
    val id: Int,
    val term: String,
    val slug: String,
    val letter: String = "",
    val partOfSpeech: String? = null,
    val etymology: String? = null,
    val definition: String = "",
    val inLife: String? = null,
    val shortDef: String = "",
    val articles: List<ArticleRefDto> = emptyList(),
    val isSaved: Boolean = false,
)
