package com.survivalcoding.stopwatch

import android.app.Application
import androidx.room.Room
import com.survivalcoding.stopwatch.data.database.LapTimeDatabase
import com.survivalcoding.stopwatch.data.repository.StopWatchRepositoryImpl
import com.survivalcoding.stopwatch.domain.repository.StopWatchRepository
import com.survivalcoding.stopwatch.domain.use_case.AddLapTimeUseCase
import com.survivalcoding.stopwatch.domain.use_case.GetAllLapTimesUseCase
import com.survivalcoding.stopwatch.domain.use_case.ResetLapTimesUseCase
import com.survivalcoding.stopwatch.domain.use_case.bundle.LapTimeUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


const val DATABASE_NAME = "lap_time_db"


@Module
@InstallIn(SingletonComponent::class)
object StopWatchAppModule {
    @Provides
    @Singleton
    fun provideLapTimeDatabase(application: Application): LapTimeDatabase {
        return Room.databaseBuilder(
            application,
            LapTimeDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideStopWatchRepository(lapTimeDatabase: LapTimeDatabase): StopWatchRepository {
        return StopWatchRepositoryImpl(lapTimeDatabase.lapTimeRecordDao)
    }

    @Provides
    @Singleton
    fun provideUseCases(stopWatchRepository: StopWatchRepository): LapTimeUseCases {
        return LapTimeUseCases(
            addLapTimeUseCase = AddLapTimeUseCase(stopWatchRepository),
            getAllLapTimesUseCase = GetAllLapTimesUseCase(stopWatchRepository),
            resetLapTimesUseCase = ResetLapTimesUseCase(stopWatchRepository)
        )
    }


}

