package com.example.gorevyoneticisi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gorevyoneticisi.adapter.TaskRecyclerViewAdapter
import com.example.gorevyoneticisi.database.Task
import com.example.gorevyoneticisi.database.TaskDatabase
import com.example.gorevyoneticisi.database.TaskRepository
import com.example.gorevyoneticisi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var taskViewModel: TaskViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        val dao = TaskDatabase.getInstance(application).taskDAO
        val repository = TaskRepository(dao)
        val factory = TaskViewModelFactory(repository)
        taskViewModel = ViewModelProvider(this,factory).get(TaskViewModel::class.java)

        binding.myViewModel = taskViewModel
        binding.lifecycleOwner = this

        initRecyclerView()

        //This is how you display Messages in MVVM architecture
        taskViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this,it, Toast.LENGTH_LONG).show()
            }
        })
    }

    // observe that live data from the repository
    private fun displayTaskList(){
        taskViewModel.tasks.observe(this, Observer {
            Log.i("myTag",it.toString())
            binding.taskRecyclerView.adapter = TaskRecyclerViewAdapter(it,{selectedItem:Task->listItemClicked(selectedItem)})
        })
    }
    private fun initRecyclerView(){
        binding.taskRecyclerView.layoutManager = LinearLayoutManager(this)
        displayTaskList()
    }
    private fun listItemClicked(task: Task){
        Toast.makeText(this,"selected name is ${task.description}",Toast.LENGTH_LONG).show()
        taskViewModel.initUpdateAndDelete(task)
    }
}