package com.survivalcoding.stopwatch.data.repository

import androidx.annotation.WorkerThread
import com.survivalcoding.stopwatch.data.data_source.LabTimeDao
import com.survivalcoding.stopwatch.domain.model.LabTime
import com.survivalcoding.stopwatch.domain.repository.LabTimeRepository
import kotlinx.coroutines.flow.Flow

class LabTimeRepositoryImpl(private val labTimeDao: LabTimeDao) : LabTimeRepository {

    override val allLabTimes: Flow<List<LabTime>> = labTimeDao.getAll()

    @WorkerThread
    override suspend fun insert(labTime: LabTime) {
        labTimeDao.insert(labTime)
    }

    @WorkerThread
    override suspend fun deleteAll() {
        labTimeDao.deleteAll()
    }
}