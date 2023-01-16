package com.survivalcoding.stopwatch.data.database.data_source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.survivalcoding.stopwatch.domain.model.LapTimeRecord

@Database(entities = [LapTimeRecord::class], version = 1, exportSchema = false)
abstract class LaptimeDatabase : RoomDatabase() {
    abstract fun laptimeRecordDao(): LaptimeRecordDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: LaptimeDatabase? = null

        fun getDatabase(context: Context): LaptimeDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LaptimeDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}