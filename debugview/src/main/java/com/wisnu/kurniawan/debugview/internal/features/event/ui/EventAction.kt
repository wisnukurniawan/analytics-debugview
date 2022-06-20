package com.wisnu.kurniawan.debugview.internal.features.event.ui

import com.wisnu.kurniawan.debugview.internal.features.eventfilter.ui.FilterType

internal sealed interface EventAction {
    data class ClickEventItem(val id: String) : EventAction

    data class Launch(val tag: String) : EventAction

    object ToggleRecording : EventAction

    data class InputSearchEvent(val text: String) : EventAction

    object ClickClearAll : EventAction

    object ClickFilter : EventAction
    data class ApplyFilter(val text: String, val filterType: FilterType) : EventAction
}
