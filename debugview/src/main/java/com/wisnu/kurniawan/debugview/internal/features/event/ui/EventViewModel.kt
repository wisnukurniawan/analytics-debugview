package com.wisnu.kurniawan.debugview.internal.features.event.ui

import androidx.lifecycle.viewModelScope
import com.wisnu.foundation.coreviewmodel.StatefulViewModel
import com.wisnu.kurniawan.debugview.internal.features.event.data.IEventEnvironment
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

internal class EventViewModel(
    environment: IEventEnvironment
) : StatefulViewModel<EventState, EventEffect, EventAction, IEventEnvironment>(
    EventState.initial,
    environment
) {

    private var searchJob: Job? = null

    override fun dispatch(action: EventAction) {
        when (action) {
            is EventAction.ClickEventItem -> {
                viewModelScope.launch {
                    setEffect(EventEffect.NavigateToEventDetails(action.id))
                }
            }
            is EventAction.Launch -> {
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    environment.getAnalytic(action.tag)
                        .take(1)
                        .onEach { setState { copy(analytic = it) } }
                        .flatMapConcat { environment.searchEvent(it.id, state.value.searchText) }
                        .collect { events ->
                            setState { copy(events = events) }
                        }
                }
            }
            EventAction.ToggleRecording -> {
                viewModelScope.launch {
                    val toggle = !state.value.analytic.isRecording
                    setState { copy(analytic = analytic.copy(isRecording = toggle)) }
                    environment.updateAnalytic(state.value.analytic)
                }
            }
            is EventAction.InputSearchEvent -> {
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    setState { copy(searchText = action.text) }
                    environment.searchEvent(state.value.analytic.id, state.value.searchText)
                        .collect { events ->
                            setState { copy(events = events) }
                        }
                }
            }
            EventAction.ClickClearAll -> {
                viewModelScope.launch {
                    setEffect(EventEffect.Cleared(state.value.analytic))
                    environment.deleteEvent(state.value.analytic.id)
                }
            }
        }
    }

}
