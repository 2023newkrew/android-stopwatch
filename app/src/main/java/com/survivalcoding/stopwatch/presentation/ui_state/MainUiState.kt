package com.survivalcoding.stopwatch.presentation.ui_state

data class MainUiState(
    var hour: Int = 0,
    var minute: Int = 0,
    var sec: Int = 0,
    var milliSec: Int = 0
) {
    fun setZero() {
        hour = 0
        minute = 0
        sec = 0
        milliSec = 0
    }
}