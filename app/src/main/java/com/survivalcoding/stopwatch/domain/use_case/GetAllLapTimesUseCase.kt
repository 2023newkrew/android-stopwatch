package com.survivalcoding.stopwatch.domain.use_case

import com.survivalcoding.stopwatch.domain.model.LapTimeRecord
import com.survivalcoding.stopwatch.domain.repository.StopWatchRepository
import kotlinx.coroutines.flow.Flow

class GetAllLapTimesUseCase(private val repository: StopWatchRepository) {
    operator fun invoke() : Flow<List<LapTimeRecord>> {
        return repository.getLapTimes()
    }
}