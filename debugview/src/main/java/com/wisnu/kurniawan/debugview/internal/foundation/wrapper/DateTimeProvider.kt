package com.wisnu.kurniawan.debugview.internal.foundation.wrapper

import java.time.LocalDateTime

internal interface DateTimeProvider {
    fun now(): LocalDateTime
}

internal class DateTimeProviderImpl : DateTimeProvider {
    override fun now(): LocalDateTime {
        return LocalDateTime.now()
    }
}

