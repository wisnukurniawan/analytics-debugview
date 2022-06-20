package com.wisnu.kurniawan.debugview.internal.features.event.data

import com.wisnu.kurniawan.debugview.internal.foundation.datastore.LocalManager
import com.wisnu.kurniawan.debugview.internal.foundation.extension.getSearchType
import com.wisnu.kurniawan.debugview.internal.model.Analytic
import com.wisnu.kurniawan.debugview.internal.model.Event
import com.wisnu.kurniawan.debugview.internal.model.SearchType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map

internal class EventEnvironment(private val localManager: LocalManager) : IEventEnvironment {
    override fun getAnalytic(tag: String): Flow<Analytic> {
        return localManager.getAnalytic(tag)
    }

    override fun searchEvent(analyticId: String, searchText: String): Flow<Pair<Boolean, List<Event>>> {
        return localManager.getFilterConfig()
            .map { it.getSearchType(searchText) }
            .flatMapConcat { searchType ->
                when (searchType) {
                    SearchType.Default -> {
                        localManager.getEvents(analyticId, LIMIT).map {
                            Pair(false, it)
                        }
                    }
                    is SearchType.Filter -> {
                        localManager.searchEvent(analyticId, LIMIT, searchType.texts).map {
                            Pair(true, it)
                        }
                    }
                    is SearchType.Query -> {
                        localManager.searchEvent(analyticId, LIMIT, searchType.text).map {
                            Pair(false, it)
                        }
                    }
                    is SearchType.QueryAndFilter -> {
                        localManager.searchEvent(analyticId, LIMIT, searchType.text, searchType.texts).map {
                            Pair(true, it)
                        }
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
