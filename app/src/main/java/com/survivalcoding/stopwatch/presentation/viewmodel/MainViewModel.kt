package com.survivalcoding.stopwatch.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.survivalcoding.stopwatch.Config
import com.survivalcoding.stopwatch.Config.Companion.PERIOD_TIMER
import com.survivalcoding.stopwatch.presentation.util.TimeSplit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*
import kotlin.concurrent.timer

class MainViewModel : ViewModel() {
    private var timer: Timer? = null

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    fun initTime(time: Long) {
        _state.value = state.value.copy(
            time = time
        )
    }

    fun initBackupTime(backupTime: Long?) {
        _state.value = state.value.copy(
            backupTime = backupTime
        )
    }

    fun initLogList(logList: List<Long>) {
        _state.value = state.value.copy(
            logList = logList
        )
    }

    fun initProgressMax(progressMax: Int) {
        _state.value = state.value.copy(
            timeProgressMax = progressMax,
            checkBackProgressMax = progressMax,
            checkFrontProgressMax = progressMax,
        )
    }

    fun addLog() {
        val logList = state.value.logList.toMutableList()
        logList.add(state.value.time)
        initLogList(logList)
    }

    fun getLogText(): String = getLogText(state.value.logList.lastIndex)

    fun getLogText(index: Int): String {
        val logTime = state.value.logList[index]
        val rightTimeSplit = TimeSplit(logTime)
        val rightLogText = String.format(
            "%01d %02d.%02d",
            rightTimeSplit.minute,
            rightTimeSplit.second,
            rightTimeSplit.milliSecond / 10
        )
        val leftLogText =
            if (index == 0) {
                rightLogText
            } else {
                val gap = logTime - state.value.logList[index - 1]
                val leftTimeSplit = TimeSplit(gap)
                String.format(
                    "%01d %02d.%02d",
                    leftTimeSplit.minute,
                    leftTimeSplit.second,
                    leftTimeSplit.milliSecond / 10
                )
            }
        return "# ${String.format("%02d", index + 1)}   $leftLogText   $rightLogText"
    }

    fun play() {
        timer = timer(period = PERIOD_TIMER) {
            _state.value = state.value.copy(
                isRunning = true,
                time = state.value.time + PERIOD_TIMER
            )
        }
    }

    fun pause() {
        timer?.cancel()
        _state.value = state.value.copy(
            isRunning = false
        )
    }

    fun refresh() {
        pause()
        initTime(0L)
        initBackupTime(null)
        initLogList(listOf())
        _state.value = state.value.copy(
            timeProgress = 0,
            timeProgressMax = 0,
            checkBackProgress = 0,
            checkBackProgressMax = 0,
            checkFrontProgress = 0,
            checkFrontProgressMax = 0
        )
    }

    fun lap(timeProgress: Int = 0, timeProgressMax: Int = 0) {
        if (state.value.logList.isEmpty()) {
            initProgressMax(state.value.time.toInt())
        } else {
            _state.value = state.value.copy(
                checkBackProgress = timeProgress,
                checkFrontProgress = timeProgress - (timeProgressMax * Config.THICK_CHECKER).toInt(),
                timeProgress = 0
            )
        }
        initBackupTime(state.value.time)
    }
}

data class MainState(
    val isRunning: Boolean = false,
    val time: Long = 0L,
    val backupTime: Long? = null,
    val logList: List<Long> = listOf(),
    val timeProgress: Int = 0,
    val timeProgressMax: Int = 0,
    val checkBackProgress: Int = 0,
    val checkBackProgressMax: Int = 0,
    val checkFrontProgress: Int = 0,
    val checkFrontProgressMax: Int = 0
)