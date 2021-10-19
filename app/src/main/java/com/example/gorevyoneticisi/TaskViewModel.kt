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
    val inputName = MutableLiveData<String>()
    @Bindable
    val inputEmail = MutableLiveData<String>()
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
            taskToUpdateOrDelete.description = inputName.value!!
            taskToUpdateOrDelete.period = inputEmail.value!!
            update(taskToUpdateOrDelete)
        }
        else{
            val description =inputName.value!!
            var period = inputEmail.value!!
            if (period == "") period = "no time limit"
            insert(Task(0,description,period))
            inputName.value = null
            inputEmail.value = null
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
            statusMessage.value = Event("Task Inserted Successfully!! $newRowId ")
        }else{
            statusMessage.value = Event("Error Occurred!!")
        }

    }
    fun update(task: Task): Job = viewModelScope.launch {
        val noOfRows = repository.update(task)
        if(noOfRows>0) {
            inputName.value = null
            inputEmail.value = null
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
        inputName.value = null
        inputEmail.value = null
        isUpdateOrDelete = false
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
        statusMessage.value = Event("Task Deleted Successfully!!")

    }
    fun initUpdateAndDelete(task: Task){
        inputName.value = task.description
        inputEmail.value = task.period
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