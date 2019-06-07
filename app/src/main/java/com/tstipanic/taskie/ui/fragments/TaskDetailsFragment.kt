package com.tstipanic.taskie.ui.fragments

import android.os.Bundle
import android.view.View
import com.tstipanic.taskie.R
import com.tstipanic.taskie.Taskie.Companion.instance
import com.tstipanic.taskie.common.EXTRA_TASK_ID
import com.tstipanic.taskie.common.RESPONSE_OK
import com.tstipanic.taskie.common.displayToast
import com.tstipanic.taskie.model.data.BackendTask
import com.tstipanic.taskie.model.data.PriorityColor
import com.tstipanic.taskie.persistance.db.TaskRoomRepository
import com.tstipanic.taskie.ui.fragments.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_task_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskDetailsFragment : BaseFragment(), UpdateTaskFragmentDialog.TaskUpdatedListener {

    private val repository = TaskRoomRepository()
    private var taskID = NO_TASK

    override fun getLayoutResourceId(): Int = R.layout.fragment_task_details


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString(EXTRA_TASK_ID)?.let { taskID = it }
        tryDisplayTask(taskID)
        updateButton.setOnClickListener { updateTask(taskID) }
    }

    private fun updateTask(taskID: String) {
        val dialog = UpdateTaskFragmentDialog.newInstance()
        dialog.taskId = taskID
        dialog.setTaskUpdatedListener(this)
        dialog.show(childFragmentManager, dialog.tag)
    }

    private fun tryDisplayTask(id: String) {
        try {
            val task = repository.getTask(id)
            displayTask(task)
        } catch (e: NoSuchElementException) {
            context?.displayToast(getString(R.string.noTaskFound))
        }
    }

    override fun onTaskUpdated(task: BackendTask) {
        tryDisplayTask(taskID)
    }

    private fun getTaskCallback(): Callback<BackendTask> = object : Callback<BackendTask> {
        override fun onFailure(call: Call<BackendTask>, t: Throwable) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onResponse(call: Call<BackendTask>, response: Response<BackendTask>) {
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK -> handleOkResponse(response)
                    else -> handleSomethingWentWrong()
                }
            }
        }
    }

    private fun handleSomethingWentWrong() = instance.displayToast("Something went wrong!")

    private fun handleOkResponse(response: Response<BackendTask>) {
        response.body()?.run {
            detailsTaskTitle.text = title
            detailsTaskDescription.text = content
            detailsPriorityView.setBackgroundResource(
                when (taskPriority) {
                    1 -> PriorityColor.LOW.getColor()
                    2 -> PriorityColor.MEDIUM.getColor()
                    else -> PriorityColor.HIGH.getColor()
                }
            )
        }

    }


    private fun displayTask(task: BackendTask) {
        detailsTaskTitle.text = task.title
        detailsTaskDescription.text = task.content
        detailsPriorityView.setBackgroundResource(
            when (task.taskPriority) {
                1 -> PriorityColor.LOW.getColor()
                2 -> PriorityColor.MEDIUM.getColor()
                else -> PriorityColor.HIGH.getColor()
            }
        )
    }

    companion object {
        const val NO_TASK = ""

        fun newInstance(taskId: String): TaskDetailsFragment {
            val bundle = Bundle().apply { putString(EXTRA_TASK_ID, taskId) }
            return TaskDetailsFragment().apply { arguments = bundle }
        }
    }
}