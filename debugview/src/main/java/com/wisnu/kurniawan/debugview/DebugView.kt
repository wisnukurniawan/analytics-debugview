package com.wisnu.kurniawan.debugview

import android.app.Application
import android.content.Context
import android.os.Bundle
import com.wisnu.kurniawan.debugview.internal.features.main.di.MainModule
import com.wisnu.kurniawan.debugview.internal.features.notification.di.EventNotificationModule
import com.wisnu.kurniawan.debugview.internal.foundation.di.DataModule
import com.wisnu.kurniawan.debugview.internal.foundation.extension.require
import com.wisnu.kurniawan.debugview.internal.foundation.lifecycle.registerVisibilityListener
import com.wisnu.kurniawan.debugview.internal.foundation.wrapper.DateTimeProviderImpl
import com.wisnu.kurniawan.debugview.internal.foundation.wrapper.IdProviderImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


object DebugView {

    private val idProvider by lazy { IdProviderImpl() }
    private val dateTimeProvider by lazy { DateTimeProviderImpl() }
    private var listenAnalytics: Job? = null

    internal fun init(context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            DataModule.inject(context)
            DataModule.inject(DataModule.db, dateTimeProvider)
            MainModule.inject(DataModule.localManager, idProvider)
            EventNotificationModule.inject(context)

            (context.applicationContext as Application).registerVisibilityListener {
                if (it) {
                    listenAnalytics?.cancel()
                    listenAnalytics = GlobalScope.launch(Dispatchers.IO) {
                        initListenAnalyticChanges()
                    }
                } else {
                    listenAnalytics?.cancel()
                }
            }
        }
    }

    fun register(vararg tags: String) {
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

    fun record(tag: String, eventName: String, bundle: Bundle) {
        val properties = bundle.keySet().associateWith { bundle.getString(it).orEmpty() }

        record(
            Event(
                tag = tag,
                name = eventName,
                properties = properties
            )
        )
    }

    fun record(tag: String, eventName: String, properties: Map<String, Any>) {
        record(
            Event(
                tag = tag,
                name = eventName,
                properties = properties.mapValues { it.value.toString() }
            )
        )
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
