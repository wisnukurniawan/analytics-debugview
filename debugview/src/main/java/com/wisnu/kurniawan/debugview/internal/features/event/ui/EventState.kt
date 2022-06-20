package com.wisnu.kurniawan.debugview.internal.features.event.ui

import com.wisnu.kurniawan.debugview.internal.features.eventfilter.ui.FilterType
import com.wisnu.kurniawan.debugview.internal.foundation.extension.sanitizeQuery
import com.wisnu.kurniawan.debugview.internal.foundation.extension.split
import com.wisnu.kurniawan.debugview.internal.foundation.wrapper.DateTimeProviderImpl
import com.wisnu.kurniawan.debugview.internal.model.Analytic
import com.wisnu.kurniawan.debugview.internal.model.Event
import com.wisnu.kurniawan.debugview.internal.model.SearchType

internal data class EventState(
    val analytic: Analytic,
    val events: List<Event>,
    val filterConfig: FilterConfig,
    val searchText: String
) {

    fun getSearchType(): SearchType {
        return when {
            searchText.isNotBlank() && filterConfig.text.isNotBlank() -> {
                SearchType.QueryAndFilter(searchText.sanitizeQuery(), filterConfig.type.split(filterConfig.text))
            }
            searchText.isNotBlank() -> {
                SearchType.Query(searchText.sanitizeQuery())
            }
            filterConfig.text.isNotBlank() -> {
                SearchType.Filter(filterConfig.type.split(filterConfig.text))
            }
            else -> SearchType.Default
        }
    }

    companion object {
        val initial = EventState(
            Analytic(
                id = "",
                tag = "",
                isRecording = false,
                createdAt = DateTimeProviderImpl().now()
            ),
            listOf(),
            FilterConfig("", FilterType.NEW_LINE),
            ""
        )
    }
}

internal data class FilterConfig(
    val text: String,
    val type: FilterType
)
