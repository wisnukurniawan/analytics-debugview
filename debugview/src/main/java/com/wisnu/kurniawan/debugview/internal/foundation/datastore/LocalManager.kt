package com.wisnu.kurniawan.debugview.internal.foundation.datastore

import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.FilterConfigDb
import com.wisnu.kurniawan.debugview.internal.foundation.extension.toAnalytic
import com.wisnu.kurniawan.debugview.internal.foundation.extension.toAnalyticDb
import com.wisnu.kurniawan.debugview.internal.foundation.extension.toEvent
import com.wisnu.kurniawan.debugview.internal.foundation.extension.toEventDb
import com.wisnu.kurniawan.debugview.internal.foundation.extension.toEvents
import com.wisnu.kurniawan.debugview.internal.foundation.wrapper.DateTimeProvider
import com.wisnu.kurniawan.debugview.internal.model.Analytic
import com.wisnu.kurniawan.debugview.internal.model.Event
import com.wisnu.kurniawan.debugview.internal.model.EventWithAnalytic
import com.wisnu.kurniawan.debugview.internal.model.FilterConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class LocalManager(
    private val dispatcher: CoroutineDispatcher,
    private val readDao: ReadDao,
    private val writeDao: WriteDao,
    private val dateTimeProvider: DateTimeProvider
) {

    fun getAnalytics(): Flow<List<Analytic>> {
        return readDao.getAnalytics()
            .filterNotNull()
            .map { analytics -> analytics.map { it.toAnalytic() } }
            .flowOn(dispatcher)
    }

    fun getAnalytic(tag: String): Flow<Analytic> {
        return readDao.getAnalytic(tag)
            .map { it.toAnalytic() }
            .flowOn(dispatcher)
    }

    fun getEventWithAnalytic(limit: Int): Flow<List<EventWithAnalytic>> {
        return readDao.getEventWithAnalytic(limit)
            .filterNotNull()
            .map { analytics ->
                analytics.map {
                    EventWithAnalytic(
                        analytic = it.analytic.toAnalytic(),
                        event = it.event.toEvent()
                    )
                }
            }
            .flowOn(dispatcher)
    }

    fun getEventWithAnalytic(limit: Int, filters: List<String>): Flow<List<EventWithAnalytic>> {
        return readDao.getEventWithAnalytic(limit, filters)
            .filterNotNull()
            .map { analytics ->
                analytics.map {
                    EventWithAnalytic(
                        analytic = it.analytic.toAnalytic(),
                        event = it.event.toEvent()
                    )
                }
            }
            .flowOn(dispatcher)
    }

    fun getEvent(id: String): Flow<Event> {
        return readDao.getEvent(id)
            .map { it.toEvent() }
            .flowOn(dispatcher)
    }

    fun getEvents(analyticId: String, limit: Int): Flow<List<Event>> {
        return readDao.getEvents(analyticId, limit)
            .filterNotNull()
            .map { it.toEvents() }
            .flowOn(dispatcher)
    }

    fun searchEvent(analyticId: String, limit: Int, query: String): Flow<List<Event>> {
        return readDao.searchEvent(analyticId, limit, query)
            .filterNotNull()
            .map { it.toEvents() }
            .flowOn(dispatcher)
    }

    fun searchEvent(analyticId: String, limit: Int, filters: List<String>): Flow<List<Event>> {
        return readDao.searchEvent(analyticId, limit, filters)
            .filterNotNull()
            .map { it.toEvents() }
            .flowOn(dispatcher)
    }

    fun searchEvent(analyticId: String, limit: Int, query: String, filters: List<String>): Flow<List<Event>> {
        return readDao.searchEvent(analyticId, limit, query, filters)
            .filterNotNull()
            .map { it.toEvents() }
            .flowOn(dispatcher)
    }

    fun getFilterConfig(): Flow<FilterConfig> {
        return readDao.getFilterConfig(FilterConfigDb.DEFAULT_ID)
            .map { FilterConfig(it.text, it.type) }
            .flowOn(dispatcher)
    }

    suspend fun insertAnalytics(data: List<Analytic>) {
        withContext(dispatcher) {
            writeDao.insertAnalytics(data.toAnalyticDb())
        }
    }

    suspend fun updateAnalytic(data: Analytic) {
        withContext(dispatcher) {
            writeDao.updateAnalytic(
                tag = data.tag,
                isRecording = data.isRecording,
                updatedAt = dateTimeProvider.now()
            )
        }
    }

    suspend fun insertEvent(analyticId: String, data: Event) {
        withContext(dispatcher) {
            writeDao.insertEvent(
                data.toEventDb(analyticId = analyticId)
            )
        }
    }

    suspend fun deleteEvent(analyticId: String) {
        withContext(dispatcher) {
            writeDao.deleteEvent(analyticId)
        }
    }

    suspend fun updateFilterConfig(config: FilterConfig) {
        withContext(dispatcher) {
            writeDao.updateFilterConfig(FilterConfigDb(FilterConfigDb.DEFAULT_ID, config.text, config.filterType))
        }
    }

}
