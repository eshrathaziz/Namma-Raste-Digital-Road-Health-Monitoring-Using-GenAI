package com.nammaraste.health.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [ReportEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reportDao(): ReportDao

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val reportDao = database.reportDao()
                    // Add default reports
                    val defaults = listOf(
                        ReportEntity(road = "Whitefield Main Road", type = "Pothole", severity = "Major — dangerous", date = "2026-04-29", name = "Anon", gps = "12.9716°N, 77.7500°E"),
                        ReportEntity(road = "Silk Board Junction Flyover", type = "Water Logging", severity = "Critical — road unusable", date = "2026-04-28", name = "Rajesh K.", gps = "12.9177°N, 77.6238°E"),
                        ReportEntity(road = "Bannerghatta Road", type = "Crack", severity = "Moderate — needs repair", date = "2026-04-26", name = "Anon", gps = "12.8876°N, 77.5970°E"),
                        ReportEntity(road = "Indiranagar 100ft Road", type = "Pothole", severity = "Minor — surface wear", date = "2026-04-24", name = "Priya S.", gps = "12.9784°N, 77.6408°E"),
                        ReportEntity(road = "Mysore Road (NH-275)", type = "Water Logging", severity = "Moderate — needs repair", date = "2026-04-23", name = "Kiran M.", gps = "12.9516°N, 77.5185°E")
                    )
                    defaults.forEach { reportDao.insertReport(it) }
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "namma_raste_db"
                )
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
