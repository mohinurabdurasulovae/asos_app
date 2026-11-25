/*
 * UzbekFormatters.kt
 * Module: :core:common   ·   Work package: WP-00
 *
 * Uzbek number / label formatting used by ViewModels, so screens get ready-made text.
 * Uzbek Latin uses ‘ (U+2018) in o‘ and g‘, and ’ (U+2019) for the tutuq belgisi.
 */
package uz.asos.core.common.text

object UzbekFormatters {

    /**
     * Text for the interface font (Plus Jakarta Sans), which has no ʻ (U+02BB) or ʼ (U+02BC).
     * Shows them as ‘ and ’, as in the design. Do NOT use on the official article text: that
     * stays verbatim and uses Source Serif 4, which has the glyphs.
     */
    fun display(text: String): String = text.replace('ʻ', '‘').replace('ʼ', '’')

    /** 50 → "50-modda". */
    fun article(number: Int): String = "$number-modda"

    /** 41, 53 → "41–53-moddalar"; 154, 154 → "154-modda". */
    fun articleRange(from: Int, to: Int): String =
        if (from == to) article(from) else "$from–$to-moddalar"

    /** 13 → "13 modda". */
    fun articleCount(count: Int): String = "$count modda"

    /** 4 → "4 daqiqa". */
    fun minutes(minutes: Int): String = "$minutes daqiqa"

    /** 3 → "3 daqiqa qoldi". */
    fun minutesLeft(minutes: Int): String = "$minutes daqiqa qoldi"

    /** "II" → "II BO‘LIM". */
    fun sectionLabel(roman: String): String = "$roman BO‘LIM"

    /** "IX" → "IX bob". */
    fun chapterLabel(roman: String): String = "$roman bob"

    /** 1240 → "1 240". */
    fun groupThousands(value: Int): String {
        val digits = kotlin.math.abs(value).toString()
        val grouped = digits.reversed().chunked(3).joinToString(" ").reversed()
        return if (value < 0) "-$grouped" else grouped
    }

    /**
     * API date "2023-05-01" (or "2023-05-01 10:00:00") → "01.05.2023".
     * Returns the input unchanged if it is not in that shape.
     */
    fun date(apiDate: String): String {
        val datePart = apiDate.take(10)
        val parts = datePart.split("-")
        if (parts.size != 3 || parts.any { it.isEmpty() }) return apiDate
        val (year, month, day) = parts
        return "$day.$month.$year"
    }
}
