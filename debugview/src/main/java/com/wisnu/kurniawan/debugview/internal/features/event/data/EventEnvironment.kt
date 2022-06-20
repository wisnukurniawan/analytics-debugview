package com.wisnu.kurniawan.debugview.internal.features.event.data

import com.wisnu.kurniawan.debugview.internal.foundation.datastore.LocalManager
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
            SearchType.Default -> localManager.getEvents(analyticId, LIMIT)
            is SearchType.Filter -> localManager.searchEvent(analyticId, LIMIT, search.texts)
            is SearchType.Query -> localManager.searchEvent(analyticId, LIMIT, search.text)
            is SearchType.QueryAndFilter -> localManager.searchEvent(analyticId, LIMIT, search.text, search.texts)
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
