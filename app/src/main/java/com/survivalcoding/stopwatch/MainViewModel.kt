package com.survivalcoding.stopwatch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private var count = 0

    val countLiveData = MutableLiveData(count)

    fun increase() {
        count++
        countLiveData.value = count
    }

    fun decrease() {
        count--
        countLiveData.value = count
    }
}