package com.survivalcoding.stopwatch.presentation.main.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.survivalcoding.stopwatch.domain.use_case.GetStopWatchStateUseCase
import com.survivalcoding.stopwatch.domain.use_case.GetStopWatchTimeUseCase
import com.survivalcoding.stopwatch.domain.use_case.SaveStopWatchTimeUseCase
import com.survivalcoding.stopwatch.presentation.main.ui_state.MainUiState
import com.survivalcoding.stopwatch.presentation.stopwatch.ui_state.StopWatchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.timer

@HiltViewModel
class MainViewModel
@Inject constructor(
    private val getStopWatchTimeUseCase: GetStopWatchTimeUseCase,
    private val saveStopWatchTimeUseCase: SaveStopWatchTimeUseCase,
    private val getStopWatchStateUseCase: GetStopWatchStateUseCase
) : ViewModel() {

    private val _mainUiState = MutableStateFlow(MainUiState(getStopWatchTimeUseCase()))
    val mainUiState = _mainUiState.asStateFlow()

    private val stopWatchUiState = getStopWatchStateUseCase()

    private var timerWork: Timer? = null
    init {
        if (stopWatchUiState.isWorking && !stopWatchUiState.isPaused) {
            timerStart()
        }
    }

    fun timerStart() {
        timerWork = timer(period = 10) {
            val tmpTime = mainUiState.value.time
            _mainUiState.value = mainUiState.value.copy(
                time = tmpTime+1

            )

        }
    }

    fun timerPause() {
        println(timerWork)
        timerWork?.cancel()
    }

    fun timeReset() {
        timerWork?.cancel()
        _mainUiState.value = mainUiState.value.copy(
            time = 0
        )
    }

    override fun onCleared() {
        timerWork?.cancel()
        super.onCleared()
    }

    fun onStoppedAction() {
        saveStopWatchTimeUseCase(_mainUiState.value.time)
    }

}

