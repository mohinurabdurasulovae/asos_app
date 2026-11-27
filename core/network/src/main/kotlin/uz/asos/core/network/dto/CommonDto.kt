package uz.asos.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDto(
    val ok: Boolean = false,
    val error: String? = null,
    val message: String? = null,
    val field: String? = null,
    val retryAfter: Int? = null,
    val triesLeft: Int? = null,
)

@Serializable
data class ListMetaDto(
    val total: Int = 0,
    val page: Int = 1,
    val perPage: Int = 20,
    val pages: Int = 1,
    val hasMore: Boolean = false,
)

@Serializable
data class ArticleRefDto(
    val number: Int = 0,
    val title: String = "",
    val slug: String = "",
)
