package com.wisnu.kurniawan.debugview.internal.foundation.extension

import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.EventDb
import com.wisnu.kurniawan.debugview.internal.model.Event

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
