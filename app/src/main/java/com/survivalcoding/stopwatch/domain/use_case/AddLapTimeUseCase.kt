package com.survivalcoding.stopwatch.domain.use_case

import com.survivalcoding.stopwatch.domain.model.LapTimeRecord
import com.survivalcoding.stopwatch.domain.repository.StopWatchRepository

class AddLapTimeUseCase(private val repository: StopWatchRepository) {
    suspend operator fun invoke(lapTimeRecord: LapTimeRecord){
        repository.addLapTime(lapTimeRecord = lapTimeRecord)
    }
}