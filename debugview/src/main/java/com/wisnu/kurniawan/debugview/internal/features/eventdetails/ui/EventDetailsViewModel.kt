package com.wisnu.kurniawan.debugview.internal.features.eventdetails.ui

import androidx.lifecycle.viewModelScope
import com.wisnu.foundation.coreviewmodel.StatefulViewModel
import com.wisnu.kurniawan.debugview.internal.features.eventdetails.data.IEventDetailsEnvironment
import com.wisnu.kurniawan.debugview.internal.foundation.extension.propertyDisplay
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
                viewModelScope.launch {
                    setEffect(EventDetailsEffect.Copy(getContentString()))
                }
            }
            EventDetailsAction.Share -> {
                viewModelScope.launch {
                    setEffect(EventDetailsEffect.Share(getContentString()))
                }
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

    private fun getContentString(): String {
        return state.value.event.name + "\n" + state.value.event.propertyDisplay()
    }
}
