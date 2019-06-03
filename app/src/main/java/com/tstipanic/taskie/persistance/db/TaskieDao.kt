package com.tstipanic.taskie.persistance.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.tstipanic.taskie.model.data.BackendTask

@Dao
interface TaskieDao {
    @Query("SELECT * FROM BackendTask")
    fun getAll(): List<BackendTask>

    @Query("DELETE FROM BackendTask")
    fun deleteAll()

    @Query("SELECT * FROM BackendTask ORDER BY taskPriority DESC")
    fun orderTaskByPriotity(): List<BackendTask>

    @Insert(onConflict = IGNORE)
    fun insertTask(task: BackendTask)

    @Insert(onConflict = REPLACE)
    fun storeAll(list: List<BackendTask>)

    @Query("SELECT * FROM BackendTask WHERE id = :id")
    fun getTask(id: String): BackendTask

    @Query("UPDATE BackendTask SET title = :title, content = :content, taskPriority = :priority WHERE id = :id")
    fun updateTask(id: String, title: String, content: String, priority: Int)

    @Delete
    fun deleteTask(task: BackendTask)
}
