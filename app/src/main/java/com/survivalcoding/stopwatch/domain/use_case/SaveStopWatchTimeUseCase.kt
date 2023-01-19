package com.survivalcoding.stopwatch.domain.use_case

import com.survivalcoding.stopwatch.domain.model.StopWatchRecord
import com.survivalcoding.stopwatch.domain.repository.LocalStorageRepository

class SaveStopWatchTimeUseCase (private val localStorageRepository: LocalStorageRepository) {
    operator fun invoke(time: Int) {
        localStorageRepository.putInt("exitTime", time)
    }
}