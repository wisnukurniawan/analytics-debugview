package com.wisnu.kurniawan.debugview.internal.features.event.data

import com.wisnu.kurniawan.debugview.internal.foundation.datastore.LocalManager
import com.wisnu.kurniawan.debugview.internal.foundation.extension.sanitizeQuery
import com.wisnu.kurniawan.debugview.internal.model.Analytic
import com.wisnu.kurniawan.debugview.internal.model.Event
import kotlinx.coroutines.flow.Flow

internal class EventEnvironment(private val localManager: LocalManager) : IEventEnvironment {
    override fun getAnalytic(tag: String): Flow<Analytic> {
        return localManager.getAnalytic(tag)
    }

    override fun searchEvent(analyticId: String, query: String): Flow<List<Event>> {
        return if (query.isBlank()) {
            localManager.getEvents(analyticId, LIMIT)
        } else {
            localManager.searchEvent(analyticId, LIMIT, query.sanitizeQuery())
        }
    }

    override suspend fun updateAnalytic(analytic: Analytic) {
        localManager.updateAnalytic(analytic)
    }

    override suspend fun deleteEvent(analyticId: String) {
        localManager.deleteEvent(analyticId)
    }

    companion object {
        private const val LIMIT = 50
    }
}
