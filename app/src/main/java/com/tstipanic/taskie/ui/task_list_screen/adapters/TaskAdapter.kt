package com.tstipanic.taskie.ui.task_list_screen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tstipanic.taskie.R
import com.tstipanic.taskie.model.data.Task

class TaskAdapter(private val onItemSelected: (Task) -> Unit) : RecyclerView.Adapter<TaskHolder>() {

    private val data: MutableList<Task> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskHolder(v)
    }


    fun getTask(position: Int): Task {
        return data[position]
    }
    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bindData(data[position], onItemSelected)
    }

    fun setData(data: MutableList<Task>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun removeItem(adapterPosition: Int) {
        data.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }

    fun getData(): List<Task> = data
}