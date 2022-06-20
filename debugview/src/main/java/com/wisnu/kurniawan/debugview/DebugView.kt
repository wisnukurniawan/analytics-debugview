package com.wisnu.kurniawan.debugview

import android.content.Context
import com.wisnu.kurniawan.debugview.internal.features.main.di.MainModule
import com.wisnu.kurniawan.debugview.internal.features.notification.di.EventNotificationModule
import com.wisnu.kurniawan.debugview.internal.foundation.di.DataModule
import com.wisnu.kurniawan.debugview.internal.foundation.extension.require
import com.wisnu.kurniawan.debugview.internal.foundation.wrapper.DateTimeProviderImpl
import com.wisnu.kurniawan.debugview.internal.foundation.wrapper.IdProviderImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


object DebugView {

    private val idProvider by lazy { IdProviderImpl() }
    private val dateTimeProvider by lazy { DateTimeProviderImpl() }

    internal fun init(context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            DataModule.inject(context)
            DataModule.inject(DataModule.db, dateTimeProvider)
            MainModule.inject(DataModule.localManager, idProvider)
            EventNotificationModule.inject(context)

            initListenAnalyticChanges()
        }
    }

    fun init(vararg tags: String) {
        require(tags.toList())

        GlobalScope.launch(Dispatchers.IO) {
            MainModule.mainEnvironment?.insertAnalytics(tags.toList())
        }
    }

    private suspend fun initListenAnalyticChanges() {
        MainModule.mainEnvironment?.getLast10EventWithAnalytic()
            ?.catch { }
            ?.collect { analytics ->
                analytics.forEach {
                    EventNotificationModule.eventNotificationManager.show(
                        it.analytic,
                        it.events
                    )
                }
            }
    }

    fun record(event: Event) {
        val createdAt = dateTimeProvider.now()
        GlobalScope.launch(Dispatchers.IO) {
            MainModule.mainEnvironment?.getAnalytic(event)
                ?.catch { }
                ?.collect {
                    MainModule.mainEnvironment?.insertEvent(it, event, createdAt)
                }
        }
    }
}
