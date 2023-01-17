package com.survivalcoding.stopwatch.domain.use_case

import com.survivalcoding.stopwatch.domain.repository.LapTimeRepository

class DeleteAllLapTimesUseCase(private val repository: LapTimeRepository) {
    suspend operator fun invoke() = repository.deleteAllLapTimes()
}