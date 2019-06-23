package com.tstipanic.taskie.persistance.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.tstipanic.taskie.model.data.Task

@Dao
interface TaskieDao {
    @Query("SELECT * FROM Task WHERE isDeleted = 0")
    fun getAll(): List<Task>

    @Query("DELETE  FROM Task")
    fun deleteAll()

    @Query("UPDATE Task SET isDeleted = 1 WHERE id = :id")
    fun markAsDeleted(id: String)

    @Query("SELECT * FROM Task WHERE isDeleted = 1")
    fun getMarkedAsDeletedList(): List<Task>

    @Query("SELECT * FROM Task ORDER BY taskPriority DESC")
    fun orderTaskByPriotity(): List<Task>

    @Query("SELECT * FROM Task WHERE isSent == 0")
    fun getUnsentTasks(): List<Task>

    @Insert(onConflict = REPLACE)
    fun insertTask(task: Task)

    @Insert(onConflict = REPLACE)
    fun storeAll(list: List<Task>)

    @Query("SELECT * FROM Task WHERE id = :id")
    fun getTask(id: String): Task

    @Query("UPDATE Task SET title = :title, content = :content, taskPriority = :priority WHERE id = :id")
    fun updateTask(id: String, title: String, content: String, priority: Int)

    @Delete
    fun deleteTask(task: Task)
}
