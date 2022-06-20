package com.wisnu.kurniawan.debugview.internal.features.eventfilter.data

import com.wisnu.kurniawan.debugview.internal.foundation.datastore.LocalManager
import com.wisnu.kurniawan.debugview.internal.model.FilterConfig
import kotlinx.coroutines.flow.Flow

internal class EventFilterEnvironment(
    private val localManager: LocalManager
) : IEventFilterEnvironment {
    override fun getFilterConfig(): Flow<FilterConfig> {
        return localManager.getFilterConfig()
    }

    override suspend fun updateFilterConfig(config: FilterConfig) {
        localManager.updateFilterConfig(config)
    }
}
