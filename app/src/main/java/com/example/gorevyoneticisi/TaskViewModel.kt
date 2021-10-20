package com.example.gorevyoneticisi

import androidx.databinding.Bindable
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
    val tasks = repository.tasks
    private var isUpdateOrDelete = false
    private lateinit var taskToUpdateOrDelete : Task

    @Bindable // for this check activity_main.xml and ViewModel.inputName
    val inputDescription = MutableLiveData<String>()
    @Bindable
    val inputPeriod = MutableLiveData<String>()
    @Bindable
    val saveOrUpdateButtonText = MutableLiveData<String>()
    @Bindable
    val  clearAllOrDeleteButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()

    val message : LiveData<Event<String>>
        get() = statusMessage

    init {
        saveOrUpdateButtonText.value="Save"
        clearAllOrDeleteButtonText.value="Clear All"
    }

    fun saveOrUpdate(){
        if(isUpdateOrDelete){
            taskToUpdateOrDelete.description = inputDescription.value!!
            taskToUpdateOrDelete.period = inputPeriod.value!!
            update(taskToUpdateOrDelete)
        }
        else{
            val description =inputDescription.value!!
            var period = inputPeriod.value!!
            if (period == "") period = "no time limit"
            insert(Task(0,description,period))
            inputDescription.value = null
            inputPeriod.value = null
        }
    }
    fun clearOrDelete(){
        if(isUpdateOrDelete){
            delete(taskToUpdateOrDelete)
        }
        else{
            clearAll()
        }
    }
    fun insert(task: Task): Job = viewModelScope.launch {
        val newRowId =repository.insert(task)
        if (newRowId>-1){
            statusMessage.value = Event("Task Saved Successfully!! ")
        }else{
            statusMessage.value = Event("Error Occurred!!")
        }
    }
    fun update(task: Task): Job = viewModelScope.launch {
        val noOfRows = repository.update(task)
        if(noOfRows>0) {
            inputDescription.value = null
            inputPeriod.value = null
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("Task Updated Successfully!!")
        }
        else{
            statusMessage.value = Event("Error Occurred!!")
        }

    }
    fun delete(task: Task): Job = viewModelScope.launch {
        repository.delete(task)
        inputDescription.value = null
        inputPeriod.value = null
        isUpdateOrDelete = false
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
        statusMessage.value = Event("Task Deleted Successfully!!")
    }
    fun initUpdateAndDelete(task: Task){
        inputDescription.value = task.description
        inputPeriod.value = task.period
        isUpdateOrDelete = true
        taskToUpdateOrDelete = task
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }
    fun clearAll() : Job =viewModelScope.launch {
        repository.deleteAll()
        statusMessage.value = Event("All Tasks Deleted Successfully!!")
    }
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }
}