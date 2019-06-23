package com.tstipanic.taskie.ui.task_list_screen.fragments.add_task_dialog

import com.tstipanic.taskie.model.data.Task


interface AddTaskContract {

    interface View {

        fun initUi()

        fun readInputs()

        fun clearInputs()

        fun displayToast(text: String)

        fun closeDialog()

        fun onTaskiesReceived(task: Task)
    }

    interface Presenter {

        fun setView(view: View)

        fun saveTask(title: String, content: String, priority: Int)
    }
}