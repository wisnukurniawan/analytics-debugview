package com.wisnu.kurniawan.debugview.internal.features.eventdetails.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.wisnu.kurniawan.debugview.internal.features.eventdetails.data.EventDetailsEnvironment
import com.wisnu.kurniawan.debugview.internal.features.eventdetails.data.IEventDetailsEnvironment
import com.wisnu.kurniawan.debugview.internal.features.eventdetails.ui.EventDetailsFragment
import com.wisnu.kurniawan.debugview.internal.features.eventdetails.ui.EventDetailsViewModel
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.LocalManager

internal object EventDetailModule {
    fun inject(fragment: EventDetailsFragment, localManager: LocalManager) {
        fragment.environment = EventDetailsEnvironment(
            localManager = localManager
        )
    }

    fun inject(fragment: EventDetailsFragment, owner: ViewModelStoreOwner, environment: IEventDetailsEnvironment) {
        val factory = EventDetailsViewModelFactory(environment)
        fragment.viewModel = ViewModelProvider(owner, factory)[EventDetailsViewModel::class.java]
    }

}

internal class EventDetailsViewModelFactory(private val environment: IEventDetailsEnvironment) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventDetailsViewModel::class.java)) {
            return EventDetailsViewModel(environment) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
