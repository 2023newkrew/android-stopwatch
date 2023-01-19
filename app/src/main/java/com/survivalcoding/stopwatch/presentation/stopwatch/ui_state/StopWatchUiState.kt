package com.survivalcoding.stopwatch.presentation.stopwatch.ui_state

data class StopWatchUiState(
    var isPaused: Boolean = true,
    var isWorking: Boolean = false,
    var progressPercent: Int = 0
)