package com.example.gorevyoneticisi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gorevyoneticisi.R
import com.example.gorevyoneticisi.database.Task
import com.example.gorevyoneticisi.databinding.ListItemBinding

class TaskRecyclerViewAdapter (private val taskList: List<Task>,
                               private val clickListener:(Task)->Unit)
    : RecyclerView.Adapter<MyViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : ListItemBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.list_item,parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(taskList[position],clickListener)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}

class MyViewHolder(val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root){
    fun bind (task: Task, clickListener:(Task)->Unit){
        binding.descriptionTextView.text = task.description
        binding.periodTextView.text = task.period
        binding.listItemLayout.setOnClickListener{
            clickListener(task)
        }
    }
}