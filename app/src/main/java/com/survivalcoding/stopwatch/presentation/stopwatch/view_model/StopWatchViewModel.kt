package com.survivalcoding.stopwatch.presentation.stopwatch.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.survivalcoding.stopwatch.domain.model.LapTimeRecord
import com.survivalcoding.stopwatch.domain.model.StopWatchRecord
import com.survivalcoding.stopwatch.domain.use_case.GetStopWatchStateUseCase
import com.survivalcoding.stopwatch.domain.use_case.SaveStopWatchStateUseCase
import com.survivalcoding.stopwatch.domain.use_case.bundle.LapTimeUseCases
import com.survivalcoding.stopwatch.presentation.stopwatch.ui_state.StopWatchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StopWatchViewModel
@Inject constructor(
    private val lapTimeUseCases: LapTimeUseCases,
    getStopWatchStateUseCase: GetStopWatchStateUseCase,
    private val saveStopWatchStateUseCase: SaveStopWatchStateUseCase
) : ViewModel() {

    val stopWatchRecord: StopWatchRecord = getStopWatchStateUseCase()

    private val _stopWatchUiState = MutableStateFlow(
        StopWatchUiState(
            isPaused = stopWatchRecord.isPaused,
            isWorking = stopWatchRecord.isWorking,
        )
    )
    val stopWatchUiState = _stopWatchUiState.asStateFlow()

    val lapTimeList: Flow<List<LapTimeRecord>> = lapTimeUseCases.getAllLapTimesUseCase()



    fun lapTime(time: Int) {
        val elapsedTime = if (stopWatchRecord.standardLapTime == 0) {
            stopWatchRecord.standardLapTime = time
            stopWatchRecord.startLapTime
        } else time - stopWatchRecord.startLapTime
        stopWatchRecord.startLapTime = time

        val lapTimeRecord =
            LapTimeRecord(elapsedTime = elapsedTime, endTime = time)

        viewModelScope.launch {
            lapTimeUseCases.addLapTimeUseCase(lapTimeRecord)
        }
    }

    fun reset() {
        stopWatchRecord.reset()
        viewModelScope.launch {
            lapTimeUseCases.resetLapTimesUseCase()
        }
        _stopWatchUiState.value = stopWatchUiState.value.copy(
            isPaused = true,
            isWorking = false
        )
    }

    fun pause() {
        stopWatchRecord.pause()
        _stopWatchUiState.value = stopWatchUiState.value.copy(
            isPaused = true,
            isWorking = true
        )
    }

    fun start() {
        stopWatchRecord.start()
        _stopWatchUiState.value = stopWatchUiState.value.copy(
            isPaused = false,
            isWorking = true
        )
    }

    fun onStoppedAction() {
        println(stopWatchRecord.isPaused)
        println(stopWatchRecord.isWorking)
        saveStopWatchStateUseCase(stopWatchRecord)
    }
}