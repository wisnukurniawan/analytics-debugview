package com.wisnu.kurniawan.debugview.internal.features.event.ui

import com.wisnu.kurniawan.debugview.internal.model.Analytic

internal sealed interface EventEffect {
    data class NavigateToEventDetails(val id: String) : EventEffect
    object ShowFilterSheet : EventEffect
    data class Cleared(val analytic: Analytic) : EventEffect
}
