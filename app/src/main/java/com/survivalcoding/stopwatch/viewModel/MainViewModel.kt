package com.survivalcoding.stopwatch.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.concurrent.timer

class MainViewModel : ViewModel() {
    private var milSec:Long = 0

    companion object{
        const val TIMER_PERIOD = 10L
    }
    private var timer: Timer? = null

    val milSecLiveData = MutableLiveData<Long>(milSec)

    fun timerPlay(){
        timer = timer(period = TIMER_PERIOD){
            milSec += TIMER_PERIOD
            milSecLiveData.postValue(milSec)
        }
    }

    fun timerStop(){
        timer?.cancel()
    }
}