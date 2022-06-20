package com.wisnu.kurniawan.debugview.internal.features.eventfilter.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.wisnu.kurniawan.debugview.internal.features.eventfilter.data.EventFilterEnvironment
import com.wisnu.kurniawan.debugview.internal.features.eventfilter.data.IEventFilterEnvironment
import com.wisnu.kurniawan.debugview.internal.features.eventfilter.ui.EventFilterFragment
import com.wisnu.kurniawan.debugview.internal.features.eventfilter.ui.EventFilterViewModel
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.LocalManager

internal object EventFilterModule {
    fun inject(fragment: EventFilterFragment, localManager: LocalManager) {
        fragment.environment = EventFilterEnvironment(
            localManager = localManager
        )
    }

    fun inject(fragment: EventFilterFragment, owner: ViewModelStoreOwner, eventFilterEnvironment: IEventFilterEnvironment) {
        val factory = EventFilterViewModelFactory(eventFilterEnvironment)
        fragment.viewModel = ViewModelProvider(owner, factory)[EventFilterViewModel::class.java]
    }

}

internal class EventFilterViewModelFactory(private val eventFilterEnvironment: IEventFilterEnvironment) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventFilterViewModel::class.java)) {
            return EventFilterViewModel(eventFilterEnvironment) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
