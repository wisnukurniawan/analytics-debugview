package com.wisnu.kurniawan.debugview.internal.features.analytic.ui

import com.wisnu.kurniawan.debugview.R
import com.wisnu.kurniawan.debugview.internal.model.Analytic

internal data class AnalyticState(
    val title: Int = R.string.debugview_analytic_title,
    val analytics: List<Analytic> = listOf()
)
