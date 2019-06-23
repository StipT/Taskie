package com.tstipanic.taskie.ui.details_screen.fragments.task_details_fragment

import android.os.Bundle
import android.view.View
import com.tstipanic.taskie.R
import com.tstipanic.taskie.common.EXTRA_TASK_ID
import com.tstipanic.taskie.common.displayToast
import com.tstipanic.taskie.model.data.PriorityColor
import com.tstipanic.taskie.model.data.Task
import com.tstipanic.taskie.ui.base.BaseFragment
import com.tstipanic.taskie.ui.details_screen.fragments.update_dialog_fragment.UpdateTaskFragmentDialog
import kotlinx.android.synthetic.main.fragment_task_details.*
import org.koin.android.ext.android.inject

class TaskDetailsFragment : BaseFragment(), TaskDetailsContract.View,
    UpdateTaskFragmentDialog.TaskUpdatedListener {

    private val presenter by inject<TaskDetailsContract.Presenter>()
    private var taskID = NO_TASK


    companion object {
        const val NO_TASK = ""

        fun newInstance(taskId: String): TaskDetailsFragment {
            val bundle = Bundle().apply { putString(EXTRA_TASK_ID, taskId) }
            return TaskDetailsFragment()
                .apply { arguments = bundle }
        }
    }

    override fun getLayoutResourceId(): Int = R.layout.fragment_task_details


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setView(this)
        arguments?.getString(EXTRA_TASK_ID)?.let { taskID = it }
        updateButton.setOnClickListener { updateTask(taskID) }

        presenter.onDisplayTask(taskID)
    }

    private fun updateTask(taskID: String) {
        val dialog = UpdateTaskFragmentDialog.newInstance()
        dialog.taskId = taskID
        dialog.setTaskUpdatedListener(this)
        dialog.show(childFragmentManager, dialog.tag)
    }

    override fun onTaskUpdated(task: Task?) {
        if (task != null) {
            presenter.onDisplayTask(taskID)
        }
    }

    override fun displayTask(task: Task) {
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

    override fun showToast(text: String) {
        activity?.displayToast(text)
    }
}