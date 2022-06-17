package com.wisnu.kurniawan.debugview

import android.content.Context
import android.util.Log
import com.wisnu.kurniawan.debugview.exception.AnalyticNotFoundException
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.DebugViewDatabase
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.LocalManager
import com.wisnu.kurniawan.debugview.internal.foundation.extension.require
import com.wisnu.kurniawan.debugview.internal.foundation.wrapper.DateTimeProviderImpl
import com.wisnu.kurniawan.debugview.internal.foundation.wrapper.IdProviderImpl
import com.wisnu.kurniawan.debugview.model.Analytic
import com.wisnu.kurniawan.debugview.model.Event
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object DebugView {

    private val analytics = MutableStateFlow(mapOf<String, String>())

    internal lateinit var db: DebugViewDatabase

    internal var localManager: LocalManager? = null

    fun build(context: Context, analytics: List<Analytic>) {
        GlobalScope.launch(Dispatchers.IO) {
            require(analytics)

            db = DebugViewDatabase.getInstance(context, analytics)

            localManager = LocalManager(
                dispatcher = Dispatchers.IO,
                readDao = db.readDao(),
                writeDao = db.writeDao(),
                idProvider = IdProviderImpl(),
                dateTimeProvider = DateTimeProviderImpl()
            )

            localManager?.getAnalyticNameWithIds()
                ?.collect {
                    DebugView.analytics.emit(it)
                }
            // init access seismic
        }
    }

    fun log(analyticName: String, event: Event) {
        GlobalScope.launch(Dispatchers.IO) {
            val analyticId = analytics.value[analyticName] ?: throw AnalyticNotFoundException(analyticName)
            localManager?.insertEvent(analyticId, event)
        }
    }

}
