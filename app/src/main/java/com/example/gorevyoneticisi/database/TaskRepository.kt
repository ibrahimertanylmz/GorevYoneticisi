package com.example.gorevyoneticisi.database

class TaskRepository(private val dao: TaskDAO) {
    val tasks = dao.getAllTasks()
    suspend fun insert(task: Task): Long{
        return dao.insertTask(task)
    }
    suspend fun update(task: Task): Int{
        return dao.updateTask(task)
    }
    suspend fun delete(task: Task){
        dao.deleteTask(task)
    }
    suspend fun deleteAll(){
        dao.deleteAll()
    }
}