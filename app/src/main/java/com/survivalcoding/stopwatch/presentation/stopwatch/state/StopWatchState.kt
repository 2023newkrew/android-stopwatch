package com.survivalcoding.stopwatch.presentation.stopwatch.state

import kotlinx.serialization.Serializable

@Serializable
data class StopWatchState(
    val isPaused: Boolean = true,
    val isWorking: Boolean = false,
    val standardLapTime: Int = 0,
    val startLapTime: Int = 0,
    val exitTime: Int = 0
)

