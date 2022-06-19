package com.wisnu.kurniawan.debugview.internal.features.eventfilter.ui

internal sealed interface EventFilterEffect {
    data class Dismiss(val text: String, val type: FilterType) : EventFilterEffect
}
