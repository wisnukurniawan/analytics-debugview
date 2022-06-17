package com.wisnu.kurniawan.debugview.internal.foundation.datastore.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    indices = [
        Index("analytic_tag", unique = true)
    ]
)
internal data class AnalyticDb(
    @PrimaryKey
    @ColumnInfo(name = "analytic_id")
    val id: String,
    @ColumnInfo(name = "analytic_tag")
    val tag: String,
    @ColumnInfo(name = "analytic_isRecording")
    val isRecording: Boolean,
    @ColumnInfo(name = "analytic_createdAt")
    val createdAt: LocalDateTime,
    @ColumnInfo(name = "analytic_updatedAt")
    val updatedAt: LocalDateTime? = null,
)
