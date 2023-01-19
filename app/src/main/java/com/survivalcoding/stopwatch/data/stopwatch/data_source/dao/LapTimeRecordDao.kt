package com.survivalcoding.stopwatch.data.stopwatch.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.survivalcoding.stopwatch.domain.stopwatch.model.LapTimeRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface LapTimeRecordDao {
    @Query("SELECT * FROM laptimerecord")
    fun getAll(): Flow<List<LapTimeRecord>>

    @Insert
    suspend fun insert(lapTimeRecord: LapTimeRecord)

    @Delete
    suspend fun delete(lapTimeRecord: LapTimeRecord)

    @Query("DELETE FROM laptimerecord")
    suspend fun deleteAll()
}