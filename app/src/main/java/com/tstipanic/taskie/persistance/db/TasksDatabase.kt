package com.tstipanic.taskie.persistance.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tstipanic.taskie.model.data.Task

@Database(entities = [Task::class], version = 1)
abstract class TasksDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskieDao

}