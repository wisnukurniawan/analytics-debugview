package com.wisnu.kurniawan.debugview.internal.foundation.extension

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


internal fun Long.toLocalDateTime(): LocalDateTime {
    val zoneId = ZoneId.systemDefault()
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), zoneId)
}

internal fun LocalDateTime.toMillis(): Long {
    val zoneId = ZoneId.systemDefault()
    return atZone(zoneId).toInstant().toEpochMilli()
}
