package com.wisnu.kurniawan.debugview.internal.foundation.extension

import com.wisnu.foundation.corejson.mapFromJson
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.AnalyticDb
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.EventDb
import com.wisnu.kurniawan.debugview.internal.model.Analytic
import com.wisnu.kurniawan.debugview.internal.model.Event

internal fun List<EventDb>.toEvents(): List<Event> {
    return map { it.toEvent() }
}

internal fun AnalyticDb.toAnalytic(): Analytic {
    return Analytic(
        tag = tag,
        id = id,
        isRecording = isRecording,
        createdAt = createdAt,
    )
}

internal fun EventDb.toEvent(): Event {
    return Event(
        name = name,
        properties = properties.mapFromJson(),
        id = id,
        createdAt = createdAt
    )
}
