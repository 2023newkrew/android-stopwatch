package com.survivalcoding.stopwatch.presentation.util

data class TimeSplit(
    val time: Long
) {
    val milliSecond: Long by lazy { time % 1000 }
    val second: Long by lazy { (time / 1000) % 60 }
    val minute: Long by lazy { (time / (1000 * 60)) % 60 }
    val hour: Long by lazy { (time / (1000 * 60 * 60)) % 24 }
}