package com.survivalcoding.stopwatch.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.survivalcoding.stopwatch.Config.Companion.PERIOD_TIMER
import java.util.*
import kotlin.concurrent.timer

class MainViewModel : ViewModel() {
    private var timer: Timer? = null

    var isRunning: Boolean = false
    val timeLiveData = MutableLiveData(0L)

    fun play() {
        timer = timer(period = PERIOD_TIMER) {
            timeLiveData.value?.let {
                timeLiveData.postValue(it + PERIOD_TIMER)
            }
        }
    }

    fun pause() {
        timer?.cancel()
    }
}