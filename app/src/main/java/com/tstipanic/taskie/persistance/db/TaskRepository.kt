package com.tstipanic.taskie.persistance.db

import com.tstipanic.taskie.model.data.BackendTask

interface TaskRepository {


    fun getAll(): List<BackendTask>

    fun getTask(id: String): BackendTask

    fun deleteAll()

    fun orderTaskByPriotity(): List<BackendTask>

    fun insertTask(task: BackendTask)

    fun updateTask(id: String, title: String, content: String, priority: Int)

    fun deleteTask(task: BackendTask)
}