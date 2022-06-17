package com.wisnu.kurniawan.debugview.internal.foundation.extension

import com.wisnu.kurniawan.debugview.exception.TagDuplicateException
import com.wisnu.kurniawan.debugview.exception.TagEmptyException

internal inline fun require(tags: List<String>) {
    if (tags.isEmpty()) throw TagEmptyException()

    tags.forEach { if (it.isBlank()) throw TagEmptyException() }

    val distinctSize = tags.distinct().size
    val actualSize = tags.size
    if (distinctSize != actualSize) throw TagDuplicateException()
}
