package com.survivalcoding.stopwatch.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LaptimeRecordDao {
    @Query("SELECT * FROM laptimerecord")
    suspend fun getAll(): List<LaptimeRecord>

    @Insert
    suspend fun insert(laptimeRecord: LaptimeRecord)

    @Delete
    suspend fun delete(laptimeRecord: LaptimeRecord)

    @Query("DELETE FROM laptimerecord")
    suspend fun deleteAll()
    // TODO: suspend 함수 물어보기
}