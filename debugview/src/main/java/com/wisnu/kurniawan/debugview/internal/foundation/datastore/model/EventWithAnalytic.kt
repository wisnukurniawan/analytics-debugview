package com.wisnu.kurniawan.debugview.internal.foundation.datastore.model

import androidx.room.Embedded

internal data class EventWithAnalytic(
    @Embedded val analytic: AnalyticDb,
    @Embedded val event: EventDb
)
