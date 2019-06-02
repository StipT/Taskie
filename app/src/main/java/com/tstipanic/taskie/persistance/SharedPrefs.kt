package com.tstipanic.taskie.persistance

import android.preference.PreferenceManager
import com.tstipanic.taskie.Taskie

object SharedPrefs {
    private const val PRIORITY_KEY = "PRIORITY_KEY"

    private fun sharedPrefs() = PreferenceManager.getDefaultSharedPreferences(Taskie.getAppContext())

    fun store(value: Int) {
        sharedPrefs().edit().putInt(PRIORITY_KEY, value).apply()
    }

    fun getInt() = sharedPrefs().getInt(PRIORITY_KEY, 0)
}