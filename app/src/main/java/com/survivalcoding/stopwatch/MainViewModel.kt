package com.survivalcoding.stopwatch

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
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
    private var lastEndTime = 0

    val laptimeRecordDao = AppDatabase.getDatabase(application).laptimeRecordDao()
    var isPaused = true // TODO 데이터 저장해야함
    var isWorking = false // TODO 데이터 저장해야함
    private var timerWork: Timer? = null
    private var time = 0
    var standardLapTime = 0 // TODO 데이터 저장해야함
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

    fun lapTime(recordList: ArrayList<LaptimeRecord>) {
        val elapsedTime = if (standardLapTime == 0) {
            standardLapTime = time
            startLapTime
        } else startLapTime - lastEndTime
        startLapTime = time

        val laptimeRecord = LaptimeRecord(elapsedTime = elapsedTime, endTime = time)
        println("레코드 id: ${laptimeRecord.rid}")
        // TODO 화면에 리스트 뿌리기

        //TODO laptime 기록 저장
        recordList.add(laptimeRecord)
        CoroutineScope(Dispatchers.IO).launch {
            laptimeRecordDao.insert(laptimeRecord)
            liveRecordList.postValue(recordList)
        }
    }

    fun reset() {
        timerWork?.cancel()
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
            liveRecordList.postValue(recordList)
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
        // 현재 기록 데이터 저장
        if (standardLapTime != 0) {
            startLapTime = time
            val elapsedTime = if (lastEndTime == 0) startLapTime else startLapTime - lastEndTime
            CoroutineScope(Dispatchers.IO).launch {
                val laptimeRecord = LaptimeRecord(elapsedTime = elapsedTime, endTime = time)
                laptimeRecordDao.insert(laptimeRecord)
                recordList.add(laptimeRecord)
                liveRecordList.postValue(recordList)
            }
        }
    }

    fun initTime(lastTime: Int) {
        time = lastTime
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