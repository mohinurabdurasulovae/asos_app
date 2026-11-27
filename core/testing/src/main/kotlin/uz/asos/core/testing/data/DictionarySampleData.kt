package uz.asos.core.testing.data

import uz.asos.core.model.dictionary.AlphabetLetter
import uz.asos.core.model.dictionary.TermArticle
import uz.asos.core.model.dictionary.TermDetail
import uz.asos.core.model.dictionary.TermSummary
import uz.asos.core.model.dictionary.TermsPage

object DictionarySampleData {

    val daxlsizlik = TermDetail(
        id = 1,
        term = "Daxlsizlik",
        slug = "daxlsizlik",
        letter = "D",
        partOfSpeech = "ot",
        etymology = "arabcha “daxl” — tegish, aralashish",
        definition = "Shaxs, uy-joy yoki yozishmalarga ruxsatsiz tegib, aralashib " +
            "bo‘lmasligi — ularning qonun bilan himoyalanganligi.",
        inLife = "Qonunda belgilangan holatlardan tashqari, telefoningizdagi xabarlarni " +
            "sizning roziligingizsiz hech kim o‘qiy olmaydi.",
        shortDef = "Ruxsatsiz tegib, aralashib bo‘lmaslik.",
        articles = listOf(
            TermArticle(27, "Erkinlik va shaxsiy daxlsizlik", "27-modda-erkinlik-va-shaxsiy-daxlsizlik"),
            TermArticle(31, "Shaxsiy hayot va uy-joy daxlsizligi", "31-modda-shaxsiy-hayot-va-uy-joy-daxlsizligi"),
        ),
        isSaved = true,
    )

    val terms = listOf(
        TermSummary(
            id = 1,
            term = "Daxlsizlik",
            slug = "daxlsizlik",
            letter = "D",
            shortDefinition = "Ruxsatsiz tegib, aralashib bo‘lmaslik.",
            articleNumbers = listOf(27, 31),
            isSaved = true,
        ),
        TermSummary(
            id = 2,
            term = "Davlat suvereniteti",
            slug = "davlat-suvereniteti",
            letter = "D",
            shortDefinition = "Davlatning o‘z hududida to‘liq mustaqil va oliy hokimiyatga egaligi.",
            articleNumbers = listOf(1),
            isSaved = false,
        ),
        TermSummary(
            id = 3,
            term = "Davlat tili",
            slug = "davlat-tili",
            letter = "D",
            shortDefinition = "Davlat ishlarida rasmiy qo‘llanadigan til. O‘zbekistonda — o‘zbek tili.",
            articleNumbers = listOf(4),
            isSaved = false,
        ),
        TermSummary(
            id = 4,
            term = "Demokratiya",
            slug = "demokratiya",
            letter = "D",
            shortDefinition = "Hokimiyat manbai xalq bo‘lgan boshqaruv tartibi.",
            articleNumbers = listOf(7, 8),
            isSaved = true,
        ),
        TermSummary(
            id = 5,
            term = "E’tiqod erkinligi",
            slug = "etiqod-erkinligi",
            letter = "E",
            shortDefinition = "Har kimning o‘z qarashlari va ishonchiga ega bo‘lish huquqi.",
            articleNumbers = listOf(33),
            isSaved = false,
        ),
        TermSummary(
            id = 6,
            term = "Ariza",
            slug = "ariza",
            letter = "A",
            shortDefinition = "Davlat organiga yozma murojaat.",
            articleNumbers = emptyList(),
            isSaved = false,
        ),
    )

    val letters: List<AlphabetLetter> = listOf(
        "A" to 1, "B" to 1, "D" to 4, "E" to 1, "F" to 1, "G" to 0, "H" to 0, "I" to 1,
        "J" to 0, "K" to 1, "L" to 0, "M" to 1, "N" to 1, "O" to 1, "P" to 2, "Q" to 1,
        "R" to 1, "S" to 1, "T" to 1, "U" to 1, "V" to 0, "X" to 0, "Y" to 0, "Z" to 0,
        "O‘" to 1, "G‘" to 1, "Sh" to 1, "Ch" to 1, "Ng" to 0,
    ).map { (letter, count) -> AlphabetLetter(letter = letter, count = count) }

    val page = TermsPage(
        terms = terms,
        letters = letters,
        page = 1,
        total = terms.size,
        hasMore = false,
    )

    val termOfTheDay = daxlsizlik
}
