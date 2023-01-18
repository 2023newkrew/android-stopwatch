package com.survivalcoding.stopwatch.data.stopwatch.data_source.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.survivalcoding.stopwatch.DATABASE_NAME
import com.survivalcoding.stopwatch.data.stopwatch.data_source.dao.LapTimeRecordDao
import com.survivalcoding.stopwatch.domain.stopwatch.model.LapTimeRecord

@Database(entities = [LapTimeRecord::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lapTimeRecordDao(): LapTimeRecordDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}