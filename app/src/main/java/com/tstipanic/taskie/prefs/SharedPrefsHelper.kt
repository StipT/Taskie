package com.tstipanic.taskie.prefs

interface SharedPrefsHelper {

    fun getUserToken(): String

    fun storeUserToken(token: String)

    fun clearUserToken()

    fun storeLastPriority(position: Int)

    fun getLastPriority(): Int
}