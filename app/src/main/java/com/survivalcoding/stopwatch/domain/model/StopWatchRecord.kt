package com.survivalcoding.stopwatch.domain.model

data class StopWatchRecord(
    var isPaused: Boolean = true,
    var isWorking: Boolean = false,
    var standardLapTime: Int = 0,
    var startLapTime: Int = 0,
){
    fun reset(){
        standardLapTime = 0
        startLapTime = 0
        isPaused = true
        isWorking = false
    }
}