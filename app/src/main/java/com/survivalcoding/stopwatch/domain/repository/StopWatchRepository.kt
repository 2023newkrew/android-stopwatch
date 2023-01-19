package com.survivalcoding.stopwatch.domain.repository

import com.survivalcoding.stopwatch.domain.model.LapTimeRecord
import kotlinx.coroutines.flow.Flow

interface StopWatchRepository {
    fun getLapTimes(): Flow<List<LapTimeRecord>>

    suspend fun resetLapTimes()

    suspend fun addLapTime(lapTimeRecord: LapTimeRecord)
}