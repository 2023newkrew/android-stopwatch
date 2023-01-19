package com.survivalcoding.stopwatch.data.repository

import android.content.SharedPreferences
import com.survivalcoding.stopwatch.domain.repository.LocalStorageRepository
import javax.inject.Inject

class LocalStorageRepositoryImpl
    @Inject constructor(private val sharedPreferences: SharedPreferences): LocalStorageRepository {

    private val editor = sharedPreferences.edit()

    override fun getInt(key: String): Int {
        return sharedPreferences.getInt(key,0)
    }

    override fun getBoolean(key: String, default: Boolean): Boolean {
        return sharedPreferences.getBoolean(key,default)
    }

    override fun putInt(key: String, value: Int) {
        editor.putInt(key,value)
        editor.apply()
    }

    override fun putBoolean(key: String, value: Boolean) {
        editor.putBoolean(key,value)
        editor.apply()
    }
}