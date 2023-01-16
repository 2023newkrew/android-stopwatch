package com.survivalcoding.stopwatch.data.database.repository

import com.survivalcoding.stopwatch.domain.model.LapTimeRecord
import com.survivalcoding.stopwatch.domain.repository.StopWatchRepository
import kotlinx.coroutines.flow.Flow

class StopWatchRepositoryImpl: StopWatchRepository{
    override suspend fun getLapTimes(): Flow<ArrayList<LapTimeRecord>> {
        TODO("Not yet implemented")
    }

    override suspend fun resetLapTimes() {
        TODO("Not yet implemented")
    }

    override suspend fun addLapTime(lapTimeRecord: LapTimeRecord) {
        TODO("Not yet implemented")
    }
}