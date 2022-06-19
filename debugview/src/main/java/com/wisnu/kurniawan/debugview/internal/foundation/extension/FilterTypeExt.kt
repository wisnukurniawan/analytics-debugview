package com.wisnu.kurniawan.debugview.internal.foundation.extension

import com.wisnu.kurniawan.debugview.R
import com.wisnu.kurniawan.debugview.internal.features.eventfilter.ui.FilterItem
import com.wisnu.kurniawan.debugview.internal.features.eventfilter.ui.FilterType

internal fun FilterType.split(text: String) = when (this) {
    FilterType.NEW_LINE -> text.split("\n")
    FilterType.COMMA -> text.split(",")
    FilterType.COLON -> text.split(";")
}

internal fun List<FilterItem>.select(filterType: List<FilterType>): List<FilterItem> {
    val newType = filterType.firstOrNull { it != selected() } ?: FilterType.NEW_LINE
    return select(newType)
}

internal fun List<FilterItem>.select(newType: FilterType): List<FilterItem> {
    return map {
        it.copy(selected = it.filterType == newType)
    }
}

internal fun List<FilterItem>.selected(): FilterType {
    return find { it.selected }?.filterType ?: FilterType.NEW_LINE
}

internal fun FilterType.stringResource() = when (this) {
    FilterType.NEW_LINE -> R.string.debugview_event_filter_type_newline
    FilterType.COMMA -> R.string.debugview_event_filter_type_comma
    FilterType.COLON -> R.string.debugview_event_filter_type_colon
}
