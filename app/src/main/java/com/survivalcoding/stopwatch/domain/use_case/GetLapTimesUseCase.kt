package com.survivalcoding.stopwatch.domain.use_case

import com.survivalcoding.stopwatch.domain.model.LapTimeRecord
import com.survivalcoding.stopwatch.domain.repository.LapTimeRepository
import kotlinx.coroutines.flow.Flow

class GetLapTimesUseCase(private val repository: LapTimeRepository) {
    operator fun invoke(): Flow<List<LapTimeRecord>> = repository.getLapTimes()
}