package com.example.gorevyoneticisi

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var taskViewModel: TaskViewModel
    @SuppressLint("SetTextI18n")
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

        val calendar= Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)


        binding.btnSetPeriod.setOnClickListener{
            val datePickerDialog = DatePickerDialog(this@MainActivity, DatePickerDialog.OnDateSetListener
            { view, year, monthOfYear, dayOfMonth ->
                binding.edtDate.setText("" + dayOfMonth + "." + (monthOfYear+1) + "." + year)
            }, year, month, day)
            datePickerDialog.show()
        }


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
    private fun onSetPeriodClick(){

    }
    private fun listItemClicked(task: Task){
        Toast.makeText(this,"selected name is ${task.description}",Toast.LENGTH_LONG).show()
        taskViewModel.initUpdateAndDelete(task)
    }
}