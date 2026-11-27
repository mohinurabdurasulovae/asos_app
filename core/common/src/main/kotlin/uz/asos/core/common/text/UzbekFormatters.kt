package uz.asos.core.common.text

object UzbekFormatters {

    fun display(text: String): String = text.replace('ʻ', '‘').replace('ʼ', '’')

    fun article(number: Int): String = "$number-modda"

    fun articleRange(from: Int, to: Int): String =
        if (from == to) article(from) else "$from–$to-moddalar"

    fun articleCount(count: Int): String = "$count modda"

    fun minutes(minutes: Int): String = "$minutes daqiqa"

    fun minutesLeft(minutes: Int): String = "$minutes daqiqa qoldi"

    fun sectionLabel(roman: String): String = "$roman BO‘LIM"

    fun chapterLabel(roman: String): String = "$roman bob"

    fun groupThousands(value: Int): String {
        val digits = kotlin.math.abs(value).toString()
        val grouped = digits.reversed().chunked(3).joinToString(" ").reversed()
        return if (value < 0) "-$grouped" else grouped
    }

    fun date(apiDate: String): String {
        val datePart = apiDate.take(10)
        val parts = datePart.split("-")
        if (parts.size != 3 || parts.any { it.isEmpty() }) return apiDate
        val (year, month, day) = parts
        return "$day.$month.$year"
    }
}
