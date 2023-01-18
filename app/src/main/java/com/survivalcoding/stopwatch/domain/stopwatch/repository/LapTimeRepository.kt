package com.survivalcoding.stopwatch.domain.stopwatch.repository

import com.survivalcoding.stopwatch.domain.stopwatch.model.LapTimeRecord
import kotlinx.coroutines.flow.Flow

interface LapTimeRepository {

    fun getLapTimes(): Flow<List<LapTimeRecord>>

    suspend fun insertLapTime(lapTimeRecord: LapTimeRecord)

    suspend fun deleteLapTime(lapTimeRecord: LapTimeRecord)

    suspend fun deleteAllLapTimes()
}