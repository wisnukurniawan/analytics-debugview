package com.wisnu.kurniawan.debugview.internal.model

import java.time.LocalDateTime

internal data class Analytic(
    val id: String,
    val tag: String,
    val isRecording: Boolean,
    val createdAt: LocalDateTime,
)
