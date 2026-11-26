/*
 * DictionaryMappers.kt
 * Module: :core:data   ·   Work package: WP-02 (filled in for WP-06)
 *
 * DTO → core:model mappers for the dictionary. TermBriefDto.asModel() is in
 * ConstitutionMappers.kt (shared with the article reader).
 */
package uz.asos.core.data.mapper

import uz.asos.core.model.constitution.ArticleRef
import uz.asos.core.model.dictionary.Term
import uz.asos.core.model.dictionary.TermLetter
import uz.asos.core.model.dictionary.TermList
import uz.asos.core.network.dto.ArticleRefDto
import uz.asos.core.network.dto.TermDto
import uz.asos.core.network.dto.TermLetterDto
import uz.asos.core.network.dto.TermListResponseDto

internal fun TermListResponseDto.asModel() = TermList(
    items = items.map { it.asModel() },
    letters = letters.map { it.asModel() },
    total = meta.total,
)

internal fun TermLetterDto.asModel() = TermLetter(letter = letter, count = count)

internal fun TermDto.asModel() = Term(
    id = id,
    term = term,
    slug = slug,
    letter = letter,
    partOfSpeech = partOfSpeech?.takeIf { it.isNotBlank() },
    etymology = etymology?.takeIf { it.isNotBlank() },
    definition = definition,
    inLife = inLife?.takeIf { it.isNotBlank() },
    shortDefinition = shortDef,
    articles = articles.map { it.asTermArticleRef() },
    isSaved = isSaved,
)

private fun ArticleRefDto.asTermArticleRef() = ArticleRef(number = number, title = title, slug = slug)
