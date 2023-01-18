package com.survivalcoding.stopwatch.domain.use_case.bundle

import com.survivalcoding.stopwatch.domain.use_case.AddLapTimeUseCase
import com.survivalcoding.stopwatch.domain.use_case.GetAllLapTimesUseCase
import com.survivalcoding.stopwatch.domain.use_case.ResetLapTimesUseCase

data class LapTimeUseCases(
    val addLapTimeUseCase: AddLapTimeUseCase,
    val getAllLapTimesUseCase: GetAllLapTimesUseCase,
    val resetLapTimesUseCase: ResetLapTimesUseCase
    )