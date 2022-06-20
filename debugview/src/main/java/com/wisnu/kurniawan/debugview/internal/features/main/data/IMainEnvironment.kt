package com.wisnu.kurniawan.debugview.internal.features.main.data

import com.wisnu.kurniawan.debugview.Event
import com.wisnu.kurniawan.debugview.internal.model.Analytic
import com.wisnu.kurniawan.debugview.internal.model.AnalyticWithEvent
import java.time.LocalDateTime
import kotlinx.coroutines.flow.Flow

internal interface IMainEnvironment {
    fun getLast10EventWithAnalytic(): Flow<List<AnalyticWithEvent>>
    fun getAnalytic(event: Event): Flow<Analytic>
    suspend fun insertEvent(analytic: Analytic, event: Event, createdAt: LocalDateTime)
    suspend fun insertAnalytics(tags: List<String>)
}
