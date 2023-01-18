package com.survivalcoding.stopwatch.domain.use_case

import com.survivalcoding.stopwatch.domain.repository.LabTimeRepository

class DeleteAllTimesUseCase(
    private val repository: LabTimeRepository
) {
    suspend operator fun invoke() {
        repository.deleteAll()
    }
}