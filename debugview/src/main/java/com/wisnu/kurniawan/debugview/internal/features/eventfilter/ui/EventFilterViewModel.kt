package com.wisnu.kurniawan.debugview.internal.features.eventfilter.ui

import androidx.lifecycle.viewModelScope
import com.wisnu.kurniawan.debugview.internal.foundation.extension.select
import com.wisnu.kurniawan.debugview.internal.foundation.viewmodel.StatefulViewModel
import kotlinx.coroutines.launch

internal class EventFilterViewModel : StatefulViewModel<EventFilterState, EventFilterEffect, EventFilterAction, Unit>(
    EventFilterState.initial,
    Unit
) {

    override fun dispatch(action: EventFilterAction) {
        when (action) {
            EventFilterAction.ClickApply -> {
                viewModelScope.launch {
                    setEffect(EventFilterEffect.Dismiss(state.value.text, state.value.filterItems.first { it.selected }.filterType))
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
                if (action.text != null && action.type != null) {
                    viewModelScope.launch {
                        setState { copy(text = action.text, filterItems = filterItems.select(action.type)) }
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
