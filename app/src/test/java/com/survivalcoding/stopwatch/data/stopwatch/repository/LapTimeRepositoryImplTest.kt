package com.survivalcoding.stopwatch.data.stopwatch.repository

import com.survivalcoding.stopwatch.data.stopwatch.data_source.dao.LapTimeRecordDao
import com.survivalcoding.stopwatch.domain.stopwatch.model.LapTimeRecord
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LapTimeRepositoryImplTest {

    @Mock
    lateinit var lapTimeRecordDao: LapTimeRecordDao

    @InjectMocks
    lateinit var lapTimeRepositoryImpl: LapTimeRepositoryImpl

    private fun <T> any(): T {
        Mockito.any<T>()
        return null as T
    }


    private var lapTimeRecords = arrayListOf<LapTimeRecord>(
        LapTimeRecord(
            0, 111, 111
        ),
        LapTimeRecord(
            1, 222, 333
        ),
        LapTimeRecord(
            2, 333, 666
        )
    )

    @Before
    fun setUp() {
        // given
        `when`(lapTimeRecordDao.getAll()).thenReturn(
            flow {
                emit(lapTimeRecords)
            }
        )
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `모든 랩타임 기록 가져오기`() = runTest {
        val result = lapTimeRepositoryImpl.getLapTimes().first()
        assertEquals(lapTimeRecords, result)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `특정 랩타입 기록 삽입`() = runTest {
        val lapTimeRecord: LapTimeRecord = LapTimeRecord(
            elapsedTime = 999,
            endTime = 999
        )
        `when`(lapTimeRecordDao.insert(any())).then {
            lapTimeRecords.add(lapTimeRecord)
        }
        lapTimeRepositoryImpl.insertLapTime(lapTimeRecord)
        verify(lapTimeRecordDao).insert(lapTimeRecord)
        val result = lapTimeRepositoryImpl.getLapTimes().first()
        assertEquals(lapTimeRecord, result[3])
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `특정 랩타입 기록 삭제`() = runTest {
        val result = lapTimeRepositoryImpl.getLapTimes().first()
        val beforeSize = result.size
        val lapTimeRecord = result[2]
        `when`(lapTimeRecordDao.delete(any())).then {
            lapTimeRecords.remove(lapTimeRecord)
        }
        lapTimeRepositoryImpl.deleteLapTime(lapTimeRecord)
        verify(lapTimeRecordDao).delete(lapTimeRecord)
        val afterSize = lapTimeRepositoryImpl.getLapTimes().first().size
        assertEquals(beforeSize - 1, afterSize)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `모든 랩타임 기록 삭제`() = runTest {
        `when`(lapTimeRecordDao.deleteAll()).then {
            lapTimeRecords.clear()
        }
        lapTimeRepositoryImpl.deleteAllLapTimes()
        verify(lapTimeRecordDao).deleteAll()
        val afterSize = lapTimeRepositoryImpl.getLapTimes().first().size
        assertEquals(0, afterSize)
    }
}