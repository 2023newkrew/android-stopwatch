package com.survivalcoding.stopwatch.domain.usecase

object GetMilliSecStringUseCase {
    operator fun invoke(time: Long): String {
        val split = SplitTimeUseCase(time)
        return String.format("%02d", split.milliSecond / 10)
    }
}