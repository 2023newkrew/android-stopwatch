package com.survivalcoding.stopwatch.presentation.stopwatch

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.survivalcoding.stopwatch.domain.model.DeltaLabTime
import com.survivalcoding.stopwatch.domain.model.LabTime
import com.survivalcoding.stopwatch.domain.use_case.StopWatchUseCases
import com.survivalcoding.stopwatch.domain.util.toDeltaLabTimes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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


    private val _milSec = MutableStateFlow(sharedPref.getLong("latest_time", 0L))
    val milSec: StateFlow<Long> = _milSec.asStateFlow()

    private val _isPlaying = MutableStateFlow<Boolean>(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isPlayedOneMore = MutableStateFlow<Boolean>(false)
    val isPlayedOneMore: StateFlow<Boolean> = _isPlayedOneMore.asStateFlow()

    init {
        if (milSec.value > 0) _isPlayedOneMore.value = true
    }

    val allDeltaLabTimes: Flow<List<DeltaLabTime>> = stopWatchUseCases.getAllLabTimesUseCase().map {
        it.toDeltaLabTimes()
    }

    fun record() = viewModelScope.launch {
        stopWatchUseCases.recordTimeUseCase(LabTime(time = milSec.value))
    }

    fun timerPlay() {
        if (isPlaying.value) return
        _isPlaying.value = true
        _isPlayedOneMore.value = true

        timer = timer(period = TIMER_PERIOD) {
            _milSec.value = milSec.value + TIMER_PERIOD
        }
    }

    fun timerStop() {
        if (!isPlaying.value) return
        _isPlaying.value = false

        timer?.cancel()
    }

    fun clear() {
        timerStop()
        _milSec.value = 0
        _isPlayedOneMore.value = false

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