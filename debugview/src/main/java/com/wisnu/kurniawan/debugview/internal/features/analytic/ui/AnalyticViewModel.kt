package com.wisnu.kurniawan.debugview.internal.features.analytic.ui

import androidx.lifecycle.viewModelScope
import com.wisnu.foundation.coreviewmodel.StatefulViewModel
import com.wisnu.kurniawan.debugview.internal.features.analytic.data.IAnalyticEnvironment
import kotlinx.coroutines.launch

internal class AnalyticViewModel(
    environment: IAnalyticEnvironment
) : StatefulViewModel<AnalyticState, AnalyticEffect, AnalyticAction, IAnalyticEnvironment>(
    AnalyticState(),
    environment
) {

    override fun dispatch(action: AnalyticAction) {
        when (action) {
            is AnalyticAction.ClickAnalyticItem -> {
                viewModelScope.launch {
                    setEffect(AnalyticEffect.NavigateToEvent(action.tag))
                }
            }
            is AnalyticAction.Launch -> {
                viewModelScope.launch {
                    if (action.tag.isNotBlank()) {
                        setEffect(AnalyticEffect.NavigateToEvent(action.tag))
                    }
                }

                viewModelScope.launch {
                    environment.getAnalytics()
                        .collect {
                            setState { copy(analytics = it) }
                        }
                }

                viewModelScope.launch {
                    environment.getFilterConfig()
                        .collect {
                            setState { copy(isFilterApplied = it.text.isNotBlank()) }
                        }
                }
            }
            AnalyticAction.ClickFilter -> {
                viewModelScope.launch {
                    setEffect(AnalyticEffect.ShowFilterSheet)
                }
            }
        }
    }
}
