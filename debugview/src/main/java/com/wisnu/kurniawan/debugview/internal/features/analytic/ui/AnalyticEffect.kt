package com.wisnu.kurniawan.debugview.internal.features.analytic.ui

internal sealed interface AnalyticEffect {
    data class NavigateToEvent(val tag: String, val isSingle: Boolean = false) : AnalyticEffect
}
