package com.survivalcoding.stopwatch.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import com.survivalcoding.stopwatch.StopWatchApplication
import com.survivalcoding.stopwatch.entity.LabTime
import com.survivalcoding.stopwatch.repository.LabTimeRepository
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timer

class MainViewModel(application: StopWatchApplication) : AndroidViewModel(application) {
    private val sharedPref: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(application)
    }
    private val repository: LabTimeRepository = application.repository


    companion object {
        const val TIMER_PERIOD = 10L
    }

    private var timer: Timer? = null
    var isPlayedOneMore: Boolean = false
    private var milSec: Long

    // 측정 후 초기화 하지 않고 종료한 경우 다시 켰을 때 마지막 시간이 기록되도락 함.
    init {
        milSec = sharedPref.getLong("latest_time", 0L)
        if (milSec > 0) isPlayedOneMore = true
    }

    val milSecLiveData = MutableLiveData(milSec)

    init {
        if (milSec > 0) milSecLiveData.postValue(milSec)
    }


    var isPlaying: Boolean = false


    val allLabTimes: LiveData<List<LabTime>> = repository.allLabTimes.asLiveData()

    private fun insert(labTime: LabTime) = viewModelScope.launch {
        repository.insert(labTime)
    }

    private fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    fun milSecInitialize(milSec: Long = 0L) {
        this.milSec = milSec
        if (this.milSec > 0) isPlayedOneMore = false
        milSecLiveData.postValue(milSec)
    }

    fun init() {
        timerStop()
        milSec = 0
        isPlayedOneMore = false
        milSecLiveData.postValue(milSec)
        deleteAll()
    }

    fun record() {
        insert(LabTime(time = milSec))
    }


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

    fun saveLatestMilSec() {
        with(sharedPref.edit()) {
            putLong("latest_time", milSec)
            apply()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerStop()

    }
}

class MainViewModelFactory(private val application: StopWatchApplication) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}