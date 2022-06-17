package com.wisnu.kurniawan.debugview.foundation.datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.wisnu.kurniawan.debugview.DateFactory
import com.wisnu.kurniawan.debugview.expect
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.DebugViewDatabase
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.ReadDao
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.WriteDao
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.AnalyticDb
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.EventDb
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalTime
@RunWith(RobolectricTestRunner::class)
class ReadTest {

    private lateinit var writeDao: WriteDao
    private lateinit var readDao: ReadDao
    private lateinit var db: DebugViewDatabase

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DebugViewDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        writeDao = db.writeDao()
        readDao = db.readDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun searchEvent() = runBlocking {
        val analytic1 = AnalyticDb(
            id = "1",
            name = "name1",
            isRecording = false,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate
        )
        val event1 = EventDb(
            id = "1",
            name = "name1",
            createdAt = DateFactory.constantDate,
            analyticId = "1",
            properties = ""
        )
        val event2 = EventDb(
            id = "2",
            name = "name2",
            createdAt = DateFactory.constantDate,
            analyticId = "1",
            properties = ""
        )
        val event3 = EventDb(
            id = "3",
            name = "name3",
            createdAt = DateFactory.constantDate,
            analyticId = "1",
            properties = ""
        )
        val event4 = EventDb(
            id = "4",
            name = "aaa11",
            createdAt = DateFactory.constantDate,
            analyticId = "1",
            properties = ""
        )
        val event5 = EventDb(
            id = "5",
            name = "aaa2",
            createdAt = DateFactory.constantDate,
            analyticId = "1",
            properties = ""
        )
        val event6 = EventDb(
            id = "6",
            name = "name6",
            createdAt = DateFactory.constantDate,
            analyticId = "1",
            properties = "aaa9"
        )

        writeDao.insertAnalytics(listOf(analytic1))
        writeDao.insertEvent(event1)
        writeDao.insertEvent(event2)
        writeDao.insertEvent(event3)
        writeDao.insertEvent(event4)
        writeDao.insertEvent(event5)
        writeDao.insertEvent(event6)

        readDao.searchEvent("*aa*").expect(
            listOf(event4, event5, event6)
        )
    }

}

