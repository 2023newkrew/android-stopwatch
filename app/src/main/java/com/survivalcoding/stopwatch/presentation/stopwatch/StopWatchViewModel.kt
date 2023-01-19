package com.survivalcoding.stopwatch.presentation.stopwatch

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.survivalcoding.stopwatch.domain.stopwatch.model.LapTimeRecord
import com.survivalcoding.stopwatch.domain.stopwatch.use_case.GetStopWatchStateUseCase
import com.survivalcoding.stopwatch.domain.stopwatch.use_case.SaveStopWatchStateUseCase
import com.survivalcoding.stopwatch.domain.stopwatch.use_case.bundle.LapTimeRecordUseCaseBundle
import com.survivalcoding.stopwatch.presentation.stopwatch.state.MainUiState
import com.survivalcoding.stopwatch.presentation.stopwatch.state.ProgressBarState
import com.survivalcoding.stopwatch.presentation.stopwatch.state.StopWatchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class StopWatchViewModel @Inject constructor(
    application: Application,
    private val lapTimeRecordUseCaseBundle: LapTimeRecordUseCaseBundle,
    private val getStopWatchStateUseCase: GetStopWatchStateUseCase,
    private val saveStopWatchStateUseCase: SaveStopWatchStateUseCase,
) : ViewModel() {

    private var time = 0
    private var timerWork: Timer? = null

    private var _stopWatchState = StopWatchState()
    val stopWatchState: StopWatchState
        get() = _stopWatchState

    private val _mainUiState = MutableStateFlow(MainUiState())
    val mainUiState = _mainUiState.asStateFlow()

    private val _progressPercentState = MutableStateFlow(ProgressBarState())
    val progressPercentState = _progressPercentState.asStateFlow()

    init {
        viewModelScope.launch {
            _stopWatchState = stopWatchState.copy(
                isPaused = getStopWatchStateUseCase.isPausedFlow().first() ?: true,
                isWorking = getStopWatchStateUseCase.isWorkingFlow().first() ?: false,
                standardLapTime = getStopWatchStateUseCase.startLapTime().first() ?: 0,
                startLapTime = getStopWatchStateUseCase.startLapTime().first() ?: 0,
                exitTime = getStopWatchStateUseCase.exitTime().first() ?: 0,
            )
            time = stopWatchState.exitTime
            if (!stopWatchState.isPaused) {
                stopWatchStart()
            }
        }
    }

    fun setStopWatchState(isWorking: Boolean) {
        _stopWatchState = stopWatchState.copy(
            isWorking = isWorking
        )
    }

    fun isWorkingFlow(): Flow<Boolean?> =
        getStopWatchStateUseCase.isWorkingFlow()

    fun getLapTimeRecords(): Flow<List<LapTimeRecord>> =
        lapTimeRecordUseCaseBundle.getLapTimesUseCase()

    fun stopWatchStart() {
        _stopWatchState = stopWatchState.copy(
            isPaused = false
        )
        timerWork = kotlin.concurrent.timer(period = 10) {    // timer() 호출
            time++    // period=10, 0.01초마다 time를 1씩 증가
            _mainUiState.value = mainUiState.value.copy(
                hour = time / 360000,
                minute = time / 6000 % 60,
                sec = time / 100 % 60,
                milliSec = time % 100
            )

            if (stopWatchState.standardLapTime > 0) {
                _progressPercentState.value = progressPercentState.value.copy(
                    percent = (time - stopWatchState.startLapTime) *
                            10 / stopWatchState.standardLapTime
                )
            }
        }
    }

    fun stopWatchPause() {
        _stopWatchState = stopWatchState.copy(
            isPaused = true
        )
        timerWork?.cancel()
    }

    fun stopWatchReset() {
        timerWork?.cancel()
        // 모든 랩타임 삭제
        viewModelScope.launch {
            lapTimeRecordUseCaseBundle.deleteAllLapTimesUseCase()
        }
        time = 0
        _stopWatchState = stopWatchState.copy(
            isPaused = true,
            standardLapTime = 0,
            startLapTime = 0
        )
        _mainUiState.value = mainUiState.value.copy(
            hour = 0,
            minute = 0,
            sec = 0,
            milliSec = 0
        )
        _progressPercentState.value = progressPercentState.value.copy(
            percent = 0
        )
    }

    fun lapTime() {
        val currentTime = time
        val elapsedTime = if (stopWatchState.standardLapTime == 0) {
            _stopWatchState = stopWatchState.copy(
                standardLapTime = currentTime,
            )
            stopWatchState.startLapTime
        } else currentTime - stopWatchState.startLapTime//startLapTime - lastEndTime
        _stopWatchState = stopWatchState.copy(
            startLapTime = currentTime
        )

        viewModelScope.launch {
            lapTimeRecordUseCaseBundle.insertLapTimeUseCase(
                LapTimeRecord(elapsedTime = elapsedTime, endTime = currentTime)
            )
        }
    }

    fun initTime() {
        _mainUiState.value = mainUiState.value.copy(
            hour = time / 360000,
            minute = time / 6000 % 60,
            sec = time / 100 % 60,
            milliSec = time % 100
        )
        if (stopWatchState.standardLapTime > 0) {
            _progressPercentState.value = progressPercentState.value.copy(
                percent = (time - stopWatchState.startLapTime) *
                        10 / stopWatchState.standardLapTime
            )
        }
    }

    fun onStoppedAction() {
        _stopWatchState = stopWatchState.copy(
            exitTime = time
        )
        // 현재 기록 데이터 저장
//        editor.putString("stopWatchState", Json.encodeToString(stopWatchState))
//        editor.apply()
        viewModelScope.launch {
            saveStopWatchStateUseCase(stopWatchState)
        }
    }
}
