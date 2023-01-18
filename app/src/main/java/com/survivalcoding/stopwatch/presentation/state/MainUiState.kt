package com.survivalcoding.stopwatch.presentation.state

data class MainUiState(
    val hour: Int = 0,
    val minute: Int = 0,
    val sec: Int = 0,
    val milliSec: Int = 0
)