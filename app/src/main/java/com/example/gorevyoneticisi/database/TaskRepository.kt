package com.example.gorevyoneticisi.database

class TaskRepository(private val dao: TaskDAO) {
    val subscribers = dao.getAllSubscribers()
    suspend fun insert(task: Task): Long{
        return dao.insertTask(task)
    }

}