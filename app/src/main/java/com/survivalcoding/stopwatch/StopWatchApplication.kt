package com.survivalcoding.stopwatch

import android.app.Application
import com.survivalcoding.stopwatch.database.StopWatchDatabase
import com.survivalcoding.stopwatch.repository.LabTimeRepository

class StopWatchApplication : Application() {
    
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { StopWatchDatabase.getDatabase(this) }
    val repository by lazy { LabTimeRepository(database.labTimeDao()) }
}