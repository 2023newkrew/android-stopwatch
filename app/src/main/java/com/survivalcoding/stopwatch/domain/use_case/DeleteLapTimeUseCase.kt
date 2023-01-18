package com.survivalcoding.stopwatch.domain.use_case

import com.survivalcoding.stopwatch.domain.model.LapTimeRecord
import com.survivalcoding.stopwatch.domain.repository.LapTimeRepository

class DeleteLapTimeUseCase(private val repository: LapTimeRepository) {
    suspend operator fun invoke(lapTimeRecord: LapTimeRecord) =
        repository.deleteLapTime(lapTimeRecord)
}