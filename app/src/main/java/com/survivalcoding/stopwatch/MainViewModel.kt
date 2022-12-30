package com.survivalcoding.stopwatch

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.survivalcoding.stopwatch.database.AppDatabase
import com.survivalcoding.stopwatch.database.LaptimeRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    //    val db = Room.databaseBuilder(
//        application,
//        AppDatabase::class.java, "laptimerecord"
//    ).build()
    var isPaused = true // TODO 데이터 저장해야함
    var isWorking = false // TODO 데이터 저장해야함
    var standardLapTime = 0 // TODO 데이터 저장해야함
    var exitTime = 0
    private val mPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(application)
    val editor = mPreferences.edit() // 에디터 객체 얻기
    private var time = 0

    init {
        isPaused = mPreferences.getBoolean("isPaused", true)
        isWorking = mPreferences.getBoolean("isWorking", false)
        standardLapTime = mPreferences.getInt("standardLapTime", 0)
        time = mPreferences.getInt("exitTime", 0)
    }

    private var lastEndTime = 0
    val laptimeRecordDao = AppDatabase.getDatabase(application).laptimeRecordDao()

    private var timerWork: Timer? = null
    private var progressPercent = 0
    private var startLapTime = 0
    var recordList: ArrayList<LaptimeRecord> = arrayListOf<LaptimeRecord>()
    private val state = MainUiState()

    val liveStateData = MutableLiveData(state)
    val liveProgressPercent = MutableLiveData(progressPercent)
    val liveRecordList = MutableLiveData(recordList)

    fun getLaptimeRecordList() {
        CoroutineScope(Dispatchers.IO).launch {
            recordList = laptimeRecordDao.getAll() as ArrayList
            liveRecordList.postValue(recordList)
        }
    }

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
        } else startLapTime - lastEndTime
        startLapTime = time

        val laptimeRecord = LaptimeRecord(elapsedTime = elapsedTime, endTime = time)
        // TODO 화면에 리스트 뿌리기

        //TODO laptime 기록 저장
        recordList.add(laptimeRecord)
        liveRecordList.value = recordList
        CoroutineScope(Dispatchers.IO).launch {
            laptimeRecordDao.insert(laptimeRecord)
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
        liveProgressPercent.value = 0

        // TODO laptime 기록 삭제
        CoroutineScope(Dispatchers.IO).launch {
            laptimeRecordDao.deleteAll()
        }
        // TODO 화면 초기화
    }

    fun deleteAll() {
        CoroutineScope(Dispatchers.IO).launch {
            laptimeRecordDao.deleteAll()
            liveRecordList.postValue(recordList)
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerWork?.cancel()
    }

    fun onPausedAction() {
        exitTime = time
        // 현재 기록 데이터 저장
        if (standardLapTime != 0) {
            startLapTime = exitTime
            val elapsedTime = if (lastEndTime == 0) startLapTime else startLapTime - lastEndTime
            CoroutineScope(Dispatchers.IO).launch {
                val laptimeRecord = LaptimeRecord(elapsedTime = elapsedTime, endTime = exitTime)
                laptimeRecordDao.insert(laptimeRecord)
                recordList.add(laptimeRecord)
                liveRecordList.postValue(recordList)
            }
        }
        editor.putBoolean("isPaused", isPaused)
        editor.putBoolean("isWorking", isWorking)
        editor.putInt("standardLapTime", standardLapTime)
        editor.putInt("exitTime", exitTime)
        editor.apply()
    }

    fun onCreateAction() {

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

data class MainUiState(
    var hour: Int = 0,
    var minute: Int = 0,
    var sec: Int = 0,
    var milliSec: Int = 0
) {
    fun setZero() {
        hour = 0
        minute = 0
        sec = 0
        milliSec = 0
    }
}