package com.tstipanic.taskie.persistance.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tstipanic.taskie.model.data.BackendTask

@Database(entities = [BackendTask::class], version = 1)
@TypeConverters(TypeConverter::class)
abstract class DaoProvider : RoomDatabase() {

    abstract fun taskDao(): TaskieDao

    companion object {
        private var instance: DaoProvider? = null
        fun getInstance(context: Context): DaoProvider {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    DaoProvider::class.java,
                    "TasksDb"
                ).allowMainThreadQueries().build()
            }
            return instance as DaoProvider
        }

    }

}