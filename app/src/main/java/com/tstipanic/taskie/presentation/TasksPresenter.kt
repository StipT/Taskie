package com.tstipanic.taskie.presentation

import android.util.Log
import com.tstipanic.taskie.common.RESPONSE_OK
import com.tstipanic.taskie.model.data.Task
import com.tstipanic.taskie.model.interactor.Interactor
import com.tstipanic.taskie.model.request.AddTaskRequest
import com.tstipanic.taskie.model.response.DeleteTaskResponse
import com.tstipanic.taskie.model.response.GetTasksResponse
import com.tstipanic.taskie.persistance.db.TaskieRepo
import com.tstipanic.taskie.ui.task_list_screen.fragments.TasksContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TasksPresenter(
    private val interactor: Interactor,
    private val repository: TaskieRepo
) : TasksContract.Presenter {

    private lateinit var view: TasksContract.View

    override fun setView(view: TasksContract.View) {
        this.view = view
    }

    override fun refreshTasks() {
        val unsentTasks = repository.getUnsentTasks()
        for (task in repository.getMarkedAsDeletedList()) {
            interactor.delete(task.id, deleteTaskCallback())
            repository.deleteTask(task)
        }
        if (view.isNetworkAvailable() && unsentTasks.isNotEmpty()) {
            for (task in unsentTasks) {
                if (task == unsentTasks.last()) {
                    interactor.save(AddTaskRequest(task.title, task.content, task.taskPriority), addLastTaskCallback())
                    repository.deleteTask(task)
                } else {
                    interactor.save(AddTaskRequest(task.title, task.content, task.taskPriority), addTaskCallback())
                    repository.deleteTask(task)
                }
            }
        } else if (view.isNetworkAvailable()) {
            interactor.getTasks(getTaskieCallback())
        } else {
            view.setList(repository.getAll())
        }
    }

    private fun addTaskCallback(): Callback<Task> = object : Callback<Task> {

        override fun onFailure(call: Call<Task>?, t: Throwable?) {
            Log.d("addTaskCallback", "Add Task failed")
            t?.printStackTrace()
        }

        override fun onResponse(call: Call<Task>, response: Response<Task>) {
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK -> handleOkAddTaskResponse(response.body())
                    else -> handleSomethingWentWrong()
                }
            }
        }
    }

    private fun handleOkAddTaskResponse(task: Task?) {
        view.displayToast(task?.title + " has been added.")
    }

    private fun handleSomethingWentWrong() = view.displayToast("Something went wrong!")


    private fun addLastTaskCallback(): Callback<Task> = object : Callback<Task> {

        override fun onFailure(call: Call<Task>?, t: Throwable?) {
            Log.d("addLastTaskCallback", "Add Task failed")
            t?.printStackTrace()
        }

        override fun onResponse(call: Call<Task>, response: Response<Task>) {
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK -> handleOkAddLastTaskResponse(response.body())
                    else -> handleSomethingWentWrong()
                }
            }
        }
    }

    private fun handleOkAddLastTaskResponse(task: Task?) {
        interactor.getTasks(getTaskieCallback())
    }


    private fun getTaskieCallback(): Callback<GetTasksResponse> = object : Callback<GetTasksResponse> {
        override fun onFailure(call: Call<GetTasksResponse>, t: Throwable) {
            view.setList(repository.getAll())
            Log.d("GetTasksResponse", "Failed")

        }

        override fun onResponse(call: Call<GetTasksResponse>, response: Response<GetTasksResponse>) {
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK -> handleOkGetTasksResponse(response)
                    else -> handleSomethingWentWrongTasksResponse()
                }
            }
        }
    }

    private fun handleSomethingWentWrongTasksResponse() {
        view.setList(repository.getAll())
    }

    private fun handleOkGetTasksResponse(response: Response<GetTasksResponse>) {
        response.body()?.notes?.run {
            for (task in this) {
                repository.insertTask(task)
            }
            view.setList(this)

        }
    }

    override fun getTasksFromLocalDatabase() = view.setList(repository.getAll())


    override fun getTasksByPriority() = view.setList(repository.orderTaskByPriotity())

    override fun deleteTask(id: String) {
        repository.markAsDeleted(id)
        interactor.delete(id, deleteTaskCallback())

    }

    override fun deleteAll() {
        val tasksForDeletion = repository.getAll()

        for (task in tasksForDeletion) {
            deleteTask(task.id)
            if (task == tasksForDeletion.last()) {
                refreshTasks()
            }
        }
        repository.deleteAll()
    }

    private fun deleteTaskCallback() = object : Callback<DeleteTaskResponse> {
        override fun onFailure(call: Call<DeleteTaskResponse>, t: Throwable) {
            view.displayToast("Task is marked as deleted. Connect with the internet to delete task.")
            Log.e("deleteTaskCallback", "Deletion failed")
        }

        override fun onResponse(call: Call<DeleteTaskResponse>, response: Response<DeleteTaskResponse>) {
            view.displayToast("Deletion successful")
            Log.e("deleteTaskCallback", "Deletion successful " + response.body())
        }
    }

    override fun insertTaskInLocalDb(task: Task) = repository.insertTask(task)
}