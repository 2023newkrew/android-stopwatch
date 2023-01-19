package com.survivalcoding.stopwatch.domain.stopwatch.use_case.bundle

import com.survivalcoding.stopwatch.domain.stopwatch.use_case.DeleteAllLapTimesUseCase
import com.survivalcoding.stopwatch.domain.stopwatch.use_case.DeleteLapTimeUseCase
import com.survivalcoding.stopwatch.domain.stopwatch.use_case.GetLapTimesUseCase
import com.survivalcoding.stopwatch.domain.stopwatch.use_case.InsertLapTimeUseCase

data class LapTimeRecordUseCaseBundle(
    val getLapTimesUseCase: GetLapTimesUseCase,
    val insertLapTimeUseCase: InsertLapTimeUseCase,
    val deleteLapTimeUseCase: DeleteLapTimeUseCase,
    val deleteAllLapTimesUseCase: DeleteAllLapTimesUseCase
)