package com.survivalcoding.stopwatch.domain.stopwatch.use_case

import com.survivalcoding.stopwatch.domain.stopwatch.model.LapTimeRecord
import com.survivalcoding.stopwatch.domain.stopwatch.repository.LapTimeRepository

class DeleteLapTimeUseCase(private val repository: LapTimeRepository) {
    suspend operator fun invoke(lapTimeRecord: LapTimeRecord) =
        repository.deleteLapTime(lapTimeRecord)
}