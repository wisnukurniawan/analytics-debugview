package com.wisnu.kurniawan.debugview.internal.features.eventdetails.ui

import com.wisnu.kurniawan.debugview.internal.foundation.wrapper.DateTimeProviderImpl
import com.wisnu.kurniawan.debugview.internal.model.Event

internal data class EventDetailsState(
    val event: Event
) {
    companion object {
        val initial = EventDetailsState(
            event = Event(
                id = "",
                name = "",
                properties = emptyMap(),
                createdAt = DateTimeProviderImpl().now()
            )
        )
    }
}
