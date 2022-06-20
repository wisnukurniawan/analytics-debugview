package com.wisnu.kurniawan.debugview.internal.features.eventfilter.data

import com.wisnu.kurniawan.debugview.internal.model.FilterConfig
import kotlinx.coroutines.flow.Flow

internal interface IEventFilterEnvironment {
    fun getFilterConfig(): Flow<FilterConfig>
    suspend fun insertFilterConfig(config: FilterConfig)
}
