package com.wisnu.kurniawan.debugview.internal.features.eventfilter.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.wisnu.kurniawan.debugview.internal.features.eventfilter.ui.EventFilterFragment
import com.wisnu.kurniawan.debugview.internal.features.eventfilter.ui.EventFilterViewModel

internal object EventFilterModule {

    fun inject(fragment: EventFilterFragment, owner: ViewModelStoreOwner) {
        val factory = EventFilterViewModelFactory()
        fragment.viewModel = ViewModelProvider(owner, factory)[EventFilterViewModel::class.java]
    }

}

internal class EventFilterViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventFilterViewModel::class.java)) {
            return EventFilterViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
