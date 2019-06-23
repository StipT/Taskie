package com.tstipanic.taskie.presentation

import com.tstipanic.taskie.common.RESPONSE_OK
import com.tstipanic.taskie.model.data.Task
import com.tstipanic.taskie.model.interactor.Interactor
import com.tstipanic.taskie.model.request.AddTaskRequest
import com.tstipanic.taskie.persistance.SharedPrefs
import com.tstipanic.taskie.persistance.db.TaskieRepo
import com.tstipanic.taskie.ui.task_list_screen.fragments.add_task_dialog.AddTaskContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddTaskPresenter(
    private val interactor: Interactor,
    private val repository: TaskieRepo
) : AddTaskContract.Presenter {

    private lateinit var view: AddTaskContract.View

    override fun setView(view: AddTaskContract.View) {
        this.view = view
    }


    override fun saveTask(title: String, content: String, priority: Int) {
        SharedPrefs.store(priority - 1)
        val task = Task(id = title.plus(content.toHashSet()), title = title, content = content, taskPriority = priority)
        task.isSent = false
        repository.insertTask(task)
        interactor.save(AddTaskRequest(task.title, task.content, task.taskPriority), addTaskCallback())
    }

    private fun addTaskCallback(): Callback<Task> = object : Callback<Task> {
        override fun onFailure(call: Call<Task>?, t: Throwable?) {
            view.closeDialog()

        }

        override fun onResponse(call: Call<Task>?, response: Response<Task>) {
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK -> handleOkResponse(response.body())
                    else -> handleSomethingWentWrong()
                }
            }
        }
    }

    private fun handleOkResponse(task: Task?) = task?.run {
        repository.deleteTask(
            Task(
                id = task.title.plus(content.toHashSet()),
                title = title,
                content = content,
                taskPriority = taskPriority
            )
        )
        task.isSent = true
        view.onTaskiesReceived(this)
    }


    private fun handleSomethingWentWrong() {
        view.displayToast("Something went wrong!")
        view.closeDialog()
    }
}