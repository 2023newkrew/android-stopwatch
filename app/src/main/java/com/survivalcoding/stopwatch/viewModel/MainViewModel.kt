package com.survivalcoding.stopwatch.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.concurrent.timer

class MainViewModel : ViewModel() {
    private var milSec: Long = 0L
    private var isPlaying: Boolean = false

    companion object {
        const val TIMER_PERIOD = 10L
    }

    private var timer: Timer? = null

    val milSecLiveData = MutableLiveData(milSec)
    val isPlayingLiveData = MutableLiveData(isPlaying)
    val isFirstPlayLiveData = MutableLiveData(false)

    fun playPauseBtnClicked() =
        if (isPlaying) timerStop() else timerPlay()


    private fun timerPlay() {
        if (isPlaying) return
        isPlaying = true
        isPlayingLiveData.postValue(true)
        isFirstPlayLiveData.postValue(true)

        timer = timer(period = TIMER_PERIOD) {
            milSec += TIMER_PERIOD
            milSecLiveData.postValue(milSec)
        }
    }

    private fun timerStop() {
        if (!isPlaying) return
        isPlaying = false
        isPlayingLiveData.postValue(false)

        timer?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        timerStop()
    }
}