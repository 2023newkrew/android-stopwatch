package com.survivalcoding.stopwatch.domain.stopwatch.use_case

import com.survivalcoding.stopwatch.domain.stopwatch.repository.LapTimeRepository

class DeleteAllLapTimesUseCase(private val repository: LapTimeRepository) {
    suspend operator fun invoke() = repository.deleteAllLapTimes()
}