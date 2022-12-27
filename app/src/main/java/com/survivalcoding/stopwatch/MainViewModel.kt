package com.survivalcoding.stopwatch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.math.min

class MainViewModel: ViewModel() {
    var isPaused = true
    private var timerWork: Timer? = null
    private var time = 0
    private var hour = 0
    private var minute = 0
    private var sec = 0
    private var milliSec = 0


    val liveHourData = MutableLiveData(hour)
    val liveMinuteData = MutableLiveData(minute)
    val liveSecData = MutableLiveData(sec)
    val liveMilliSecData = MutableLiveData(milliSec)

    fun start() {
        isPaused = false
        timerWork = kotlin.concurrent.timer(period = 10) {	// timer() 호출
            time++	// period=10, 0.01초마다 time를 1씩 증가
            hour = time / 360000
            minute = time / 6000 % 60
            sec = time / 100 % 60	// time/100, 나눗셈의 몫 (초 부분)
            milliSec = time % 100	// time%100, 나눗셈의 나머지 (밀리초 부분)

            liveHourData.postValue(hour)
            liveMinuteData.postValue(minute)
            liveSecData.postValue(sec)
            liveMilliSecData.postValue(milliSec)
        }
    }
    fun pause() {
        isPaused = true
        timerWork?.cancel()
    }
    fun lapTime() {
        // TODO laptime 기록 추가
    }
    fun reset() {

        timerWork?.cancel()
        time = 0
        isPaused = true
        liveHourData.postValue(0)
        liveMinuteData.postValue(0)
        liveSecData.postValue(0)
        liveMilliSecData.postValue(0)

        // TODO laptime 기록 삭제
    }
}