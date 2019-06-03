package com.tstipanic.taskie.persistance.db

import com.tstipanic.taskie.Taskie
import com.tstipanic.taskie.model.data.BackendTask

class TaskRoomRepository : TaskRepository {

    private var db: DaoProvider = DaoProvider.getInstance(Taskie.getAppContext())

    private var taskDao: TaskieDao = db.taskDao()


    override fun getAll(): List<BackendTask> = taskDao.getAll()

    override fun getTask(id: String): BackendTask = taskDao.getTask(id)

    override fun orderTaskByPriotity(): List<BackendTask> = taskDao.orderTaskByPriotity()

    override fun deleteAll() = taskDao.deleteAll()

    override fun insertTask(task: BackendTask) = taskDao.insertTask(task)

    override fun updateTask(id: String, title: String, content: String, priority: Int) =
        taskDao.updateTask(id, title, content, priority)

    override fun deleteTask(task: BackendTask) = taskDao.deleteTask(task)
}