package com.tstipanic.taskie.ui.task_list_screen.fragments

import com.tstipanic.taskie.model.data.Task

interface TasksContract {

    interface View {

        fun initUi()

        fun displayToast(text: String)

        fun setList(list: List<Task>)

        fun isNetworkAvailable(): Boolean

    }

    interface Presenter {

        fun setView(view: View)

        fun refreshTasks()

        fun getTasksFromLocalDatabase()

        fun getTasksByPriority()

        fun deleteTask(id: String)

        fun deleteAll()

        fun insertTaskInLocalDb(task: Task)
    }
}