package com.wisnu.kurniawan.debugview.internal.model

import java.time.LocalDateTime

internal data class Event(
    val id: String,
    val name: String,
    val properties: Map<String, String>,
    val createdAt: LocalDateTime
)
