package com.wisnu.kurniawan.debugview.foundation.datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.wisnu.kurniawan.debugview.DateFactory
import com.wisnu.kurniawan.debugview.expect
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.DebugViewDatabase
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.ReadDao
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.WriteDao
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.AnalyticDb
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.EventWithAnalytic
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.EventDb
import java.time.LocalDateTime
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalTime
@RunWith(RobolectricTestRunner::class)
class WriteTest {

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
    fun insertAnalytics() = runBlocking {
        val analytics = listOf(
            AnalyticDb(
                id = "1",
                tag = "name1",
                isRecording = false,
                createdAt = DateFactory.constantDate,
                updatedAt = DateFactory.constantDate
            ),
            AnalyticDb(
                id = "2",
                tag = "name2",
                isRecording = false,
                createdAt = DateFactory.constantDate,
                updatedAt = DateFactory.constantDate
            )
        )

        writeDao.insertAnalytics(analytics)

        readDao.getAnalytics().expect(analytics)
    }

    @Test
    fun updateAnalytic() = runBlocking {
        val analytic1 = AnalyticDb(
            id = "1",
            tag = "name1",
            isRecording = false,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate
        )
        val analytic2 = AnalyticDb(
            id = "2",
            tag = "name2",
            isRecording = false,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate
        )
        val analytics = listOf(
            analytic1,
            analytic2
        )

        writeDao.insertAnalytics(analytics)
        writeDao.updateAnalytic(analytic1.tag, true, DateFactory.constantDate)

        readDao.getAnalytic(analytic1.tag).expect(analytic1.copy(isRecording = true))
    }

    @Test
    fun insertEvent() = runBlocking {
        val analytic1 = AnalyticDb(
            id = "1",
            tag = "name1",
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

        writeDao.insertAnalytics(listOf(analytic1))
        writeDao.insertEvent(event1)

        readDao.getEvent(event1.id).expect(event1)
    }

    @Test
    fun deleteEvent() = runBlocking {
        val analytic1 = AnalyticDb(
            id = "1",
            tag = "name1",
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

        writeDao.insertAnalytics(listOf(analytic1))
        writeDao.insertEvent(event1)
        writeDao.insertEvent(event2)
        writeDao.deleteEvent(analytic1.id)

        readDao.getEvents(analytic1.id, 10).expect(listOf<EventDb>())
    }

    @Test
    fun getEventWithAnalytic() = runBlocking {
        val analytic1 = AnalyticDb(
            id = "1",
            tag = "name1",
            isRecording = false,
            createdAt = LocalDateTime.of(2021, 1, 1, 0, 0, 0, 0),
            updatedAt = DateFactory.constantDate
        )
        val analytic2 = AnalyticDb(
            id = "2",
            tag = "name2",
            isRecording = false,
            createdAt = LocalDateTime.of(2021, 1, 1, 0, 0, 0, 0),
            updatedAt = DateFactory.constantDate
        )
        val event1 = EventDb(
            id = "1",
            name = "name1",
            createdAt = LocalDateTime.of(2021, 1, 3, 0, 0, 0, 0),
            analyticId = "1",
            properties = ""
        )
        val event2 = EventDb(
            id = "2",
            name = "name2",
            createdAt = LocalDateTime.of(2021, 1, 4, 0, 0, 0, 0),
            analyticId = "1",
            properties = ""
        )
        val event3 = EventDb(
            id = "3",
            name = "name3",
            createdAt = LocalDateTime.of(2021, 1, 5, 0, 0, 0, 0),
            analyticId = "1",
            properties = ""
        )
        val event4 = EventDb(
            id = "4",
            name = "name4",
            createdAt = LocalDateTime.of(2021, 1, 6, 0, 0, 0, 0),
            analyticId = "2",
            properties = ""
        )
        val event5 = EventDb(
            id = "5",
            name = "name5",
            createdAt = LocalDateTime.of(2021, 1, 7, 0, 0, 0, 0),
            analyticId = "2",
            properties = ""
        )
        val event6 = EventDb(
            id = "6",
            name = "name6",
            createdAt = LocalDateTime.of(2021, 1, 8, 0, 0, 0, 0),
            analyticId = "2",
            properties = ""
        )

        writeDao.insertAnalytics(listOf(analytic1, analytic2))
        writeDao.insertEvent(event1)
        writeDao.insertEvent(event2)
        writeDao.insertEvent(event3)
        writeDao.insertEvent(event4)
        writeDao.insertEvent(event5)
        writeDao.insertEvent(event6)

        readDao.getEventWithAnalytic(5).expect(
            listOf(
                EventWithAnalytic(
                    analytic2,
                    event6
                ),
                EventWithAnalytic(
                    analytic2,
                    event5
                ),
                EventWithAnalytic(
                    analytic2,
                    event4
                ),
                EventWithAnalytic(
                    analytic1,
                    event3
                ),
                EventWithAnalytic(
                    analytic1,
                    event2
                ),
            )
        )
    }
}
