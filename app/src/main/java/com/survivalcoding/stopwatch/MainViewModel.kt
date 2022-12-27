package com.survivalcoding.stopwatch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class MainViewModel : ViewModel() {
    var isPaused = true
    private var timerWork: Timer? = null
    private var time = 0
    private var hour = 0
    private var minute = 0
    private var sec = 0
    private var milliSec = 0
    private var standardLapTime = 0
    private var progressPercent = 0
    private var startLapTime = 0

    val liveHourData = MutableLiveData(hour)
    val liveMinuteData = MutableLiveData(minute)
    val liveSecData = MutableLiveData(sec)
    val liveMilliSecData = MutableLiveData(milliSec)
    val liveProgressPercent = MutableLiveData(progressPercent)

    fun start() {
        isPaused = false
        timerWork = kotlin.concurrent.timer(period = 10) {    // timer() 호출
            time++    // period=10, 0.01초마다 time를 1씩 증가
            hour = time / 360000
            minute = time / 6000 % 60
            sec = time / 100 % 60    // time/100, 나눗셈의 몫 (초 부분)
            milliSec = time % 100    // time%100, 나눗셈의 나머지 (밀리초 부분)
            if(standardLapTime>0){
                progressPercent = (time - startLapTime)*100/standardLapTime
            }

            liveHourData.postValue(hour)
            liveMinuteData.postValue(minute)
            liveSecData.postValue(sec)
            liveMilliSecData.postValue(milliSec)
            liveProgressPercent.postValue(progressPercent)
        }
    }

    fun pause() {
        isPaused = true
        timerWork?.cancel()
    }

    fun lapTime() {
        if(standardLapTime==0) standardLapTime = time
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
        liveHourData.postValue(0)
        liveMinuteData.postValue(0)
        liveSecData.postValue(0)
        liveMilliSecData.postValue(0)
        liveProgressPercent.postValue(0)

        // TODO laptime 기록 삭제
    }

}