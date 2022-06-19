package com.wisnu.kurniawan.debugview.internal.features.eventdetails.ui

internal sealed interface EventDetailsEffect {
    data class Copy(val content: String) : EventDetailsEffect
    data class Share(val content: String) : EventDetailsEffect
}
