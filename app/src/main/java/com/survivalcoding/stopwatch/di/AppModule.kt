package com.survivalcoding.stopwatch.di

import android.content.Context
import com.survivalcoding.stopwatch.data.stopwatch.data_source.dao.LapTimeRecordDao
import com.survivalcoding.stopwatch.data.stopwatch.data_source.database.AppDatabase
import com.survivalcoding.stopwatch.data.stopwatch.repository.LapTimeRepositoryImpl
import com.survivalcoding.stopwatch.domain.stopwatch.repository.LapTimeRepository
import com.survivalcoding.stopwatch.domain.stopwatch.use_case.DeleteAllLapTimesUseCase
import com.survivalcoding.stopwatch.domain.stopwatch.use_case.DeleteLapTimeUseCase
import com.survivalcoding.stopwatch.domain.stopwatch.use_case.GetLapTimesUseCase
import com.survivalcoding.stopwatch.domain.stopwatch.use_case.InsertLapTimeUseCase
import com.survivalcoding.stopwatch.domain.stopwatch.use_case.bundle.LapTimeRecordUseCaseBundle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideLapTimeRecordDao(appDatabase: AppDatabase): LapTimeRecordDao {
        return appDatabase.lapTimeRecordDao()
    }

    @Provides
    @Singleton
    fun provideLapTimeRecordRepository(lapTimeRecordDao: LapTimeRecordDao): LapTimeRepository {
        return LapTimeRepositoryImpl(lapTimeRecordDao)
    }

    @Provides
    @Singleton
    fun provideLapTimeRecordUseCaseBundle(lapTimeRepository: LapTimeRepository): LapTimeRecordUseCaseBundle {
        return LapTimeRecordUseCaseBundle(
            GetLapTimesUseCase(lapTimeRepository),
            InsertLapTimeUseCase(lapTimeRepository),
            DeleteLapTimeUseCase(lapTimeRepository),
            DeleteAllLapTimesUseCase(lapTimeRepository)
        )
    }
}