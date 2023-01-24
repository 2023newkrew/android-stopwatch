package com.survivalcoding.stopwatch.domain.usecase

import com.survivalcoding.stopwatch.domain.model.TimeSplit

object SplitTimeUseCase {
    operator fun invoke(time: Long) = TimeSplit(
        time % 1000,
        (time / 1000) % 60,
        (time / (1000 * 60)) % 60,
        (time / (1000 * 60 * 60)) % 24
    )
}