package com.survivalcoding.stopwatch.data.stopwatch.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.survivalcoding.stopwatch.domain.stopwatch.repository.StopWatchStateRepository
import com.survivalcoding.stopwatch.presentation.stopwatch.state.StopWatchState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class StopWatchStateRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : StopWatchStateRepository {
    companion object {
        val IS_PAUSED_KEY = booleanPreferencesKey("IS_PAUSED")
        val IS_WORKING_KEY = booleanPreferencesKey("IS_WORKING")
        val STANDARD_LAP_TIME_KEY = intPreferencesKey("STANDARD_LAP_TIME")
        val START_LAP_TIME_KEY = intPreferencesKey("START_LAP_TIME")
        val EXIT_TIME_KEY = intPreferencesKey("EXIT_TIME")
    }

    override suspend fun saveStopWatchState(stopWatchState: StopWatchState) {
        dataStore.edit {
            it[IS_PAUSED_KEY] = stopWatchState.isPaused
            it[IS_WORKING_KEY] = stopWatchState.isWorking
            it[STANDARD_LAP_TIME_KEY] = stopWatchState.standardLapTime
            it[START_LAP_TIME_KEY] = stopWatchState.startLapTime
            it[EXIT_TIME_KEY] = stopWatchState.exitTime
        }
    }

    override fun isPausedFlow(): Flow<Boolean?> = dataStore.data.map {
        it[IS_PAUSED_KEY]
    }

    override fun isWorkingFlow(): Flow<Boolean?> = dataStore.data.map {
        it[IS_WORKING_KEY]
    }

    override fun standardLapTime(): Flow<Int?> = dataStore.data.map {
        it[STANDARD_LAP_TIME_KEY]
    }

    override fun startLapTime(): Flow<Int?> = dataStore.data.map {
        it[START_LAP_TIME_KEY]
    }

    override fun exitTime(): Flow<Int?> = dataStore.data.map {
        it[EXIT_TIME_KEY]
    }
}