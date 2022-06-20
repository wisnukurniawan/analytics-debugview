package com.wisnu.kurniawan.debugview.internal.features.event.ui

import com.wisnu.kurniawan.debugview.internal.foundation.wrapper.DateTimeProviderImpl
import com.wisnu.kurniawan.debugview.internal.model.Analytic
import com.wisnu.kurniawan.debugview.internal.model.Event

internal data class EventState(
    val analytic: Analytic,
    val events: List<Event>,
    val searchText: String
) {

    companion object {
        val initial = EventState(
            Analytic(
                id = "",
                tag = "",
                isRecording = false,
                createdAt = DateTimeProviderImpl().now()
            ),
            listOf(),
            ""
        )
    }
}
