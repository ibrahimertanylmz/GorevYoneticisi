package com.example.gorevyoneticisi.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class],version = 1)
abstract class TaskDatabase : RoomDatabase(){
    abstract val taskDAO: TaskDAO

    //Instance usage for Singleton Principle (CAN BE USED FOR ALL PROJECTS)
    companion object{
        //Volatile annotation makes the field visible to other fields
        @Volatile
        private var INSTANCE : TaskDatabase ?= null
        fun getInstance (context: Context):TaskDatabase{
            synchronized(this){
                var instance: TaskDatabase? = INSTANCE
                if (instance == null){
                    instance= Room.databaseBuilder(
                        context.applicationContext,
                        TaskDatabase::class.java,
                        "task_data_database").build() }
                return instance
            }
        }
    }
}