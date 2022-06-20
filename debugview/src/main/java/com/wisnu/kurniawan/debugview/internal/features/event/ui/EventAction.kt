package com.wisnu.kurniawan.debugview.internal.features.event.ui

internal sealed interface EventAction {
    data class ClickEventItem(val id: String) : EventAction

    data class Launch(val tag: String) : EventAction

    object ToggleRecording : EventAction

    data class InputSearchEvent(val text: String) : EventAction

    object ClickClearAll : EventAction

    object ClickFilter : EventAction
    object ApplyFilter : EventAction
}
