package com.survivalcoding.stopwatch.domain.use_case

import android.content.SharedPreferences
import com.survivalcoding.stopwatch.domain.model.StopWatchRecord

class GetStopWatchRecordUseCase(private val sharedPreferences: SharedPreferences) {
    operator fun invoke(): StopWatchRecord{
        return StopWatchRecord(
            isPaused = sharedPreferences.getBoolean("isPaused", true),
            isWorking = sharedPreferences.getBoolean("isWorking", false),
            standardLapTime = sharedPreferences.getInt("standardLapTime", 0),
            startLapTime = sharedPreferences.getInt("startLapTime", 0),
            exitTime = sharedPreferences.getInt("exitTime",0)
        )
    }
}