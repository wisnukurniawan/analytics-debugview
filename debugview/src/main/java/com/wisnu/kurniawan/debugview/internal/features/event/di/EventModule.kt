package com.wisnu.kurniawan.debugview.internal.features.event.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.wisnu.kurniawan.debugview.internal.features.event.data.EventEnvironment
import com.wisnu.kurniawan.debugview.internal.features.event.data.IEventEnvironment
import com.wisnu.kurniawan.debugview.internal.features.event.ui.EventFragment
import com.wisnu.kurniawan.debugview.internal.features.event.ui.EventViewModel
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.LocalManager

internal object EventModule {

    fun inject(fragment: EventFragment, localManager: LocalManager) {
        fragment.environment = EventEnvironment(
            localManager = localManager
        )
    }

    fun inject(fragment: EventFragment, owner: ViewModelStoreOwner, environment: IEventEnvironment) {
        val factory = EventViewModelFactory(environment)
        fragment.viewModel = ViewModelProvider(owner, factory)[EventViewModel::class.java]
    }

}

internal class EventViewModelFactory(private val environment: IEventEnvironment) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            return EventViewModel(environment) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
