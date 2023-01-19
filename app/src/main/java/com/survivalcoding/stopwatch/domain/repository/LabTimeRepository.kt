package com.survivalcoding.stopwatch.domain.repository

import androidx.annotation.WorkerThread
import com.survivalcoding.stopwatch.domain.model.LabTime
import kotlinx.coroutines.flow.Flow

interface LabTimeRepository {

    val allLabTimes: Flow<List<LabTime>>

    @WorkerThread
    suspend fun insert(labTime: LabTime)

    @WorkerThread
    suspend fun deleteAll()
}