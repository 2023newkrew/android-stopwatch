package com.survivalcoding.stopwatch.domain.usecase

object GetSecStringUseCase {
    operator fun invoke(time: Long): String {
        val split = SplitTimeUseCase(time)
        return if (split.hour > 0) String.format("%d:%02d:%02d", split.hour, split.minute, split.second)
        else if (split.minute > 0) String.format("%02d:%02d", split.minute, split.second)
        else split.second.toString()
    }
}