package com.tstipanic.taskie.presentation

import com.tstipanic.taskie.common.RESPONSE_OK
import com.tstipanic.taskie.model.data.Task
import com.tstipanic.taskie.model.interactor.Interactor
import com.tstipanic.taskie.model.request.UpdateTaskRequest
import com.tstipanic.taskie.persistance.db.TaskieRepo
import com.tstipanic.taskie.ui.details_screen.fragments.update_dialog_fragment.UpdateTaskContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateTaskPresenter(
    private val interactor: Interactor,
    private val repository: TaskieRepo
) : UpdateTaskContract.Presenter {

    private lateinit var view: UpdateTaskContract.View

    override fun setView(view: UpdateTaskContract.View) {
        this.view = view
    }


    override fun onUpdateTask(id: String, title: String, content: String, priority: Int) {
        interactor.editNote(UpdateTaskRequest(id, title, content, priority), updateTaskCallback())
        repository.updateTask(id, title, content, priority)
    }

    override fun getTaskFromDb(id: String) {
        view.fillFields(repository.getTask(id))
    }

    private fun updateTaskCallback() = object : Callback<Task> {
        override fun onFailure(call: Call<Task>, t: Throwable) {
            view.showToast("Update Failed!")
//            onTaskiesUpdated(repository.getTask(taskId))
        }

        override fun onResponse(call: Call<Task>, response: Response<Task>) {
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK -> handleOkResponseUpdateTask(response)
                    else -> handleSomethingWentWrongUpdateTask()
                }
            }
        }
    }

    private fun handleSomethingWentWrongUpdateTask() {
        view.showToast("Update Failed!")
        view.closeDialog()
    }


    private fun handleOkResponseUpdateTask(response: Response<Task>) {
        response.body()?.run {
            onTaskiesUpdated(this)
        }
    }


    private fun onTaskiesUpdated(task: Task) {
        view.onTaskUpdated(task)
        view.closeDialog()
    }


}