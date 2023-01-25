package com.survivalcoding.stopwatch.data.stopwatch.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesOf
import com.survivalcoding.stopwatch.presentation.stopwatch.state.StopWatchState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StopWatchStateRepositoryImplTest {

    @Mock
    private lateinit var dataStore: DataStore<Preferences>

    private lateinit var stopWatchStateRepositoryImpl: StopWatchStateRepositoryImpl

    private val stopWatchState = StopWatchState(
        isPaused = true,
        isWorking = false,
        standardLapTime = 5000,
        startLapTime = 1000,
        exitTime = 3000
    )

    private val preferences = preferencesOf(
        StopWatchStateRepositoryImpl.IS_PAUSED_KEY to stopWatchState.isPaused,
        StopWatchStateRepositoryImpl.IS_WORKING_KEY to stopWatchState.isWorking,
        StopWatchStateRepositoryImpl.START_LAP_TIME_KEY to stopWatchState.startLapTime,
        StopWatchStateRepositoryImpl.STANDARD_LAP_TIME_KEY to stopWatchState.standardLapTime,
        StopWatchStateRepositoryImpl.EXIT_TIME_KEY to stopWatchState.exitTime,
    )

    private fun <T> any(): T {
        Mockito.any<T>()
        return null as T
    }

    @Before
    fun setUp() {
        stopWatchStateRepositoryImpl = StopWatchStateRepositoryImpl(dataStore)
        runBlocking {
            `when`(dataStore.data).thenReturn(flowOf(preferences))
            stopWatchStateRepositoryImpl.saveStopWatchState(stopWatchState)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun saveStopWatchState() = runTest {
//        `when`(dataStore.edit { any() }).then {
//            val block = it.arguments[0] as (Preferences) -> Unit
//            block(preferences)
//        }
        // TODO: 구현해야 함
        //  * 어떠한 방법으로 edit을 mocking하여 테스트 해야 할 지 잘 모르겠음..
        //  * verify(dataStore).edit {} 내부에 assert는 오류 남
        //  * `when`(dataStore.edit { any() }).then 이용하여 건드려야 할 것 같으나, 참고 자료가 없다..
        verify(dataStore).edit(any())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `isPausedFlow는 DataStore로 부터 isPaused를 반환한다`() = runTest {
        assertEquals(stopWatchState.isPaused, stopWatchStateRepositoryImpl.isPausedFlow().first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `isWorkingFlow는 DataStore로 부터 isWorking을 반환한다`() = runTest {
        assertEquals(stopWatchState.isWorking, stopWatchStateRepositoryImpl.isWorkingFlow().first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `standardLapTime는 DataStore로 부터 standardLapTime을 반환한다`() = runTest {
        assertEquals(
            stopWatchState.standardLapTime,
            stopWatchStateRepositoryImpl.standardLapTime().first()
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `startLapTime는 DataStore로 부터 startLapTime을 반환한다`() = runTest {
        assertEquals(
            stopWatchState.startLapTime,
            stopWatchStateRepositoryImpl.startLapTime().first()
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `exitTime는 DataStore로 부터 exitTime는을 반환한다`() = runTest {
        assertEquals(stopWatchState.exitTime, stopWatchStateRepositoryImpl.exitTime().first())
    }
}

