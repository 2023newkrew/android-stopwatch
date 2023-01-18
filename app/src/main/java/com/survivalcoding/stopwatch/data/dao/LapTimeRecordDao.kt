package com.survivalcoding.stopwatch.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.survivalcoding.stopwatch.domain.model.LapTimeRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface LapTimeRecordDao {
    @Query("SELECT * FROM laptimerecord")
    fun selectAll(): Flow<List<LapTimeRecord>>

    @Insert
    suspend fun insert(laptimerecord: LapTimeRecord)

    @Query("DELETE FROM laptimerecord")
    suspend fun deleteAll()
}