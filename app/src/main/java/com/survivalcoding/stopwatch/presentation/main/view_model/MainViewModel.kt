package com.survivalcoding.stopwatch.presentation.main.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.survivalcoding.stopwatch.domain.model.LapTimeRecord
import com.survivalcoding.stopwatch.domain.use_case.GetStopWatchRecordUseCase
import com.survivalcoding.stopwatch.domain.use_case.SaveStopWatchRecordUseCase
import com.survivalcoding.stopwatch.domain.use_case.bundle.LapTimeUseCases
import com.survivalcoding.stopwatch.presentation.main.ui_state.MainUiState
import com.survivalcoding.stopwatch.presentation.stopwatch.ui_state.StopWatchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.timer

@HiltViewModel
class MainViewModel
@Inject constructor(
    private val lapTimeUseCases: LapTimeUseCases,
    private val getStopWatchRecordUseCase: GetStopWatchRecordUseCase,
    private val saveStopWatchRecordUseCase: SaveStopWatchRecordUseCase
) : ViewModel() {

    private val stopWatchRecord = getStopWatchRecordUseCase()

    private val _mainUiState = MutableStateFlow(MainUiState(stopWatchRecord.exitTime))
    val mainUiState = _mainUiState.asStateFlow()

    private val _stopWatchUiState = MutableStateFlow(
        StopWatchUiState(
            isPaused = stopWatchRecord.isPaused,
            isWorking = stopWatchRecord.isWorking,
            progressPercent = (stopWatchRecord.exitTime - stopWatchRecord.startLapTime) * 100 / stopWatchRecord.standardLapTime
        )
    )
    val stopWatchUiState = _stopWatchUiState.asStateFlow()

    val lapTimeLists = lapTimeUseCases.getAllLapTimesUseCase()

    private var timerWork: Timer? = null

    init {
        if (stopWatchRecord.isWorking && !stopWatchRecord.isPaused) {
            timerWork = timer(period = 10) {
                _mainUiState.value.time += 1
            }
        }
    }

    fun start() {
        _stopWatchUiState.value.isPaused = false
        _stopWatchUiState.value.isWorking = true
        timerWork = timer(period = 10) {
            _mainUiState.value.time += 1
        }
    }

    fun pause() {
        _stopWatchUiState.value.isPaused = true
        timerWork?.cancel()
    }

    fun lapTime() {
        val elapsedTime = if (stopWatchRecord.standardLapTime == 0) {
            stopWatchRecord.standardLapTime = mainUiState.value.time
            stopWatchRecord.startLapTime
        } else mainUiState.value.time - stopWatchRecord.startLapTime
        stopWatchRecord.startLapTime = mainUiState.value.time

        val lapTimeRecord = LapTimeRecord(elapsedTime = elapsedTime, endTime = mainUiState.value.time)

        viewModelScope.launch {
            lapTimeUseCases.addLapTimeUseCase(lapTimeRecord)
        }

    }

    fun reset() {
        timerWork?.cancel()
        viewModelScope.launch {
            lapTimeUseCases.resetLapTimesUseCase()
        }

        _mainUiState.value.setZero()
        stopWatchRecord.reset()

        viewModelScope.launch {
            lapTimeUseCases.resetLapTimesUseCase()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerWork?.cancel()
    }

    fun onStoppedAction() {
        saveStopWatchRecordUseCase(stopWatchRecord)
    }

}

