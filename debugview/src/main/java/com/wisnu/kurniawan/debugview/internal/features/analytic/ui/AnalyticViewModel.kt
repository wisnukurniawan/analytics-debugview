package com.wisnu.kurniawan.debugview.internal.features.analytic.ui

import androidx.lifecycle.viewModelScope
import com.wisnu.kurniawan.debugview.internal.features.analytic.data.IAnalyticEnvironment
import com.wisnu.kurniawan.debugview.internal.foundation.viewmodel.StatefulViewModel
import kotlinx.coroutines.launch

internal class AnalyticViewModel(
    environment: IAnalyticEnvironment
) : StatefulViewModel<AnalyticState, AnalyticEffect, AnalyticAction, IAnalyticEnvironment>(
    AnalyticState(),
    environment
) {

    init {
        viewModelScope.launch {
            environment.getAnalytics()
                .collect {
                    if (it.size == 1) {
                        setEffect(AnalyticEffect.NavigateToEvent(it.first().tag))
                    } else {
                        setState { copy(analytics = it) }
                    }
                }
        }
    }

    override fun dispatch(action: AnalyticAction) {
        when (action) {
            is AnalyticAction.ClickAnalyticItem -> {
                viewModelScope.launch {
                    setEffect(AnalyticEffect.NavigateToEvent(action.tag))
                }
            }
        }
    }
}
