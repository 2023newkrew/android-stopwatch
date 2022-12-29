package com.survivalcoding.stopwatch.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LabTime(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val time: Long
)