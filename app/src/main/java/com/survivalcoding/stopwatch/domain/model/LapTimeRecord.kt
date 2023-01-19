package com.survivalcoding.stopwatch.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.intellij.lang.annotations.Flow

@Entity
data class LapTimeRecord(
    @PrimaryKey(autoGenerate = true) var rid: Int = 0,
    val elapsedTime: Int,
    val endTime: Int,
)