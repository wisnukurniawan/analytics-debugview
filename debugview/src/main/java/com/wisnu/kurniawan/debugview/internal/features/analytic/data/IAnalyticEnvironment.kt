package com.wisnu.kurniawan.debugview.internal.features.analytic.data

import com.wisnu.kurniawan.debugview.internal.model.Analytic
import com.wisnu.kurniawan.debugview.internal.model.FilterConfig
import kotlinx.coroutines.flow.Flow

internal interface IAnalyticEnvironment {
    fun getAnalytics(): Flow<List<Analytic>>
    fun getFilterConfig(): Flow<FilterConfig>
}
