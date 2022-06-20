package com.wisnu.kurniawan.debugview.internal.features.event.data

import com.wisnu.kurniawan.debugview.internal.model.Analytic
import com.wisnu.kurniawan.debugview.internal.model.Event
import kotlinx.coroutines.flow.Flow

internal interface IEventEnvironment {
    fun getAnalytic(tag: String): Flow<Analytic>
    fun searchEvent(analyticId: String, searchText: String): Flow<Pair<Boolean, List<Event>>>
    suspend fun updateAnalytic(analytic: Analytic)
    suspend fun deleteEvent(analyticId: String)
}
