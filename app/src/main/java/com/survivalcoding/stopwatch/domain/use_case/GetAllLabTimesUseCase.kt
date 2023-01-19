package com.survivalcoding.stopwatch.domain.use_case

import com.survivalcoding.stopwatch.domain.model.LabTime
import com.survivalcoding.stopwatch.domain.repository.LabTimeRepository
import kotlinx.coroutines.flow.Flow

class GetAllLabTimesUseCase(
    private val repository: LabTimeRepository
) {
    operator fun invoke(): Flow<List<LabTime>> {
        return repository.allLabTimes
    }
}