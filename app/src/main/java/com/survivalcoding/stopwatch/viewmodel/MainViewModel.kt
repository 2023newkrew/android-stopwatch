package com.survivalcoding.stopwatch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.concurrent.timer

class MainViewModel : ViewModel() {
    private var milSec: Long = 0L


    companion object {
        const val TIMER_PERIOD = 10L
    }

    private var timer: Timer? = null

    val milSecLiveData = MutableLiveData(milSec)
    var isPlaying: Boolean = false
    var isPlayedOneMore: Boolean = false

    fun timerPlay() {
        if (isPlaying) return
        isPlaying = true
        isPlayedOneMore = true

        timer = timer(period = TIMER_PERIOD) {
            milSec += TIMER_PERIOD
            milSecLiveData.postValue(milSec)
        }
    }

    fun timerStop() {
        if (!isPlaying) return
        isPlaying = false

        timer?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        timerStop()
    }
}