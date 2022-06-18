package com.wisnu.kurniawan.debugview.internal.features.eventdetails.data

import com.wisnu.kurniawan.debugview.internal.foundation.datastore.LocalManager
import com.wisnu.kurniawan.debugview.internal.model.Event
import kotlinx.coroutines.flow.Flow

internal class EventDetailsEnvironment(private val localManager: LocalManager) : IEventDetailsEnvironment {
    override fun getEvent(id: String): Flow<Event> {
        return localManager.getEvent(id)
    }
}
