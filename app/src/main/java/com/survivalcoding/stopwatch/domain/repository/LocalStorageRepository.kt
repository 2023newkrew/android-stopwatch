package com.survivalcoding.stopwatch.domain.repository

interface LocalStorageRepository {
    fun getInt(key: String): Int
    fun getBoolean(key: String, default: Boolean): Boolean
    fun putInt(key: String, value: Int )
    fun putBoolean(key: String, value: Boolean)
}