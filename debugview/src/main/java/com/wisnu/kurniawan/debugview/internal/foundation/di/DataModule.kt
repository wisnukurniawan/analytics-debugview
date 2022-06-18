package com.wisnu.kurniawan.debugview.internal.foundation.di

import android.content.Context
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.DebugViewDatabase
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.LocalManager
import com.wisnu.kurniawan.debugview.internal.foundation.wrapper.DateTimeProvider
import kotlinx.coroutines.Dispatchers

internal object DataModule {

    lateinit var db: DebugViewDatabase
    lateinit var localManager: LocalManager

    fun inject(
        context: Context,
        tags: List<String>
    ) {
        db = DebugViewDatabase.getInstance(context, tags)
    }

    fun inject(db: DebugViewDatabase, dateTimeProvider: DateTimeProvider) {
        localManager = LocalManager(
            dispatcher = Dispatchers.IO,
            readDao = db.readDao(),
            writeDao = db.writeDao(),
            dateTimeProvider = dateTimeProvider
        )
    }
}
