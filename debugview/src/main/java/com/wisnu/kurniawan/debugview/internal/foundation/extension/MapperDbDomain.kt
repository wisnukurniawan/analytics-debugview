package com.wisnu.kurniawan.debugview.internal.foundation.extension

import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.AnalyticDb
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.EventDb
import com.wisnu.kurniawan.debugview.model.Analytic
import com.wisnu.kurniawan.debugview.model.Event

internal fun List<AnalyticDb>.toAnalytics(): List<Analytic> {
    return map { it.toAnalytic() }
}

internal fun List<EventDb>.toEvents(): List<Event> {
    return map { it.toEvent() }
}

internal fun AnalyticDb.toAnalytic(): Analytic {
    return Analytic(
        name = name,
        isRecording = isRecording
    )
}

internal fun EventDb.toEvent(): Event {
    return Event(
        name = name,
        properties = properties.mapFromJson()
    )
}
