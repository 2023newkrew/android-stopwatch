package com.survivalcoding.stopwatch.domain.model

data class DeltaLabTime(
    val labNumber: Int,
    val deltaTime: Long,
    val nowTime: Long
)
