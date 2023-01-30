package com.survivalcoding.stopwatch.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.survivalcoding.stopwatch.data.data_source.database.LapTimeDatabase
import com.survivalcoding.stopwatch.data.repository.LocalStorageRepositoryImpl
import com.survivalcoding.stopwatch.data.repository.StopWatchRepositoryImpl
import com.survivalcoding.stopwatch.domain.repository.LocalStorageRepository
import com.survivalcoding.stopwatch.domain.repository.StopWatchRepository
import com.survivalcoding.stopwatch.domain.use_case.*
import com.survivalcoding.stopwatch.domain.use_case.bundle.LapTimeUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun provideSharedPreference
        (@ApplicationContext context: Context): SharedPreferences{
        return context.getSharedPreferences("stop_watch_shared_pref", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideLocalStorageRepository(sharedPreferences: SharedPreferences):LocalStorageRepository{
        return LocalStorageRepositoryImpl(sharedPreferences)
    }


    @Provides
    @Singleton
    fun provideGetStopWatchRecordUseCase(localStorageRepository: LocalStorageRepository): GetStopWatchStateUseCase{
        return GetStopWatchStateUseCase(localStorageRepository)
    }

    @Provides
    @Singleton
    fun provideSaveStopWatchRecordUseCase(localStorageRepository: LocalStorageRepository): SaveStopWatchStateUseCase {
        return SaveStopWatchStateUseCase(localStorageRepository)
    }

    @Provides
    @Singleton
    fun provideGetStopWatchTimeUseCase(localStorageRepository: LocalStorageRepository): GetStopWatchTimeUseCase{
        return GetStopWatchTimeUseCase(localStorageRepository)
    }

    @Provides
    @Singleton
    fun provideSaveStopWatchTimeUseCase(localStorageRepository: LocalStorageRepository): SaveStopWatchTimeUseCase{
        return SaveStopWatchTimeUseCase(localStorageRepository)
    }

}

