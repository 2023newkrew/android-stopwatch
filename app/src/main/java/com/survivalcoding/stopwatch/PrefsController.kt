package com.survivalcoding.stopwatch

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class PrefsController(private val context: Context) {
    fun putLogArrayList(logList: List<Long>) {
        val prefs = context.getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val json = Gson().toJson(logList)
        editor.putString(KEY_LOG_ARRAY_LIST, json)
        editor.apply()
    }

    fun restoreLogArrayList(): List<Long> {
        val prefs = context.getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_LOG_ARRAY_LIST, null)
        return if (json == null) {
            listOf()
        } else {
            val type: Type = object : TypeToken<List<Long>?>() {}.type
            Gson().fromJson(json, type)
        }
    }
}