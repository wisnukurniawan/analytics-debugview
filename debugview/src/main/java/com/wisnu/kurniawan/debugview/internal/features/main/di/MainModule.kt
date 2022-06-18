package com.wisnu.kurniawan.debugview.internal.features.main.di

import com.wisnu.kurniawan.debugview.internal.features.main.data.IMainEnvironment
import com.wisnu.kurniawan.debugview.internal.features.main.data.MainEnvironment
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.LocalManager
import com.wisnu.kurniawan.debugview.internal.foundation.wrapper.IdProvider

internal object MainModule {

    var mainEnvironment: IMainEnvironment? = null

    fun inject(
        localManager: LocalManager,
        idProvider: IdProvider
    ) {
        mainEnvironment = MainEnvironment(
            localManager,
            idProvider
        )
    }

}
