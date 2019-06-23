package com.tstipanic.taskie.ui.details_screen.fragments.task_details_fragment

import com.tstipanic.taskie.model.data.Task

interface TaskDetailsContract {

    interface View {

        fun showToast(text: String)

        fun displayTask(task: Task)


    }

    interface Presenter {

        fun setView(view: View)

        fun onDisplayTask(id: String)


    }
}