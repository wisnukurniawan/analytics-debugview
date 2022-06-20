package com.wisnu.kurniawan.debugview.internal.features.event.data

import com.wisnu.kurniawan.debugview.internal.foundation.datastore.LocalManager
import com.wisnu.kurniawan.debugview.internal.foundation.extension.sanitizeQuery
import com.wisnu.kurniawan.debugview.internal.model.Analytic
import com.wisnu.kurniawan.debugview.internal.model.Event
import com.wisnu.kurniawan.debugview.internal.model.SearchType
import kotlinx.coroutines.flow.Flow

internal class EventEnvironment(private val localManager: LocalManager) : IEventEnvironment {
    override fun getAnalytic(tag: String): Flow<Analytic> {
        return localManager.getAnalytic(tag)
    }

    override fun searchEvent(analyticId: String, search: SearchType): Flow<List<Event>> {
        return when (search) {
            is SearchType.Filter -> {
                if (search.texts.isEmpty()) {
                    localManager.getEvents(analyticId, LIMIT)
                } else {
                    localManager.searchEvent(analyticId, LIMIT, search.texts)
                }
            }
            is SearchType.Query -> {
                if (search.text.isBlank()) {
                    localManager.getEvents(analyticId, LIMIT)
                } else {
                    localManager.searchEvent(analyticId, LIMIT, search.text.sanitizeQuery())
                }
            }
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
