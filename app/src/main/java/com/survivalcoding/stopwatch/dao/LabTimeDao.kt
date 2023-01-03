package com.survivalcoding.stopwatch.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.survivalcoding.stopwatch.entity.LabTime
import kotlinx.coroutines.flow.Flow

@Dao
interface LabTimeDao {
    @Query("SELECT * FROM LabTime ORDER BY time")
    fun getAll(): Flow<List<LabTime>>

    @Insert
    suspend fun insert(labTime: LabTime)

    @Query("DELETE FROM LabTime")
    suspend fun deleteAll()
}