package com.survivalcoding.stopwatch

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import com.survivalcoding.stopwatch.data.database.LapTimeDatabase
import com.survivalcoding.stopwatch.domain.model.LapTimeRecord
import com.survivalcoding.stopwatch.domain.use_case.bundle.LapTimeUseCases
import com.survivalcoding.stopwatch.presentation.ui_state.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(
    application: StopWatchApplication,
    private val lapTimeUseCases: LapTimeUseCases
) : ViewModel() {

    var isPaused = true
    var isWorking = false
    var standardLapTime = 0
    var exitTime = 0
    private var startLapTime = 0
    private val mPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(application)
    val editor = mPreferences.edit() // 에디터 객체 얻기
    private var time = 0

    init {
        isPaused = mPreferences.getBoolean("isPaused", true)
        isWorking = mPreferences.getBoolean("isWorking", false)
        standardLapTime = mPreferences.getInt("standardLapTime", 0)
        time = mPreferences.getInt("exitTime", 0)
        startLapTime = mPreferences.getInt("startLapTime", 0)
        if (!isPaused) {
            start()
        }
    }



    private var timerWork: Timer? = null
    private var progressPercent = 0
    var recordList: ArrayList<LapTimeRecord> = arrayListOf<LapTimeRecord>()
    private val state = MainUiState()

    val liveStateData = MutableLiveData(state)
    val liveProgressPercent = MutableLiveData(progressPercent)
    val liveRecordList = MutableLiveData(recordList)


    fun start() {
        isPaused = false
        timerWork = kotlin.concurrent.timer(period = 10) {    // timer() 호출
            time++    // period=10, 0.01초마다 time를 1씩 증가
            state.hour = time / 360000
            state.minute = time / 6000 % 60
            state.sec = time / 100 % 60    // time/100, 나눗셈의 몫 (초 부분)
            state.milliSec = time % 100    // time%100, 나눗셈의 나머지 (밀리초 부분)
            if (standardLapTime > 0) {
                progressPercent = (time - startLapTime) * 100 / standardLapTime
                liveProgressPercent.postValue(progressPercent)
            }
            liveStateData.postValue(state)
        }
    }

    fun pause() {
        isPaused = true
        timerWork?.cancel()
    }

    fun lapTime() {
        val elapsedTime = if (standardLapTime == 0) {
            standardLapTime = time
            startLapTime
        } else time - startLapTime//startLapTime - lastEndTime
        startLapTime = time

        val laptimeRecord = LapTimeRecord(elapsedTime = elapsedTime, endTime = time)
        // TODO 화면에 리스트 뿌리기

        //TODO laptime 기록 저장
        recordList.add(laptimeRecord)
        liveRecordList.value = recordList
        CoroutineScope(Dispatchers.IO).launch {
            lapTimeUseCases.addLapTimeUseCase(laptimeRecord)
        }
    }

    fun reset() {
        timerWork?.cancel()
        recordList.clear()
        liveRecordList.value = recordList
        time = 0
        standardLapTime = 0
        startLapTime = 0
        progressPercent = 0
        isPaused = true
        state.setZero()
        liveStateData.value = state
        liveProgressPercent.postValue(progressPercent)

        // TODO laptime 기록 삭제
        CoroutineScope(Dispatchers.IO).launch {
            lapTimeRecordDao.deleteAll()
        }
        // TODO 화면 초기화
    }

    override fun onCleared() {
        super.onCleared()
        timerWork?.cancel()
    }

    fun onStoppedAction() {
        exitTime = time
        // 현재 기록 데이터 저장

        editor.putBoolean("isPaused", isPaused)
        editor.putBoolean("isWorking", isWorking)
        editor.putInt("standardLapTime", standardLapTime)
        editor.putInt("startLapTime", startLapTime)
        editor.putInt("exitTime", exitTime)
        editor.apply()
    }

    fun onDestroyAction() {
        pause()
        onStoppedAction()
    }

    fun initTime() {
        state.hour = time / 360000
        state.minute = time / 6000 % 60
        state.sec = time / 100 % 60    // time/100, 나눗셈의 몫 (초 부분)
        state.milliSec = time % 100    // time%100, 나눗셈의 나머지 (밀리초 부분)
        if (standardLapTime > 0) {
            progressPercent = (time - startLapTime) * 100 / standardLapTime
            liveProgressPercent.postValue(progressPercent)
        }
        liveStateData.postValue(state)
    }

}

