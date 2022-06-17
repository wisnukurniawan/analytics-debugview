package com.wisnu.kurniawan.debugview

data class Event(
    val tag: String,
    val name: String,
    val properties: Map<String, String> = emptyMap()
)
