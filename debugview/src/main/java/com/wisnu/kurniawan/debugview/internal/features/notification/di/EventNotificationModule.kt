package com.wisnu.kurniawan.debugview.internal.features.notification.di

import android.annotation.SuppressLint
import android.content.Context
import com.wisnu.kurniawan.debugview.internal.features.notification.data.EventNotificationManager

internal object EventNotificationModule {

    @SuppressLint("StaticFieldLeak")
    lateinit var eventNotificationManager: EventNotificationManager

    fun inject(context: Context) {
        eventNotificationManager = EventNotificationManager(context)
    }

}
