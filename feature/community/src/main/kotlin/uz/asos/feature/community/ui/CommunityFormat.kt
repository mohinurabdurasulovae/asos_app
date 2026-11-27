package uz.asos.feature.community.ui

import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

internal fun String.shortAge(now: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC)): String {
    val written = runCatching { LocalDateTime.parse(this, API_FORMAT) }.getOrNull()
        ?: return this

    val minutes = Duration.between(written, now).toMinutes()
    return when {
        minutes < 1 -> "hozir"
        minutes < 60 -> "$minutes daq"
        minutes < 60 * 24 -> "${minutes / 60} soat"
        minutes < 60 * 48 -> "kecha"
        minutes < 60 * 24 * 7 -> "${minutes / (60 * 24)} kun"
        else -> written.format(DATE_FORMAT)
    }
}

internal fun memberCountLabel(count: Int): String = when {
    count < 1_000 -> "$count a’zo"
    else -> {
        val thousands = count / 1_000
        val remainder = (count % 1_000) / 100
        if (remainder == 0) "$thousands ming a’zo" else "$thousands,$remainder ming a’zo"
    }
}

private val API_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
private val DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
