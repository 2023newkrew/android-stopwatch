package com.survivalcoding.stopwatch.viewmodel

import androidx.lifecycle.*
import com.survivalcoding.stopwatch.entity.LabTime
import com.survivalcoding.stopwatch.repository.LabTimeRepository
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timer

class MainViewModel(private val repository: LabTimeRepository) : ViewModel() {
    private var milSec: Long = 0L


    companion object {
        const val TIMER_PERIOD = 10L
    }

    private var timer: Timer? = null

    val milSecLiveData = MutableLiveData(milSec)
    var isPlaying: Boolean = false
    var isPlayedOneMore: Boolean = false

    val allLabTimes: LiveData<List<LabTime>> = repository.allLabTimes.asLiveData()

    private fun insert(labTime: LabTime) = viewModelScope.launch {
        repository.insert(labTime)
    }

    private fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    fun init() {
        timerStop()
        milSec = 0
        milSecLiveData.postValue(milSec)
        isPlayedOneMore = false
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

    override fun onCleared() {
        super.onCleared()
        timerStop()
    }
}

class MainViewModelFactory(private val repository: LabTimeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}