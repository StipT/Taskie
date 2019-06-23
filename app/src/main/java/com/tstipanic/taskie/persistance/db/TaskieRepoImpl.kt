package com.tstipanic.taskie.persistance.db

import com.tstipanic.taskie.model.data.Task

class TaskieRepoImpl(db: TasksDatabase) : TaskieRepo {

    private var tasksDao: TaskieDao = db.taskDao()

    override fun getAll(): List<Task> = tasksDao.getAll()

    override fun getTask(id: String): Task = tasksDao.getTask(id)

    override fun orderTaskByPriotity(): List<Task> = tasksDao.orderTaskByPriotity()

    override fun getUnsentTasks(): List<Task> = tasksDao.getUnsentTasks()

    override fun getMarkedAsDeletedList(): List<Task> = tasksDao.getMarkedAsDeletedList()

    override fun deleteAll() = tasksDao.deleteAll()

    override fun markAsDeleted(id: String) = tasksDao.markAsDeleted(id)

    override fun insertTask(task: Task) = tasksDao.insertTask(task)

    override fun updateTask(id: String, title: String, content: String, priority: Int) =
        tasksDao.updateTask(id, title, content, priority)

    override fun deleteTask(task: Task) = tasksDao.deleteTask(task)
}