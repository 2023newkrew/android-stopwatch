package com.survivalcoding.stopwatch.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LaptimeRecord(
    @PrimaryKey(autoGenerate = true) var rid: Int = 0,
    @ColumnInfo(name = "elapsed_time") val elapsedTime: Int,
    @ColumnInfo(name = "end_time") val endTime: Int,
)