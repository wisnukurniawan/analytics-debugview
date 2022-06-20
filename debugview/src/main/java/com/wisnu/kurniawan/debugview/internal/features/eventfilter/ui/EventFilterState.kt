package com.wisnu.kurniawan.debugview.internal.features.eventfilter.ui

import com.wisnu.kurniawan.debugview.internal.foundation.extension.selected
import com.wisnu.kurniawan.debugview.internal.foundation.extension.split
import com.wisnu.kurniawan.debugview.internal.model.FilterType

internal data class EventFilterState(
    val text: String,
    val filterItems: List<FilterItem>
) {

    val textDisplay = filterItems.selected().split(text).joinToString(separator = "") {
        "$it\n"
    }

    companion object {
        val initial = EventFilterState(
            text = "",
            filterItems = listOf(
                FilterItem(
                    selected = true,
                    filterType = FilterType.NEW_LINE
                ),
                FilterItem(
                    selected = false,
                    filterType = FilterType.COMMA
                ),
                FilterItem(
                    selected = false,
                    filterType = FilterType.COLON
                )
            )
        )
    }
}

internal data class FilterItem(
    val selected: Boolean,
    val filterType: FilterType
)

