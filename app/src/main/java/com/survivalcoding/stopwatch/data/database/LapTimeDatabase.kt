package com.survivalcoding.stopwatch.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.survivalcoding.stopwatch.data.dao.LapTimeRecordDao
import com.survivalcoding.stopwatch.domain.model.LapTimeRecord

@Database(entities = [LapTimeRecord::class], version = 1, exportSchema = false)
abstract class LapTimeDatabase : RoomDatabase() {
    abstract val lapTimeRecordDao: LapTimeRecordDao

    companion object {
        @Volatile
        private var INSTANCE: LapTimeDatabase? = null

        fun getDatabase(context: Context): LapTimeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LapTimeDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}