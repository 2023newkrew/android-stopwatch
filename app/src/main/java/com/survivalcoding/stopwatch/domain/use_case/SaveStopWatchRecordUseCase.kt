package com.survivalcoding.stopwatch.domain.use_case

import android.content.SharedPreferences
import com.survivalcoding.stopwatch.domain.model.StopWatchRecord

class SaveStopWatchRecordUseCase(private val sharedPreferences: SharedPreferences) {
    operator fun invoke(stopWatchRecord: StopWatchRecord) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isPaused", stopWatchRecord.isPaused)
        editor.putBoolean("isWorking", stopWatchRecord.isWorking)
        editor.putInt("standardLapTime", stopWatchRecord.standardLapTime)
        editor.putInt("startLapTime", stopWatchRecord.startLapTime)
        editor.putInt("exitTime", stopWatchRecord.exitTime)
        editor.apply()
    }
}