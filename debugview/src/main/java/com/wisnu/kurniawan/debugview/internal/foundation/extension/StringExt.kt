package com.wisnu.kurniawan.debugview.internal.foundation.extension

/**
 * Build phrase queries
 */
fun String.sanitizeQuery(): String {
    return trim()
        .split(" ")
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .map { it.replace(Regex.fromLiteral("\""), "\"\"") }
        .joinToString(separator = " ") { "\"$it*\"" }
}
