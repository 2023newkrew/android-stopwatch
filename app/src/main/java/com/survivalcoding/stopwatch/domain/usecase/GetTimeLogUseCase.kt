package com.survivalcoding.stopwatch.domain.usecase

object GetTimeLogUseCase {
    operator fun invoke(index: Int, timeList: ArrayList<Long>): String {
        val time = timeList[index]
        val splitRight = SplitTimeUseCase(time)
        val logRight = String.format("%01d %02d.%02d", splitRight.minute, splitRight.second, splitRight.milliSecond / 10)
        val logLeft =
            if (index == 0) {
                logRight
            } else {
                val gap = time - timeList[index - 1]
                val splitLeft = SplitTimeUseCase(gap)
                String.format("%01d %02d.%02d", splitLeft.minute, splitLeft.second, splitLeft.milliSecond / 10)
            }
        return "# ${String.format("%02d", index + 1)}   $logLeft   $logRight"
    }
}