package com.wisnu.kurniawan.debugview.internal.features.analytic.data

import com.wisnu.kurniawan.debugview.internal.foundation.datastore.LocalManager
import com.wisnu.kurniawan.debugview.internal.model.Analytic
import com.wisnu.kurniawan.debugview.internal.model.FilterConfig
import kotlinx.coroutines.flow.Flow

internal class AnalyticEnvironment(
    private val localManager: LocalManager
) : IAnalyticEnvironment {
    override fun getAnalytics(): Flow<List<Analytic>> {
        return localManager.getAnalytics()
    }

    override fun getFilterConfig(): Flow<FilterConfig> {
        return localManager.getFilterConfig()
    }
}
