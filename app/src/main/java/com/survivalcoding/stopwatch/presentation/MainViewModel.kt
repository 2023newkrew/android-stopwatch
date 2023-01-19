package com.survivalcoding.stopwatch.presentation

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val application: Application,
    savedState: SavedStateHandle
) : ViewModel() {
    private val mPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(application)
    private val editor: SharedPreferences.Editor = mPreferences.edit()

    fun saveCurrentTab(selectedItemId: Int) {
        editor.putInt("selected_tab", selectedItemId)
        editor.apply()
    }

    fun getLastSavedTab(defaultTabId: Int): Int {
        return mPreferences.getInt("selected_tab", defaultTabId)
    }
}