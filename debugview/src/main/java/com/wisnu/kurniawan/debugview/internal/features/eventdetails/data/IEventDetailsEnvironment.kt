package com.wisnu.kurniawan.debugview.internal.features.eventdetails.data

import com.wisnu.kurniawan.debugview.internal.model.Event
import kotlinx.coroutines.flow.Flow

internal interface IEventDetailsEnvironment {
    fun getEvent(id: String): Flow<Event>
}
