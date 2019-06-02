package com.tstipanic.taskie.persistance.db

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
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

    @Update(onConflict = REPLACE)
    fun updateTask(task: BackendTask)

    @Delete
    fun deleteTask(task: BackendTask)
}
