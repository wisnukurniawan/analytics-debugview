package com.wisnu.kurniawan.debugview.internal.features.eventdetails.ui

internal sealed interface EventDetailsAction {
    object Copy : EventDetailsAction
    object Share : EventDetailsAction
    data class Launch(val id: String) : EventDetailsAction
}
