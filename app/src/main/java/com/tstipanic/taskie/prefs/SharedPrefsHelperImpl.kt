package com.tstipanic.taskie.prefs

import com.tstipanic.taskie.Taskie
import com.tstipanic.taskie.common.KEY_USER_TOKEN


class SharedPrefsHelperImpl : SharedPrefsHelper {

    private val preferences = Taskie.instance.providePreferences()

    // API authentication token
    override fun getUserToken(): String = preferences.getString(KEY_USER_TOKEN, "")

    override fun storeUserToken(token: String) = preferences.edit().putString(KEY_USER_TOKEN, token).apply()

    override fun clearUserToken() = preferences.edit().remove(KEY_USER_TOKEN).apply()
}

fun provideSharedPrefs(): SharedPrefsHelper {
    return SharedPrefsHelperImpl()
}