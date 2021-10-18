package com.example.gorevyoneticisi.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDAO {
    @Insert
    suspend fun insertTask(task: Task): Long

    //no suspend modifier because uses LiveData
    @Query("SELECT * FROM task_data_table")
    fun getAllSubscribers(): LiveData<List<Task>>
}