package com.tstipanic.taskie.di

import androidx.room.Room
import com.tstipanic.taskie.persistance.db.TaskieRepo
import com.tstipanic.taskie.persistance.db.TaskieRepoImpl
import com.tstipanic.taskie.persistance.db.TasksDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(androidApplication(), TasksDatabase::class.java, "TasksDb")
            .allowMainThreadQueries().build()
    }

    factory<TaskieRepo> { TaskieRepoImpl(get()) }
}