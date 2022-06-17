package com.wisnu.kurniawan.debugview.internal.foundation.datastore

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.AnalyticDb
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.EventDb
import com.wisnu.kurniawan.debugview.internal.foundation.datastore.model.EventFtsDb
import com.wisnu.kurniawan.debugview.internal.foundation.extension.toAnalyticDb
import com.wisnu.kurniawan.debugview.internal.foundation.wrapper.DateTimeProviderImpl
import com.wisnu.kurniawan.debugview.internal.foundation.wrapper.IdProviderImpl
import com.wisnu.kurniawan.debugview.model.Analytic
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        AnalyticDb::class,
        EventDb::class,
        EventFtsDb::class,
    ],
    version = 1,
)
@TypeConverters(DateConverter::class)
internal abstract class DebugViewDatabase : RoomDatabase() {
    abstract fun writeDao(): WriteDao
    abstract fun readDao(): ReadDao

    companion object {
        private const val DB_NAME = "debugview-db"

        @Volatile
        private var INSTANCE: DebugViewDatabase? = null

        fun getInstance(context: Context, analytics: List<Analytic>): DebugViewDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context, analytics).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context, analytics: List<Analytic>): DebugViewDatabase {
            val db = Room.databaseBuilder(
                context,
                DebugViewDatabase::class.java,
                DB_NAME
            )
                .addCallback(
                    object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            GlobalScope.launch(Dispatchers.IO) {
                                initPrePopulateDefaultAnalytic(context, analytics)
                            }
                        }
                    }
                )
                .fallbackToDestructiveMigration()
            return db.build()
        }

        private suspend fun initPrePopulateDefaultAnalytic(context: Context, analytics: List<Analytic>) {
            val analyticDbs = analytics.map {
                it.toAnalyticDb(
                    id = { IdProviderImpl().generate() },
                    createdAt = { DateTimeProviderImpl().now() },
                    updatedAt = null
                )
            }
            val writeDao = getInstance(context, analytics).writeDao()

            writeDao.insertAnalytics(analyticDbs)
        }


    }
}
