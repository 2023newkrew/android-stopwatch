package com.survivalcoding.stopwatch.domain.use_case

data class StopWatchUseCases(
    val getAllLabTimesUseCase: GetAllLabTimesUseCase,
    val recordTimeUseCase: RecordTimeUseCase,
    val deleteAllTimesUseCase: DeleteAllTimesUseCase
)