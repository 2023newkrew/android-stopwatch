package com.survivalcoding.stopwatch

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPref: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(application)
    }

    private var count = 0

    val countLiveData = MutableLiveData(count)

    init {
        count = sharedPref.getInt("count", 0)
        countLiveData.value = count
    }

    fun increase() {
        count++
        countLiveData.value = count

        with (sharedPref.edit()) {
            putInt("count", count)
            apply()
        }
    }

    fun decrease() {
        count--
        countLiveData.value = count
    }
}