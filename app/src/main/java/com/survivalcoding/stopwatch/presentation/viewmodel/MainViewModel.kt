package com.survivalcoding.stopwatch.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.survivalcoding.stopwatch.Config.Companion.PERIOD_TIMER
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.timer

class MainViewModel @Inject constructor() : ViewModel() {
    private var timer: Timer? = null

    var runningLiveData = MutableLiveData(false)
    val timeLiveData = MutableLiveData(0L)

    var backupTime :Long? = null
    var logArrayList = ArrayList<Long>()

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