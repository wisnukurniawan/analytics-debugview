package com.wisnu.kurniawan.debugview.internal.features.event.ui

internal sealed interface EventEffect {
    data class NavigateToEventDetails(val id: String) : EventEffect
    data class ShowFilterSheet(val filterConfig: FilterConfig) : EventEffect
}
