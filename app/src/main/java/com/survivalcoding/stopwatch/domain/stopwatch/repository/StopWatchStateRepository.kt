package com.survivalcoding.stopwatch.domain.stopwatch.repository

import com.survivalcoding.stopwatch.presentation.stopwatch.state.StopWatchState
import kotlinx.coroutines.flow.Flow

interface StopWatchStateRepository {

    suspend fun saveStopWatchState(stopWatchState: StopWatchState)

    fun isPausedFlow(): Flow<Boolean?>

    fun isWorkingFlow(): Flow<Boolean?>

    fun standardLapTime(): Flow<Int?>

    fun startLapTime(): Flow<Int?>

    fun exitTime(): Flow<Int?>
}