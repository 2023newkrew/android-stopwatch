package com.survivalcoding.stopwatch.domain.usecase

object GetLastTimeLogUseCase {
    operator fun invoke(lastTime: Long, timeList: ArrayList<Long>): String {
        val index = timeList.size
        val splitRight = SplitTimeUseCase(lastTime)
        val logRight = String.format("%01d %02d.%02d", splitRight.minute, splitRight.second, splitRight.milliSecond / 10)
        val logLeft =
            if (index == 1) {
                logRight
            } else {
                val gap = lastTime - timeList[index - 2]
                val splitLeft = SplitTimeUseCase(gap)
                String.format("%01d %02d.%02d", splitLeft.minute, splitLeft.second, splitLeft.milliSecond / 10)
            }
        return "# ${String.format("%02d", index)}   $logLeft   $logRight"
    }
}