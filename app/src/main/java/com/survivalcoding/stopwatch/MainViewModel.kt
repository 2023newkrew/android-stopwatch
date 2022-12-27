package com.survivalcoding.stopwatch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.concurrent.timer

class MainViewModel : ViewModel() {
    private lateinit var timer: Timer
    var isRunning: Boolean = false
    val timeLiveData = MutableLiveData(0L)

    fun play() {
        timer = timer(period = Config.PERIOD_TIMER) {
            timeLiveData.postValue(timeLiveData.value!! + Config.PERIOD_TIMER)
        }
    }

    fun pause() {
        timer.cancel()
    }

    override fun onCleared() {
        timer.cancel()
        super.onCleared()
    }
}