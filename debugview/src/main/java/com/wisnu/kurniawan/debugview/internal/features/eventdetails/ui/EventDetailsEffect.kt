package com.wisnu.kurniawan.debugview.internal.features.eventdetails.ui

import com.wisnu.kurniawan.debugview.internal.model.Event

internal sealed interface EventDetailsEffect {
    data class Copy(val event: Event) : EventDetailsEffect
    data class Share(val event: Event) : EventDetailsEffect
}
