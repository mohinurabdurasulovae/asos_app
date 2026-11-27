package uz.asos.feature.legalhelp.ui

import uz.asos.core.common.text.UzbekFormatters
import uz.asos.feature.legalhelp.help.FreeSlotUi
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale

internal object LegalHelpFormat {

    val TASHKENT: ZoneId = ZoneId.of("Asia/Tashkent")

    private val DATE = Regex("""(\d{4})-(\d{2})-(\d{2})""")
    private val TIME = Regex("""(\d{2}):(\d{2})""")

    fun today(): LocalDate = LocalDate.now(TASHKENT)

    fun time(serverTime: String): String = TIME.find(serverTime.substringAfter('T').substringAfter(' '))
        ?.value
        ?: TIME.find(serverTime)?.value
        ?: ""

    fun date(serverTime: String): LocalDate? = DATE.find(serverTime)?.let { match ->
        val (y, m, d) = match.destructured
        runCatching { LocalDate.of(y.toInt(), m.toInt(), d.toInt()) }.getOrNull()
    }

    fun fullDate(date: LocalDate): String = "%02d.%02d.%d".format(Locale.ROOT, date.dayOfMonth, date.monthValue, date.year)

    fun freeSlot(raw: String?, today: LocalDate = today()): FreeSlotUi? {
        if (raw.isNullOrBlank()) return null
        val date = date(raw) ?: return null
        val time = time(raw).ifEmpty { return null }
        return FreeSlotUi(
            raw = raw,
            isToday = date == today,
            dateLabel = "%02d.%02d".format(Locale.ROOT, date.dayOfMonth, date.monthValue),
            time = time,
        )
    }

    fun rating(value: Double): String = String.format(Locale.ROOT, "%.1f", value).replace('.', ',')

    fun dialNumber(phone: String): String {
        val digits = phone.filter { it.isDigit() }
        return if (phone.trim().startsWith("+")) "+$digits" else digits
    }

    fun specialtyLine(specialties: List<String>): String {
        val items = specialties.map { UzbekFormatters.display(it.trim()) }.filter { it.isNotEmpty() }
        if (items.isEmpty()) return ""
        val rest = items.drop(1).map { it.replaceFirstChar { c -> c.lowercase(Locale.ROOT) } }
        return when (rest.size) {
            0 -> items.first()
            1 -> "${items.first()} va ${rest.first()}"
            else -> (listOf(items.first()) + rest.dropLast(1)).joinToString(", ") + " va " + rest.last()
        }
    }

    fun fileMeta(bytes: Long, fileName: String, mime: String?): String {
        val size = when {
            bytes <= 0 -> null
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> "${bytes / 1024} KB"
            else -> String.format(Locale.ROOT, "%.1f MB", bytes / (1024.0 * 1024.0)).replace('.', ',')
        }
        val extension = fileName.substringAfterLast('.', "").lowercase(Locale.ROOT)
        val kind = when {
            extension == "doc" || extension == "docx" || mime?.contains("word") == true -> "Word"
            extension == "pdf" || mime == "application/pdf" -> "PDF"
            extension in setOf("jpg", "jpeg", "png", "webp") || mime?.startsWith("image/") == true -> "Rasm"
            extension.isNotEmpty() -> extension.uppercase(Locale.ROOT)
            else -> null
        }
        return listOfNotNull(size, kind).joinToString(" · ")
    }
}
