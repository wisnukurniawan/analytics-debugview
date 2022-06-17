package com.wisnu.kurniawan.debugview

import android.annotation.SuppressLint
import android.content.Context
import com.wisnu.kurniawan.debugview.internal.features.main.data.IMainEnvironment
import com.wisnu.kurniawan.debugview.internal.features.main.data.MainEnvironment
import com.wisnu.kurniawan.debugview.internal.features.notification.data.EventNotificationManager
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.DebugViewDatabase
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.LocalManager
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

    private lateinit var db: DebugViewDatabase
    private lateinit var localManager: LocalManager
    private var mainEnvironment: IMainEnvironment? = null

    @SuppressLint("StaticFieldLeak")
    internal var eventNotificationManager: EventNotificationManager? = null

    fun init(context: Context, tags: List<String>) {
        require(tags)

        GlobalScope.launch(Dispatchers.IO) {
            initDatabase(context, tags)
            initLocalManager()
            initMainEnvironment()

            launch {
                initListenAnalyticChanges(context)
            }
        }
    }

    private fun initDatabase(context: Context, tags: List<String>) {
        db = DebugViewDatabase.getInstance(context, tags)
    }

    private fun initLocalManager() {
        localManager = LocalManager(
            dispatcher = Dispatchers.IO,
            readDao = db.readDao(),
            writeDao = db.writeDao(),
            dateTimeProvider = dateTimeProvider
        )
    }

    private fun initMainEnvironment() {
        mainEnvironment = MainEnvironment(
            localManager = localManager,
            idProvider = idProvider,
        )
    }

    private suspend fun initListenAnalyticChanges(context: Context) {
        eventNotificationManager = EventNotificationManager(context)

        mainEnvironment?.getLast10EventWithAnalytic()
            ?.catch { }
            ?.collect { analytics ->
                analytics.forEach {
                    eventNotificationManager?.show(
                        it.analytic,
                        it.events
                    )
                }
            }
    }

    fun record(event: Event) {
        val createdAt = dateTimeProvider.now()
        GlobalScope.launch(Dispatchers.IO) {
            mainEnvironment?.getAnalytic(event)
                ?.catch { }
                ?.collect {
                    mainEnvironment?.insertEvent(it, event, createdAt)
                }
        }
    }
}
