package com.survivalcoding.stopwatch

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.survivalcoding.stopwatch.Config.Companion.KEY_LOG_ARRAY_LIST
import java.lang.reflect.Type

class PrefsController(val context: Context) {
    fun putLogArrayList(arrayList: ArrayList<Long>) {
        val prefs = context.getSharedPreferences(Config.KEY_PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()

        val json = Gson().toJson(arrayList)
        editor.putString(KEY_LOG_ARRAY_LIST, json)
        editor.apply()
    }

    fun restoreLogArrayList(): ArrayList<Long> {
        val prefs = context.getSharedPreferences(Config.KEY_PREFS, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_LOG_ARRAY_LIST, null)
        return if (json == null) {
            ArrayList()
        } else {
            val type: Type = object : TypeToken<ArrayList<Long>?>() {}.type
            Gson().fromJson(json, type)
        }
    }
}