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

    override fun dispatch(action: AnalyticAction) {
        when (action) {
            is AnalyticAction.ClickAnalyticItem -> {
                viewModelScope.launch {
                    setEffect(AnalyticEffect.NavigateToEvent(action.tag))
                }
            }
            is AnalyticAction.Launch -> {
                viewModelScope.launch {
                    environment.getAnalytics()
                        .collect {
                            setState { copy(analytics = it) }

                            val isSingle = it.size == 1
                            if (action.tag.isNotBlank()) {
                                setEffect(AnalyticEffect.NavigateToEvent(action.tag, isSingle))
                            } else if (isSingle) {
                                setEffect(AnalyticEffect.NavigateToEvent(it.first().tag, isSingle))
                            }
                        }
                }
            }
        }
    }
}
