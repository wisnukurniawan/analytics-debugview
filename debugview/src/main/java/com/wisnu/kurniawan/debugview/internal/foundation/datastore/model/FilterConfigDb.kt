package com.wisnu.kurniawan.debugview.internal.foundation.datastore.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wisnu.kurniawan.debugview.internal.model.FilterType

@Entity
internal data class FilterConfigDb(
    @PrimaryKey
    @ColumnInfo(name = "filter_config_id")
    val id: String = DEFAULT_ID,
    @ColumnInfo(name = "filter_config_text")
    val text: String = "",
    @ColumnInfo(name = "filter_config_type")
    val type: FilterType = FilterType.NEW_LINE
) {
    companion object {
        const val DEFAULT_ID = "DEFAULT_ID"
    }
}
