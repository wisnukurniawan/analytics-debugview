package com.wisnu.kurniawan.debugview.internal.features.eventfilter.ui

import com.wisnu.kurniawan.debugview.internal.model.FilterType

internal sealed interface EventFilterAction {
    data class SeparateChanges(val selected: List<FilterType>) : EventFilterAction
    object ClickApply : EventFilterAction
    object ClickReset : EventFilterAction
    data class ClickPaste(val text: String) : EventFilterAction
    object Launch : EventFilterAction
}

