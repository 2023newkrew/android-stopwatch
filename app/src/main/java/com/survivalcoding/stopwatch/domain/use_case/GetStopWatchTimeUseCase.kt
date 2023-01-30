package com.survivalcoding.stopwatch.domain.use_case

import com.survivalcoding.stopwatch.domain.model.StopWatchRecord
import com.survivalcoding.stopwatch.domain.repository.LocalStorageRepository

class GetStopWatchTimeUseCase(private val localStorageRepository: LocalStorageRepository) {
    operator fun invoke(): Int {
        return localStorageRepository.getInt("exitTime")
    }
}