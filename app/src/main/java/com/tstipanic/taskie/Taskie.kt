package com.tstipanic.taskie

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.tstipanic.taskie.common.PREFERENCES_NAME
import com.tstipanic.taskie.di.databaseModule
import com.tstipanic.taskie.di.networkingModule
import com.tstipanic.taskie.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin



class Taskie: Application() {

    companion object {
        lateinit var instance: Taskie
            private set
        fun getAppContext(): Context = instance.applicationContext
    }

    override fun onCreate() {
        instance = this
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        startKoin {
            modules(listOf(presentationModule, networkingModule, databaseModule))
            androidContext(this@Taskie)
        }
    }

    fun providePreferences(): SharedPreferences = instance.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
}