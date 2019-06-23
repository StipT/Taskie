package com.tstipanic.taskie.presentation


import com.tstipanic.taskie.persistance.db.TaskieRepo
import com.tstipanic.taskie.ui.details_screen.fragments.task_details_fragment.TaskDetailsContract

class TaskDetailsPresenter(
    private val repository: TaskieRepo
) : TaskDetailsContract.Presenter {

    private lateinit var view: TaskDetailsContract.View
    override fun setView(view: TaskDetailsContract.View) {
        this.view = view
    }

    override fun onDisplayTask(id: String) {
        try {
            view.displayTask(repository.getTask(id))

        } catch (e: NoSuchElementException) {
            view.showToast("Task not found")
        }
    }
}