package com.wisnu.kurniawan.debugview.internal.foundation.datastore.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = AnalyticDb::class,
            parentColumns = ["analytic_id"],
            childColumns = ["event_analyticId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("event_id"),
        Index("event_analyticId"),
    ]
)
internal data class EventDb(
    @PrimaryKey
    @ColumnInfo(name = "event_id")
    val id: String,
    @ColumnInfo(name = "event_analyticId")
    val analyticId: String,
    @ColumnInfo(name = "event_name")
    val name: String,
    @ColumnInfo(name = "event_properties")
    val properties: String,
    @ColumnInfo(name = "event_createdAt")
    val createdAt: LocalDateTime
)
