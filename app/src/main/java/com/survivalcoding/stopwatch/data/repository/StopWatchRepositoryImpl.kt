package com.survivalcoding.stopwatch.data.repository

import com.survivalcoding.stopwatch.data.data_source.dao.LapTimeRecordDao
import com.survivalcoding.stopwatch.domain.model.LapTimeRecord
import com.survivalcoding.stopwatch.domain.repository.StopWatchRepository
import kotlinx.coroutines.flow.Flow

class StopWatchRepositoryImpl(private val lapTimeRecordDao: LapTimeRecordDao): StopWatchRepository{
    override fun getLapTimes(): Flow<List<LapTimeRecord>> {
        return lapTimeRecordDao.selectAll()
    }

    override suspend fun resetLapTimes() {
        lapTimeRecordDao.deleteAll()
    }

    override suspend fun addLapTime(lapTimeRecord: LapTimeRecord) {
        lapTimeRecordDao.insert(laptimerecord = lapTimeRecord)
    }


}