package com.wisnu.kurniawan.debugview.internal.features.eventdetails.ui

import androidx.lifecycle.viewModelScope
import com.wisnu.kurniawan.debugview.internal.features.eventdetails.data.IEventDetailsEnvironment
import com.wisnu.kurniawan.debugview.internal.foundation.viewmodel.StatefulViewModel
import kotlinx.coroutines.launch

internal class EventDetailsViewModel(
    environment: IEventDetailsEnvironment
) : StatefulViewModel<EventDetailsState, EventDetailsEffect, EventDetailsAction, IEventDetailsEnvironment>(
    EventDetailsState.initial,
    environment
) {


    override fun dispatch(action: EventDetailsAction) {
        when (action) {
            EventDetailsAction.Copy -> {

            }
            EventDetailsAction.Share -> {

            }
            is EventDetailsAction.Launch -> {
                viewModelScope.launch {
                    environment.getEvent(action.id)
                        .collect {
                            setState { copy(event = it) }
                        }
                }
            }
        }
    }
}
