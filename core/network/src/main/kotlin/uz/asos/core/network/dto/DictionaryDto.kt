package uz.asos.core.network.dto

import kotlinx.serialization.Serializable

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
    val articles: List<Int> = emptyList(),
    val isSaved: Boolean = false,
)

@Serializable
data class AlphabetLetterDto(
    val letter: String,
    val count: Int = 0,
)

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
