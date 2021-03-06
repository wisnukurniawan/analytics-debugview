package com.wisnu.kurniawan.debugview.internal.features.analytic.ui

internal sealed interface AnalyticEffect {
    data class NavigateToEvent(val tag: String) : AnalyticEffect
    object ShowFilterSheet : AnalyticEffect
}
