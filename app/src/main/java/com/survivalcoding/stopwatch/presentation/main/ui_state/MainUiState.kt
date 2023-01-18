package com.survivalcoding.stopwatch.presentation.main.ui_state

data class MainUiState(
    var time: Int = 0
) {
    fun setZero() {
        time = 0
    }
    fun getHour(): Int{
        return time / 360000
    }
    fun getMin(): Int{
        return time / 6000 % 60
    }
    fun getSec(): Int{
        return time / 100 % 60
    }
    fun getMilliSec(): Int {
        return time % 100
    }
}