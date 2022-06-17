package com.wisnu.kurniawan.debugview.internal.foundation.datastore.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4

@Fts4(contentEntity = EventDb::class)
@Entity
data class EventFtsDb(
    @ColumnInfo(name = "event_name")
    val name: String,
    @ColumnInfo(name = "event_properties")
    val properties: String,
)
