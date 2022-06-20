package com.wisnu.kurniawan.debugview.internal.foundation.extension

import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.AnalyticDb
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.EventDb
import com.wisnu.kurniawan.debugview.internal.model.Analytic
import com.wisnu.kurniawan.debugview.internal.model.Event

internal fun List<Analytic>.toAnalyticDb(): List<AnalyticDb> {
    return map {
        it.toAnalyticDb()
    }
}

internal fun Analytic.toAnalyticDb(): AnalyticDb {
    return AnalyticDb(
        id = id,
        tag = tag,
        isRecording = isRecording,
        createdAt = createdAt,
        updatedAt = null
    )
}

internal fun Event.toEventDb(
    analyticId: String,
): EventDb {
    return EventDb(
        id = id,
        analyticId = analyticId,
        name = name,
        properties = toJson(properties),
        createdAt = createdAt,
    )
}
