package com.wisnu.kurniawan.debugview.internal.features.eventfilter.ui

internal sealed interface EventFilterAction {
    data class SeparateChanges(val selected: List<FilterType>) : EventFilterAction
    object ClickApply : EventFilterAction
    object ClickReset : EventFilterAction
    data class ClickPaste(val text: String) : EventFilterAction
    data class Launch(val text: String?, val type: FilterType?) : EventFilterAction
}

