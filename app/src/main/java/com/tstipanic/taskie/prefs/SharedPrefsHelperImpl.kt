package com.tstipanic.taskie.prefs

import com.tstipanic.taskie.Taskie
import com.tstipanic.taskie.common.KEY_USER_TOKEN
import com.tstipanic.taskie.common.PRIORITY_PREF


class SharedPrefsHelperImpl : SharedPrefsHelper {

    private val preferences = Taskie.instance.providePreferences()

    // API authentication token
    override fun getUserToken(): String = preferences.getString(KEY_USER_TOKEN, "")

    override fun storeUserToken(token: String) = preferences.edit().putString(KEY_USER_TOKEN, token).apply()

    override fun clearUserToken() = preferences.edit().remove(KEY_USER_TOKEN).apply()

    //Priority shared prefs
    override fun storeLastPriority(position: Int) = preferences.edit().putInt(PRIORITY_PREF, position).apply()

    override fun getLastPriority(): Int = preferences.getInt(PRIORITY_PREF, 0)
}

fun provideSharedPrefs(): SharedPrefsHelper {
    return SharedPrefsHelperImpl()
}