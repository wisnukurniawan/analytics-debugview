package com.wisnu.kurniawan.debugview.internal.features.analytic.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.wisnu.kurniawan.debugview.internal.features.analytic.data.AnalyticEnvironment
import com.wisnu.kurniawan.debugview.internal.features.analytic.data.IAnalyticEnvironment
import com.wisnu.kurniawan.debugview.internal.features.analytic.ui.AnalyticFragment
import com.wisnu.kurniawan.debugview.internal.features.analytic.ui.AnalyticViewModel
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.LocalManager

internal object AnalyticModule {

    fun inject(fragment: AnalyticFragment, localManager: LocalManager) {
        fragment.environment = AnalyticEnvironment(
            localManager = localManager
        )
    }

    fun inject(fragment: AnalyticFragment, owner: ViewModelStoreOwner, environment: IAnalyticEnvironment) {
        val factory = AnalyticViewModelFactory(environment)
        fragment.viewModel = ViewModelProvider(owner, factory)[AnalyticViewModel::class.java]
    }

}

internal class AnalyticViewModelFactory(private val environment: IAnalyticEnvironment) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnalyticViewModel::class.java)) {
            return AnalyticViewModel(environment) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
