package com.survivalcoding.stopwatch.domain.stopwatch.use_case

import com.survivalcoding.stopwatch.domain.stopwatch.repository.StopWatchStateRepository
import com.survivalcoding.stopwatch.presentation.stopwatch.state.StopWatchState
import javax.inject.Inject

class SaveStopWatchStateUseCase @Inject constructor(private val stopWatchStateRepository: StopWatchStateRepository) {
    suspend operator fun invoke(stopWatchState: StopWatchState) =
        stopWatchStateRepository.saveStopWatchState(stopWatchState)
}