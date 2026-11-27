package uz.asos.core.data.mapper

import uz.asos.core.model.dictionary.AlphabetLetter
import uz.asos.core.model.dictionary.TermSummary
import uz.asos.core.model.dictionary.TermArticle
import uz.asos.core.model.dictionary.TermDetail
import uz.asos.core.model.dictionary.TermsPage
import uz.asos.core.network.dto.AlphabetLetterDto
import uz.asos.core.network.dto.ArticleRefDto
import uz.asos.core.network.dto.TermDto
import uz.asos.core.network.dto.TermListItemDto
import uz.asos.core.network.dto.TermsResponseDto

internal fun TermsResponseDto.asModel() = TermsPage(
    terms = items.map { it.asModel() },
    letters = letters.map { it.asModel() },
    page = meta?.page ?: 1,
    total = meta?.total ?: items.size,
    hasMore = meta?.hasMore ?: false,
)

internal fun TermListItemDto.asModel() = TermSummary(
    id = id,
    term = term,
    slug = slug,
    letter = letter,
    shortDefinition = shortDef,
    articleNumbers = articles,
    isSaved = isSaved,
)

internal fun AlphabetLetterDto.asModel() = AlphabetLetter(
    letter = letter,
    count = count,
)

internal fun TermDto.asModel() = TermDetail(
    id = id,
    term = term,
    slug = slug,
    letter = letter,
    partOfSpeech = partOfSpeech?.takeIf { it.isNotBlank() },
    etymology = etymology?.takeIf { it.isNotBlank() },
    definition = definition,
    inLife = inLife?.takeIf { it.isNotBlank() },
    shortDef = shortDef,
    articles = articles.map { it.asTermArticle() },
    isSaved = isSaved,
)

internal fun ArticleRefDto.asTermArticle() = TermArticle(
    number = number,
    title = title,
    slug = slug,
)
