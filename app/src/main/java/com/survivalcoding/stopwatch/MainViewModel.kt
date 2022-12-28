package com.survivalcoding.stopwatch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class MainViewModel : ViewModel() {
    var isPaused = true
    var isWorking = false
    private var timerWork: Timer? = null
    private var time = 0
    private var standardLapTime = 0
    private var progressPercent = 0
    private var startLapTime = 0

    private val state = MainUiState()

    val liveStateData = MutableLiveData(state)
    val liveProgressPercent = MutableLiveData(progressPercent)

    fun start() {
        isPaused = false
        timerWork = kotlin.concurrent.timer(period = 10) {    // timer() 호출
            time++    // period=10, 0.01초마다 time를 1씩 증가
            state.hour = time / 360000
            state.minute = time / 6000 % 60
            state.sec = time / 100 % 60    // time/100, 나눗셈의 몫 (초 부분)
            state.milliSec = time % 100    // time%100, 나눗셈의 나머지 (밀리초 부분)
            if (standardLapTime > 0) {
                progressPercent = (time - startLapTime) * 100 / standardLapTime
                liveProgressPercent.postValue(progressPercent)
            }
            liveStateData.postValue(state)
        }
    }

    fun pause() {
        isPaused = true
        timerWork?.cancel()
    }

    fun lapTime() {
        if (standardLapTime == 0) standardLapTime = time
        startLapTime = time

        //TODO laptime 기록 저장

    }

    fun reset() {
        timerWork?.cancel()
        time = 0
        standardLapTime = 0
        startLapTime = 0
        progressPercent = 0
        isPaused = true
        state.setZero()
        liveStateData.value = state
        liveProgressPercent.value = 0

        // TODO laptime 기록 삭제
    }

    override fun onCleared() {
        super.onCleared()
        timerWork?.cancel()
    }

}

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