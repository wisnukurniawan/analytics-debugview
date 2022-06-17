package com.wisnu.kurniawan.debugview.internal.foundation.extension

import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.AnalyticDb
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.EventDb
import com.wisnu.kurniawan.debugview.model.Analytic
import com.wisnu.kurniawan.debugview.model.Event
import java.time.LocalDateTime

internal fun List<Analytic>.toAnalyticDbs(
    id: () -> String,
    createdAt: () -> LocalDateTime,
    updatedAt: (() -> LocalDateTime)?,
): List<AnalyticDb> {
    return map {
        it.toAnalyticDb(
            id = id,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}

internal fun Analytic.toAnalyticDb(
    id: () -> String,
    createdAt: () -> LocalDateTime,
    updatedAt: (() -> LocalDateTime)?,
): AnalyticDb {
    return AnalyticDb(
        id = id(),
        name = name,
        isRecording = isRecording,
        createdAt = createdAt(),
        updatedAt = updatedAt?.invoke()
    )
}

internal fun Event.toEventDb(
    analyticId: String,
    id: () -> String,
    createdAt: () -> LocalDateTime,
): EventDb {
    return EventDb(
        id = id(),
        analyticId = analyticId,
        name = name,
        properties = toJson(properties),
        createdAt = createdAt(),
    )
}
