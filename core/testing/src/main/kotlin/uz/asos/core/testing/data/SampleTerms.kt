/*
 * SampleTerms.kt
 * Module: :core:testing   ·   Work package: WP-02 (added for WP-06)
 *
 * Dictionary sample data, copied from the live API (GET /v1/terms, /v1/terms/daxlsizlik) and
 * the Lugat.dc.html artboard. Two terms are saved, so the Saqlangan filter has content.
 */
package uz.asos.core.testing.data

import uz.asos.core.model.constitution.ArticleRef
import uz.asos.core.model.dictionary.Term
import uz.asos.core.model.dictionary.TermLetter
import uz.asos.core.model.dictionary.TermList
import uz.asos.core.model.dictionary.TermSummary

object SampleTerms {

    val daxlsizlik = Term(
        id = 1,
        term = "Daxlsizlik",
        slug = "daxlsizlik",
        letter = "D",
        partOfSpeech = "ot",
        etymology = "arabcha “daxl” — tegish, aralashish",
        definition = "Shaxs, uy-joy yoki yozishmalarga ruxsatsiz tegib, aralashib bo‘lmasligi — ularning qonun bilan himoyalanganligi.",
        inLife = "Qonunda belgilangan holatlardan tashqari, telefoningizdagi xabarlarni sizning roziligingizsiz hech kim o‘qiy olmaydi.",
        shortDefinition = "Ruxsatsiz tegib, aralashib bo‘lmaslik.",
        articles = listOf(
            ArticleRef(27, "Erkinlik va shaxsiy daxlsizlik", "27-modda-erkinlik-va-shaxsiy-daxlsizlik"),
            ArticleRef(31, "Shaxsiy hayot va uy-joy daxlsizligi", "31-modda-shaxsiy-hayot-va-uy-joy-daxlsizligi"),
        ),
        isSaved = true,
    )

    private fun brief(id: Int, term: String, slug: String, letter: String, short: String, articles: List<Int>, saved: Boolean = false) =
        TermSummary(id, term, slug, letter, short, articles, saved)

    val items: List<TermSummary> = listOf(
        brief(24, "Ariza", "ariza", "A", "Davlat organiga yozma murojaat.", emptyList()),
        brief(25, "Burch", "burch", "B", "Konstitutsiya yuklagan majburiyat.", listOf(60)),
        brief(26, "Chetlatish", "chetlatish", "Ch", "Darsga yoki ishga kiritmaslik.", listOf(50)),
        brief(1, "Daxlsizlik", "daxlsizlik", "D", "Ruxsatsiz tegib, aralashib bo‘lmaslik.", listOf(27, 31), saved = true),
        brief(2, "Davlat suvereniteti", "davlat-suvereniteti", "D", "Davlatning o‘z hududida to‘liq mustaqilligi.", listOf(1)),
        brief(3, "Davlat tili", "davlat-tili", "D", "Davlat ishlarida rasmiy qo‘llanadigan til.", listOf(4)),
        brief(4, "Demokratiya", "demokratiya", "D", "Hokimiyat manbai xalq bo‘lgan boshqaruv tartibi.", emptyList(), saved = true),
        brief(5, "Dunyoviy davlat", "dunyoviy-davlat", "D", "Din davlatdan ajratilgan davlat.", listOf(1)),
        brief(11, "Fuqarolik", "fuqarolik", "F", "Shaxs va davlat o‘rtasidagi barqaror huquqiy aloqa.", emptyList()),
        brief(10, "Kafolat", "kafolat", "K", "Huquqning amalda ishlashini ta’minlovchi vosita.", listOf(55)),
        brief(13, "Shaxsiy daxlsizlik", "shaxsiy-daxlsizlik", "Sh", "Erkinlikni asossiz cheklab bo‘lmasligi.", listOf(27)),
    )

    /** The 29-letter Uzbek Latin rail, in the API's order. */
    val letters: List<TermLetter> = listOf(
        "A", "B", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
        "U", "V", "X", "Y", "Z", "O‘", "G‘", "Sh", "Ch", "Ng",
    ).map { letter -> TermLetter(letter, items.count { it.letter == letter }) }

    val list = TermList(items = items, letters = letters, total = items.size)

    /** Full entry for [slug]: Daxlsizlik in full, the rest built from the list item. */
    fun detail(slug: String): Term? {
        if (slug == daxlsizlik.slug) return daxlsizlik
        val item = items.firstOrNull { it.slug == slug } ?: return null
        return Term(
            id = item.id,
            term = item.term,
            slug = item.slug,
            letter = item.letter,
            partOfSpeech = "ot",
            etymology = null,
            definition = item.shortDefinition.orEmpty(),
            inLife = null,
            shortDefinition = item.shortDefinition,
            articles = item.articleNumbers.map { ArticleRef(it, "$it-modda", "$it-modda") },
            isSaved = item.isSaved,
        )
    }
}
