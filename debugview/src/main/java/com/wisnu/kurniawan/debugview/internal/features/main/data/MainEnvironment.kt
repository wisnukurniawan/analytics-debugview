package com.wisnu.kurniawan.debugview.internal.features.main.data

import com.wisnu.kurniawan.debugview.Event
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.LocalManager
import com.wisnu.kurniawan.debugview.internal.foundation.extension.getSearchType
import com.wisnu.kurniawan.debugview.internal.foundation.wrapper.IdProvider
import com.wisnu.kurniawan.debugview.internal.model.Analytic
import com.wisnu.kurniawan.debugview.internal.model.AnalyticWithEvent
import com.wisnu.kurniawan.debugview.internal.model.SearchType
import java.time.LocalDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import com.wisnu.kurniawan.debugview.internal.model.Event as InternalEvent

internal class MainEnvironment(
    private val localManager: LocalManager,
    private val idProvider: IdProvider,
) : IMainEnvironment {

    override fun getLast10EventWithAnalytic(): Flow<List<AnalyticWithEvent>> {
        val limit = 10
        return localManager.getFilterConfig()
            .map { it.getSearchType("") }
            .flatMapConcat {
                // TODO log why when app in BG not listen
                when (it) {
                    is SearchType.Filter -> localManager.getEventWithAnalytic(limit, it.texts)
                    else -> localManager.getEventWithAnalytic(limit)
                }
            }
            .map { analytics ->
                analytics.groupBy { it.analytic.tag }
                    .map { (_, value) ->
                        val events = value.map { it.event }
                        val analytic = value.map { it.analytic }.first()
                        AnalyticWithEvent(
                            analytic = analytic,
                            events = events
                        )
                    }
            }
    }

    override fun getAnalytic(event: Event): Flow<Analytic> {
        return localManager
            .getAnalytic(event.tag)
            .take(1)
            .filter { it.isRecording }
    }

    override suspend fun insertEvent(analytic: Analytic, event: Event, createdAt: LocalDateTime) {
        localManager.insertEvent(
            analytic.id,
            InternalEvent(
                id = idProvider.generate(),
                name = event.name,
                properties = event.properties,
                createdAt = createdAt
            )
        )
    }

}
