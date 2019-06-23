package com.tstipanic.taskie.persistance.db

import com.tstipanic.taskie.model.data.Task

interface TaskieRepo {


    fun getAll(): List<Task>

    fun getTask(id: String): Task

    fun getUnsentTasks(): List<Task>

    fun markAsDeleted(id: String)

    fun getMarkedAsDeletedList(): List<Task>

    fun deleteAll()

    fun orderTaskByPriotity(): List<Task>

    fun insertTask(task: Task)

    fun updateTask(id: String, title: String, content: String, priority: Int)

    fun deleteTask(task: Task)
}