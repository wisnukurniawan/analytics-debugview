package com.wisnu.kurniawan.debugview.internal.foundation.datastore

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.AnalyticDb
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.EventDb
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.EventWithAnalytic
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ReadDao {

    @Query("SELECT * FROM AnalyticDb")
    fun getAnalytics(): Flow<List<AnalyticDb>>

    @Query("SELECT * FROM AnalyticDb WHERE analytic_tag = :tag")
    fun getAnalytic(tag: String): Flow<AnalyticDb>

    @Transaction
    @Query(
        """
            SELECT *
            FROM EventDb 
            LEFT JOIN AnalyticDb ON event_analyticId = AnalyticDb.analytic_id
            ORDER BY event_createdAt DESC
            LIMIT :limit
            """
    )
    fun getEventWithAnalytic(limit: Int): Flow<List<EventWithAnalytic>>

    @Query("SELECT * FROM EventDb WHERE event_analyticId = :analyticId ORDER BY event_createdAt DESC LIMIT :limit")
    fun getEvents(analyticId: String, limit: Int): Flow<List<EventDb>>

    @Query("SELECT * FROM EventDb WHERE event_id = :id")
    fun getEvent(id: String): Flow<EventDb>

    @Transaction
    @Query(
        """
            SELECT *
            FROM EventDb 
            JOIN EventFtsDb ON EventDb.event_name = EventFtsDb.event_name
            WHERE event_analyticId = :analyticId AND EventFtsDb MATCH :query
            ORDER BY event_createdAt DESC
            LIMIT :limit
            """
    )
    fun searchEvent(analyticId: String, limit: Int, query: String): Flow<List<EventDb>>

    @Transaction
    @Query(
        """
            SELECT *
            FROM EventDb 
            WHERE event_analyticId = :analyticId
            AND event_name IN (:filters)
            ORDER BY event_createdAt DESC
            LIMIT :limit
            """
    )
    fun searchEvent(analyticId: String, limit: Int, filters: List<String>): Flow<List<EventDb>>

}
