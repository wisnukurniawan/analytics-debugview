package com.wisnu.kurniawan.debugview.internal.foundation.datastore

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.AnalyticDb
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.EventDb
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ReadDao {

    @Query("SELECT * FROM AnalyticDb")
    fun getAnalytics(): Flow<List<AnalyticDb>>

    @Query("SELECT * FROM AnalyticDb WHERE analytic_name = :name")
    fun getAnalytic(name: String): Flow<AnalyticDb>

    @Query("SELECT * FROM EventDb WHERE event_analyticId = :analyticId LIMIT 50")
    fun getEvents(analyticId: String): Flow<List<EventDb>>

    @Query("SELECT * FROM EventDb WHERE event_id = :id")
    fun getEvent(id: String): Flow<EventDb>

    @Transaction
    @Query(
        """
            SELECT *
            FROM EventDb 
            JOIN EventFtsDb ON EventDb.event_name = EventFtsDb.event_name
            WHERE EventFtsDb MATCH :query
            """
    )
    fun searchEvent(query: String): Flow<List<EventDb>>

}
