package com.wisnu.kurniawan.debugview.internal.foundation.datastore

import com.wisnu.kurniawan.debugview.internal.foundation.extension.toAnalytic
import com.wisnu.kurniawan.debugview.internal.foundation.extension.toAnalyticDbs
import com.wisnu.kurniawan.debugview.internal.foundation.extension.toAnalytics
import com.wisnu.kurniawan.debugview.internal.foundation.extension.toEvent
import com.wisnu.kurniawan.debugview.internal.foundation.extension.toEventDb
import com.wisnu.kurniawan.debugview.internal.foundation.extension.toEvents
import com.wisnu.kurniawan.debugview.internal.foundation.wrapper.DateTimeProvider
import com.wisnu.kurniawan.debugview.internal.foundation.wrapper.IdProvider
import com.wisnu.kurniawan.debugview.model.Analytic
import com.wisnu.kurniawan.debugview.model.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.withContext

internal class LocalManager(
    private val dispatcher: CoroutineDispatcher,
    private val readDao: ReadDao,
    private val writeDao: WriteDao,
    private val idProvider: IdProvider,
    private val dateTimeProvider: DateTimeProvider
) {

    fun getAnalyticNameWithIds(): Flow<Map<String, String>> {
        return readDao.getAnalytics()
            .filterNotNull()
            .map { analyticDb ->
                analyticDb.associateBy({ it.name }, { it.id })
            }
            .flowOn(dispatcher)
    }

    fun getAnalytic(name: String): Flow<Analytic> {
        return readDao.getAnalytic(name)
            .map { it.toAnalytic() }
            .flowOn(dispatcher)
    }

    fun getEvents(analyticId: String): Flow<List<Event>> {
        return readDao.getEvents(analyticId)
            .filterNotNull()
            .map { it.toEvents() }
            .flowOn(dispatcher)
    }

    fun getEvent(id: String): Flow<Event> {
        return readDao.getEvent(id)
            .map { it.toEvent() }
            .flowOn(dispatcher)
    }

    fun searchEvent(query: String): Flow<List<Event>> {
        return readDao.searchEvent(query)
            .filterNotNull()
            .map { it.toEvents() }
            .flowOn(dispatcher)
    }

    suspend fun insertAnalytics(data: List<Analytic>) {
        withContext(dispatcher) {
            writeDao.insertAnalytics(
                data.toAnalyticDbs(
                    id = { idProvider.generate() },
                    createdAt = { dateTimeProvider.now() },
                    updatedAt = null
                )
            )
        }
    }

    suspend fun updateAnalytic(data: Analytic) {
        withContext(dispatcher) {
            writeDao.updateAnalytic(
                name = data.name,
                isRecording = data.isRecording,
                updatedAt = dateTimeProvider.now()
            )
        }
    }

    suspend fun insertEvent(analyticId: String, data: Event) {
        withContext(dispatcher) {
            writeDao.insertEvent(
                data.toEventDb(
                    analyticId = analyticId,
                    id = { idProvider.generate() },
                    createdAt = { dateTimeProvider.now() }
                )
            )
        }
    }

    suspend fun deleteEvent(analyticId: String) {
        withContext(dispatcher) {
            writeDao.deleteEvent(analyticId)
        }
    }

}
