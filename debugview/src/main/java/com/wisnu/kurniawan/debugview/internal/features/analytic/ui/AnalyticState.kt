package com.wisnu.kurniawan.debugview.internal.features.analytic.ui

import com.wisnu.kurniawan.debugview.internal.model.Analytic

internal data class AnalyticState(
    val analytics: List<Analytic> = listOf(),
    val isFilterApplied: Boolean = false
)
