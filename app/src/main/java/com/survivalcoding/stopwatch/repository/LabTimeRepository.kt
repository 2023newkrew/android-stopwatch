package com.survivalcoding.stopwatch.repository

import androidx.annotation.WorkerThread
import com.survivalcoding.stopwatch.dao.LabTimeDao
import com.survivalcoding.stopwatch.entity.LabTime
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class LabTimeRepository(private val labTimeDao: LabTimeDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allLabTimes: Flow<List<LabTime>> = labTimeDao.getAll()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @WorkerThread
    suspend fun insert(labTime: LabTime) {
        labTimeDao.insert(labTime)
    }

    @WorkerThread
    suspend fun deleteAll() {
        labTimeDao.deleteAll()
    }
}