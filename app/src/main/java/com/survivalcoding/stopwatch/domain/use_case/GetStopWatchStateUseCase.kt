package com.survivalcoding.stopwatch.domain.use_case

import com.survivalcoding.stopwatch.domain.model.StopWatchRecord
import com.survivalcoding.stopwatch.domain.repository.LocalStorageRepository

class GetStopWatchStateUseCase(private val localStorageRepository: LocalStorageRepository) {
    operator fun invoke(): StopWatchRecord{
        return StopWatchRecord(
            isPaused = localStorageRepository.getBoolean("isPaused",true),
            isWorking = localStorageRepository.getBoolean("isWorking",false),
            standardLapTime = localStorageRepository.getInt("standardLapTime"),
            startLapTime = localStorageRepository.getInt("startLapTime"),
        )
    }
}