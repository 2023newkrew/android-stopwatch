package com.survivalcoding.stopwatch.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.survivalcoding.stopwatch.domain.model.LabTime

@Database(entities = [LabTime::class], version = 1, exportSchema = false)
abstract class StopWatchDatabase : RoomDatabase() {
    abstract fun labTimeDao(): LabTimeDao

    companion object {
        const val DB_NAME = "stopwatch_database"
    }
}