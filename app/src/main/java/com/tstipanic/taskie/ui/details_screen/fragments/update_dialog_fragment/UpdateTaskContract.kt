package com.tstipanic.taskie.ui.details_screen.fragments.update_dialog_fragment

import com.tstipanic.taskie.model.data.Task

interface UpdateTaskContract {


    interface View {

        fun fillFields(task: Task)

        fun updateTask()

        fun closeDialog()

        fun onTaskUpdated(task: Task)

        fun showToast(text: String)


    }

    interface Presenter {

        fun setView(view: View)

        fun onUpdateTask(id: String, title: String, content: String, priority: Int)

        fun getTaskFromDb(id: String)


    }
}