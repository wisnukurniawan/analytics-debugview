package com.wisnu.kurniawan.debugview.internal.foundation.extension

import com.wisnu.kurniawan.debugview.internal.foundation.wrapper.DateTimeProviderImpl
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


internal fun Long.toLocalDateTime(): LocalDateTime {
    val zoneId = ZoneId.systemDefault()
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), zoneId)
}

internal fun LocalDateTime.toMillis(): Long {
    val zoneId = ZoneId.systemDefault()
    return atZone(zoneId).toInstant().toEpochMilli()
}

internal fun LocalDateTime.formatDateTime(currentDate: LocalDateTime = DateTimeProviderImpl().now()): String {
    val patternWithYear = "EEE, dd MMM yyyy"
    val patternWithoutYear = "EEE, dd MMM"
    val zoneId = ZoneId.systemDefault()
    val locale = Locale.getDefault()

    return if (year == currentDate.year) {
        SimpleDateFormat(patternWithoutYear, locale).format(atZone(zoneId).toInstant().toEpochMilli())
    } else {
        SimpleDateFormat(patternWithYear, locale).format(atZone(zoneId).toInstant().toEpochMilli())
    }
}
