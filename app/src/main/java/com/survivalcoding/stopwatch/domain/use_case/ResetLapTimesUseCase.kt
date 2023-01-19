package com.survivalcoding.stopwatch.domain.use_case

import com.survivalcoding.stopwatch.domain.repository.StopWatchRepository

class ResetLapTimesUseCase(private val repository: StopWatchRepository) {
    suspend operator fun invoke(){
        repository.resetLapTimes()
    }
}