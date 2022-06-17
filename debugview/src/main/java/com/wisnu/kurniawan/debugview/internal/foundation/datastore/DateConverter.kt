package com.wisnu.kurniawan.debugview.internal.foundation.datastore

import androidx.room.TypeConverter
import com.wisnu.kurniawan.debugview.internal.foundation.extension.toLocalDateTime
import com.wisnu.kurniawan.debugview.internal.foundation.extension.toMillis
import java.time.LocalDateTime

internal class DateConverter {

    @TypeConverter
    fun toDate(date: Long?): LocalDateTime? {
        if (date == null) return null

        return date.toLocalDateTime()
    }

    @TypeConverter
    fun toDateLong(date: LocalDateTime?): Long? {
        if (date == null) return null

        return date.toMillis()
    }

}
