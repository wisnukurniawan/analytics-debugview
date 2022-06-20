package com.wisnu.kurniawan.debugview.internal.features.event.data

import com.wisnu.kurniawan.debugview.internal.model.Analytic
import com.wisnu.kurniawan.debugview.internal.model.Event
import com.wisnu.kurniawan.debugview.internal.model.SearchType
import kotlinx.coroutines.flow.Flow

internal interface IEventEnvironment {
    fun getAnalytic(tag: String): Flow<Analytic>
    fun searchEvent(analyticId: String, search: SearchType = SearchType.Default): Flow<List<Event>>
    suspend fun updateAnalytic(analytic: Analytic)
    suspend fun deleteEvent(analyticId: String)
}
