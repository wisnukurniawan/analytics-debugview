package com.wisnu.kurniawan.debugview.internal.foundation.extension

import com.wisnu.kurniawan.debugview.internal.model.FilterConfig
import com.wisnu.kurniawan.debugview.internal.model.SearchType

internal fun FilterConfig.getSearchType(searchText: String): SearchType {
    return when {
        searchText.isNotBlank() && text.isNotBlank() -> {
            SearchType.QueryAndFilter(searchText.sanitizeQuery(), filterType.split(text))
        }
        searchText.isNotBlank() -> {
            SearchType.Query(searchText.sanitizeQuery())
        }
        text.isNotBlank() -> {
            SearchType.Filter(filterType.split(text))
        }
        else -> SearchType.Default
    }
}
