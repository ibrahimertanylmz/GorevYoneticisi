package com.example.gorevyoneticisi

import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gorevyoneticisi.database.Task
import com.example.gorevyoneticisi.database.TaskRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TaskViewModel (private val repository: TaskRepository): ViewModel(), Observable {
    val subscribers = repository.subscribers

    private val statusMessage = MutableLiveData<Event<String>>()

    val message : LiveData<Event<String>>
        get() = statusMessage

    fun insert(task: Task): Job = viewModelScope.launch {
        val newRowId =repository.insert(task)
        if (newRowId>-1){
            statusMessage.value = Event("Subscriber Inserted Successfully!! $newRowId ")
        }else{
            statusMessage.value = Event("Error Occurred!!")
        }

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }
}