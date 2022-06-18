package com.wisnu.kurniawan.debugview.internal.features.event.ui

internal sealed interface EventAction {
    data class ClickEventItem(val id: String) : EventAction
    data class Launch(val tag: String) : EventAction
    object ToggleRecording : EventAction
    data class SearchEvent(val text: String) : EventAction
    object DeleteAll : EventAction
}
