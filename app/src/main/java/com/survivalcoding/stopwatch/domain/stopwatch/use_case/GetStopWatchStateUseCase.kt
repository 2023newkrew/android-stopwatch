package com.survivalcoding.stopwatch.domain.stopwatch.use_case

import com.survivalcoding.stopwatch.domain.stopwatch.repository.StopWatchStateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStopWatchStateUseCase @Inject constructor(private val stopWatchStateRepository: StopWatchStateRepository) {
    fun isPausedFlow(): Flow<Boolean?> = stopWatchStateRepository.isPausedFlow()

    fun isWorkingFlow(): Flow<Boolean?> = stopWatchStateRepository.isWorkingFlow()

    fun standardLapTime(): Flow<Int?> = stopWatchStateRepository.standardLapTime()

    fun startLapTime(): Flow<Int?> = stopWatchStateRepository.startLapTime()

    fun exitTime(): Flow<Int?> = stopWatchStateRepository.exitTime()
}