package com.survivalcoding.stopwatch.domain.stopwatch.use_case

import com.survivalcoding.stopwatch.domain.stopwatch.model.LapTimeRecord
import com.survivalcoding.stopwatch.domain.stopwatch.repository.LapTimeRepository
import kotlinx.coroutines.flow.Flow

class GetLapTimesUseCase(private val repository: LapTimeRepository) {
    operator fun invoke(): Flow<List<LapTimeRecord>> = repository.getLapTimes()
}