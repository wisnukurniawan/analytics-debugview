package com.wisnu.kurniawan.debugview.internal.model

internal sealed interface SearchType {
    data class Query(val text: String) : SearchType
    data class Filter(val texts: List<String>) : SearchType
    data class QueryAndFilter(
        val text: String,
        val texts: List<String>
    ) : SearchType

    object Default : SearchType
}
