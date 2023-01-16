package com.survivalcoding.stopwatch.data.database.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.survivalcoding.stopwatch.domain.model.LapTimeRecord
import java.util.concurrent.Flow

@Dao
interface LapTimeRecordDao {
    @Query("SELECT * FROM laptimerecord")
    suspend fun getAll(): List<LapTimeRecord>

    @Insert
    suspend fun insert(laptimeRecord: LapTimeRecord)

    @Query("DELETE FROM laptimerecord")
    suspend fun deleteAll()
    // TODO: suspend 함수 물어보기
}