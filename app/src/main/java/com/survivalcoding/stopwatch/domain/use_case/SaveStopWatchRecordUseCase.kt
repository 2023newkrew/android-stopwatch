package com.survivalcoding.stopwatch.domain.use_case

import com.survivalcoding.stopwatch.domain.model.StopWatchRecord
import com.survivalcoding.stopwatch.domain.repository.LocalStorageRepository

class SaveStopWatchRecordUseCase(private val localStorageRepository: LocalStorageRepository) {
    operator fun invoke(stopWatchRecord: StopWatchRecord) {
        localStorageRepository.putBoolean("isPaused", stopWatchRecord.isPaused)
        localStorageRepository.putBoolean("isWorking", stopWatchRecord.isWorking)
        localStorageRepository.putInt("standardLapTime", stopWatchRecord.standardLapTime)
        localStorageRepository.putInt("startLapTime", stopWatchRecord.startLapTime)
        localStorageRepository.putInt("exitTime", stopWatchRecord.exitTime)
    }
}