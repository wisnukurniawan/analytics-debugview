package com.wisnu.kurniawan.debugview.internal.foundation.extension

import com.wisnu.kurniawan.debugview.exception.AnalyticDuplicateException
import com.wisnu.kurniawan.debugview.exception.AnalyticEmptyException
import com.wisnu.kurniawan.debugview.model.Analytic

internal inline fun require(analytics: List<Analytic>) {
    if (analytics.isEmpty()) throw AnalyticEmptyException()

    analytics.forEach { if (it.name.isBlank()) throw AnalyticEmptyException() }

    val distinctSize = analytics.distinctBy { it.name }.size
    val actualSize = analytics.size
    if (distinctSize != actualSize) throw AnalyticDuplicateException()
}
