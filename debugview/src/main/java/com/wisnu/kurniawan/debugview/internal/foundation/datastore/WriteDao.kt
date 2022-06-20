package com.wisnu.kurniawan.debugview.internal.foundation.datastore

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.AnalyticDb
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.EventDb
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.FilterConfigDb
import java.time.LocalDateTime

@Dao
internal abstract class WriteDao {

    @Insert
    abstract suspend fun insertAnalytics(data: List<AnalyticDb>)

    @Query("UPDATE AnalyticDb SET analytic_isRecording = :isRecording, analytic_updatedAt = :updatedAt WHERE analytic_tag = :tag")
    abstract suspend fun updateAnalytic(tag: String, isRecording: Boolean, updatedAt: LocalDateTime)

    @Insert
    abstract suspend fun insertEvent(data: EventDb)

    @Query("DELETE FROM EventDb WHERE event_analyticId = :analyticId")
    abstract suspend fun deleteEvent(analyticId: String)

    @Update
    abstract suspend fun insertFilterConfig(data: FilterConfigDb)

}
