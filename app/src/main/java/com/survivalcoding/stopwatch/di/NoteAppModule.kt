package com.survivalcoding.stopwatch.di

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.survivalcoding.stopwatch.data.data_source.StopWatchDatabase
import com.survivalcoding.stopwatch.data.repository.LabTimeRepositoryImpl
import com.survivalcoding.stopwatch.domain.repository.LabTimeRepository
import com.survivalcoding.stopwatch.domain.use_case.DeleteAllTimesUseCase
import com.survivalcoding.stopwatch.domain.use_case.GetAllLabTimesUseCase
import com.survivalcoding.stopwatch.domain.use_case.RecordTimeUseCase
import com.survivalcoding.stopwatch.domain.use_case.StopWatchUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NoteAppModule {
    @Provides
    @Singleton
    fun provideStopWatchDatabase(app: Application): StopWatchDatabase {
        return Room.databaseBuilder(
            app,
            StopWatchDatabase::class.java,
            StopWatchDatabase.DB_NAME
        ).build()
    }


    @Provides
    @Singleton
    fun provideLabTimeRepository(stopWatchDatabase: StopWatchDatabase): LabTimeRepository {
        return LabTimeRepositoryImpl(stopWatchDatabase.labTimeDao())
    }

    @Provides
    @Singleton
    fun provideStopWatchUseCases(repository: LabTimeRepository): StopWatchUseCases {
        return StopWatchUseCases(
            getAllLabTimesUseCase = GetAllLabTimesUseCase(repository),
            recordTimeUseCase = RecordTimeUseCase(repository),
            deleteAllTimesUseCase = DeleteAllTimesUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(app)
    }
}