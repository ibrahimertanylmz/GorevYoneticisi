package com.example.gorevyoneticisi.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDAO {
    @Insert
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task): Int

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM task_data_table")
    suspend fun deleteAll()

    //no suspend modifier because uses LiveData
    @Query("SELECT * FROM task_data_table")
    fun getAllTasks(): LiveData<List<Task>>
}