package com.wisnu.kurniawan.debugview.internal.features.eventfilter.ui

import androidx.lifecycle.viewModelScope
import com.wisnu.kurniawan.debugview.internal.features.eventfilter.data.IEventFilterEnvironment
import com.wisnu.kurniawan.debugview.internal.foundation.extension.select
import com.wisnu.kurniawan.debugview.internal.foundation.viewmodel.StatefulViewModel
import com.wisnu.kurniawan.debugview.internal.model.FilterConfig
import kotlinx.coroutines.launch

internal class EventFilterViewModel(
    environment: IEventFilterEnvironment
) : StatefulViewModel<EventFilterState, EventFilterEffect, EventFilterAction, IEventFilterEnvironment>(
    EventFilterState.initial,
    environment
) {

    override fun dispatch(action: EventFilterAction) {
        when (action) {
            EventFilterAction.ClickApply -> {
                viewModelScope.launch {
                    environment.insertFilterConfig(FilterConfig(state.value.text, state.value.filterItems.first { it.selected }.filterType))
                    setEffect(EventFilterEffect.Dismiss)
                }
            }
            is EventFilterAction.ClickPaste -> {
                viewModelScope.launch {
                    setState { copy(text = action.text.trim()) }
                }
            }
            is EventFilterAction.SeparateChanges -> {
                viewModelScope.launch {
                    setState { copy(text = "", filterItems = filterItems.select(action.selected)) }
                }
            }
            is EventFilterAction.Launch -> {
                viewModelScope.launch {
                    environment.getFilterConfig()
                        .collect {
                            setState { copy(text = it.text, filterItems = filterItems.select(it.filterType)) }
                        }
                }
            }
            EventFilterAction.ClickReset -> {
                viewModelScope.launch {
                    setState { copy(text = "") }
                }
            }
        }
    }
}
