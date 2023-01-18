package com.survivalcoding.stopwatch.presentation.stopwatch

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.survivalcoding.stopwatch.domain.model.LabTime
import com.survivalcoding.stopwatch.domain.use_case.StopWatchUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.timer

private const val TIMER_PERIOD = 10L

@HiltViewModel
class StopWatchViewModel @Inject constructor(
    private val stopWatchUseCases: StopWatchUseCases,
    private val sharedPref: SharedPreferences
) : ViewModel() {

    private var timer: Timer? = null
    var isPlayedOneMore: Boolean = false

    private val _milSec = MutableStateFlow(sharedPref.getLong("latest_time", 0L))
    val milSec: StateFlow<Long> = _milSec.asStateFlow()

    init {
        if (milSec.value > 0) isPlayedOneMore = true
    }

    var isPlaying: Boolean = false

    val allLabTimes: Flow<List<LabTime>> = stopWatchUseCases.getAllLabTimesUseCase()

    fun record() = viewModelScope.launch {
        stopWatchUseCases.recordTimeUseCase(LabTime(time = milSec.value))
    }

    fun timerPlay() {
        if (isPlaying) return
        isPlaying = true
        isPlayedOneMore = true

        timer = timer(period = TIMER_PERIOD) {
            _milSec.value = milSec.value + TIMER_PERIOD
        }
    }

    fun timerStop() {
        if (!isPlaying) return
        isPlaying = false

        timer?.cancel()
    }

    fun clear() {
        timerStop()
        _milSec.value = 0
        isPlayedOneMore = false

        viewModelScope.launch {
            stopWatchUseCases.deleteAllTimesUseCase()
        }
    }

    fun saveLatestMilSec() {
        with(sharedPref.edit()) {
            putLong("latest_time", milSec.value)
            apply()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerStop()
    }
}