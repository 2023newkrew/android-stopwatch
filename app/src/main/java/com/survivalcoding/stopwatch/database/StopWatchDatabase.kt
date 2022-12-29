package com.survivalcoding.stopwatch.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.survivalcoding.stopwatch.dao.LabTimeDao
import com.survivalcoding.stopwatch.entity.LabTime

@Database(entities = [LabTime::class], version = 1, exportSchema = false)
abstract class StopWatchDatabase : RoomDatabase() {
    abstract fun labTimeDao(): LabTimeDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: StopWatchDatabase? = null

        fun getDatabase(context: Context): StopWatchDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StopWatchDatabase::class.java,
                    "stopwatch_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}