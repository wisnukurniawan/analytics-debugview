package com.wisnu.kurniawan.debugview.internal.foundation.datastore.model

import androidx.room.Embedded
import androidx.room.Relation

internal data class AnalyticWithEvent(
    @Embedded val analytic: AnalyticDb,
    @Relation(
        entity = EventDb::class,
        parentColumn = "analytic_id",
        entityColumn = "event_analyticId"
    )
    val events: List<EventDb>
)
