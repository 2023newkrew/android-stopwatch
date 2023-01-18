package com.survivalcoding.stopwatch.domain.util

import com.survivalcoding.stopwatch.domain.model.DeltaLabTime
import com.survivalcoding.stopwatch.domain.model.LabTime

fun secFormat(sec: Long): String {
    val secText = String.format("%02d", sec % 60)
    val minuteText = if (sec >= 60) String.format(
        "%02d:", (sec / 60) % 60
    ) else ""
    val hourText = if (sec >= 3600) String.format(
        "%02d:", (sec / 3600)
    ) else ""
    return hourText + minuteText + secText
}

fun milSecFormat(milSec: Long) = String.format(
    "%01d:%02d:%02d", (milSec / 1000) / 60, (milSec / 1000) % 60, (milSec % 1000) / 10
)

fun List<LabTime>.toDeltaLabTimes(): List<DeltaLabTime> {
    var preLabTime = 0L
    return this.mapIndexed { index, labTime ->
        val deltaTime = DeltaLabTime(
            labNumber = index + 1, deltaTime = labTime.time - preLabTime, nowTime = labTime.time
        )
        preLabTime = labTime.time
        deltaTime
    }
}
