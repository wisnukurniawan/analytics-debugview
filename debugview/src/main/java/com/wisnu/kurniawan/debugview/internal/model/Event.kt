package com.wisnu.kurniawan.debugview.model

import java.time.LocalDateTime

data class Event(
    val id: String,
    val name: String,
    val properties: Map<String, String>,
    val createdAt: LocalDateTime
)
