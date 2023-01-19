package com.survivalcoding.stopwatch.domain.use_case

import com.survivalcoding.stopwatch.domain.model.LabTime
import com.survivalcoding.stopwatch.domain.repository.LabTimeRepository

class RecordTimeUseCase(
    private val repository: LabTimeRepository
) {
    suspend operator fun invoke(labTime: LabTime) {
        repository.insert(labTime)
    }
}